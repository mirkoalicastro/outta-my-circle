package com.acg.outtamycircle.network;

import android.support.annotation.NonNull;

import com.acg.outtamycircle.network.googleimpl.MessageReceiver;
import com.acg.outtamycircle.network.googleimpl.MyGoogleRoom;
import com.badlogic.androidgames.framework.Pool;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class NetworkMessageHandlerImpl implements NetworkMessageHandler {
    private static final int MAX_BUFFER_SIZE = 127;
    private static final byte ENDING_CHAR = 127;
    private static final int MAX_CAPACITY = 40;
    private static final int INITIAL_CAPACITY = 20;

    private MessageReceiver first, second;

    private byte[] buffer = new byte[MAX_BUFFER_SIZE];
    private int currentBufferSize = 0;

    private final MyGoogleRoom myGoogleRoom;

    public NetworkMessageHandlerImpl(MyGoogleRoom myGoogleRoom) {
        this.myGoogleRoom = myGoogleRoom;
        for(int i=0; i<INITIAL_CAPACITY; i++)
            bufferPool.free(new byte[MAX_BUFFER_SIZE]);
    }

    public NetworkMessageHandlerImpl setReceivers(MessageReceiver first, MessageReceiver second) {
        this.first = first;
        this.second = second;
        return this;
    }

    @Override
    public void sendReliable(final String playerId) {
        sendReliable(playerId, true, null);
    }

    public interface OnComplete {
        void work(Task<Integer> task);
    }

    public void sendReliable(final String playerId, OnComplete onComplete) {
        sendReliable(playerId, true, onComplete);
    }

    private final Pool<Object> bufferPool = new Pool.SynchronizedPool<>(new Pool.PoolObjectFactory<Object>() {
        @Override
        public Object createObject() {
            return new byte[MAX_BUFFER_SIZE];
        }
    }, MAX_CAPACITY);

    private void sendReliable(final String playerId, boolean clearBuffer, final OnComplete onComplete) {
        final byte[] toSend = (byte[]) bufferPool.newObject();
        System.arraycopy(buffer, 0, toSend, 0, currentBufferSize);
        if(currentBufferSize < MAX_BUFFER_SIZE)
            toSend[currentBufferSize] = ENDING_CHAR;

        myGoogleRoom.getRealTimeMultiplayerClient()
                .sendReliableMessage(toSend, myGoogleRoom.getRoomId(), playerId, null)
                .addOnCompleteListener(new OnCompleteListener<Integer>() {
                    @Override
                    public void onComplete(@NonNull Task<Integer> task) {
                        bufferPool.free(toSend);
                        if(onComplete != null)
                            onComplete.work(task);
                    }
                });
        if(clearBuffer)
            clearBuffer();
    }

    @Override
    public void sendUnreliable(String player) {
        final byte[] toSend = (byte[]) bufferPool.newObject();
        System.arraycopy(buffer, 0, toSend, 0, currentBufferSize);
        if(currentBufferSize < MAX_BUFFER_SIZE)
            toSend[currentBufferSize] = ENDING_CHAR;
        String roomId = myGoogleRoom.getRoomId();
        myGoogleRoom.getRealTimeMultiplayerClient()
                .sendUnreliableMessage(toSend, roomId, player)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        bufferPool.free(toSend);
                    }
                });
        clearBuffer();
    }

    @Override
    public void broadcastReliable() {
        for(String playerId: myGoogleRoom.getParticipantIds())
            if(!playerId.equals(myGoogleRoom.getPlayerId()))
                sendReliable(playerId, false, null);
        clearBuffer();
    }

    public void broadcastReliable(OnComplete onComplete) {
        for(String playerId: myGoogleRoom.getParticipantIds())
            if(!playerId.equals(myGoogleRoom.getPlayerId()))
                sendReliable(playerId, false, onComplete);
        clearBuffer();
    }

    @Override
    public void broadcastUnreliable() {
        final byte[] toSend = (byte[]) bufferPool.newObject();
        System.arraycopy(buffer, 0, toSend, 0, currentBufferSize);
        if(currentBufferSize < MAX_BUFFER_SIZE)
            toSend[currentBufferSize] = ENDING_CHAR;
        myGoogleRoom.getRealTimeMultiplayerClient()
                .sendUnreliableMessageToOthers(toSend, myGoogleRoom.getRoomId())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        bufferPool.free(toSend);
                    }
                });
        clearBuffer();
    }

    @Override
    public boolean putInBuffer(GameMessage message) {
        GameMessage.Type type = message.getType();
        if(currentBufferSize+type.length>=MAX_BUFFER_SIZE)
            return false;
        message.putInBuffer(buffer, currentBufferSize);
        currentBufferSize += type.length;
        buffer[currentBufferSize] = ENDING_CHAR;
        return true;
    }

    @Override
    public Iterable<GameMessage> getMessages() {
        synchronized (myGoogleRoom) {
            MessageReceiver tmp = first;
            second.clear();
            first = second;
            second = tmp;
            return tmp.getMessages();
        }
    }

    @Override
    public void onRealTimeMessageReceived(@NonNull RealTimeMessage realTimeMessage) {
        synchronized (myGoogleRoom) {
            byte[] messageData = realTimeMessage.getMessageData();
            int cursor = 0;

            while (cursor < messageData.length && messageData[cursor] != ENDING_CHAR) {
                GameMessage message = GameMessage.createInstance();
                message.setSender(realTimeMessage.getSenderParticipantId());
                int length = GameMessage.Type.values()[messageData[cursor]].length;
                System.arraycopy(messageData, cursor, message.buffer, 0, length);
                first.storeMessage(message);
                cursor += length;
            }
        }
    }

    @Override
    public void clearBuffer(){
        currentBufferSize = 0;
        buffer[0] = ENDING_CHAR;
    }
}
