package com.acg.outtamycircle.network;

import android.support.annotation.NonNull;
import android.util.Log;

import com.acg.outtamycircle.network.googleimpl.MyGoogleRoom;
import com.acg.outtamycircle.network.googleimpl.MessageReceiver;
import com.badlogic.androidgames.framework.Pool;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Arrays;

public class NetworkMessageHandlerImpl implements NetworkMessageHandler {
    private static final int MAX_BUFFER_SIZE = 400; //TODO
    private static final byte ENDING_CHAR = 127;

    private MessageReceiver first, second;

    //TODO visibility
    byte[] buffer = new byte[MAX_BUFFER_SIZE];
    private int currentBufferSize = 0;

    private final MyGoogleRoom myGoogleRoom;

    public NetworkMessageHandlerImpl(MyGoogleRoom myGoogleRoom) {
        this.myGoogleRoom = myGoogleRoom;
    }

    //TODO change
    public NetworkMessageHandlerImpl setReceivers(MessageReceiver first, MessageReceiver second) {
        this.first = first;
        this.second = second;
        return this;
    }

    @Override
    public void sendReliable(final String playerId) {
        sendReliable(playerId, true);
    }

//    private final Pools.Pool<Object> bufferSyncPool = new Pools.SynchronizedPool<>(100);

    private final com.badlogic.androidgames.framework.Pool<Object> bufferPool = new Pool.SynchronizedPool<>(new Pool.PoolObjectFactory<Object>() {
        @Override
        public Object createObject() {
            return new byte[MAX_BUFFER_SIZE];
        }
    }, 20);

    {
//        for(int i=0; i<100; i++)
  //          bufferSyncPool.release(new byte[MAX_BUFFER_SIZE]);

        for(int i=0; i<20; i++)
            bufferPool.free(new byte[MAX_BUFFER_SIZE]);
    }

    private void sendReliable(final String playerId, boolean clearBuffer) {
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
        Log.d("MACHENESACC", "realtime " + Boolean.toString(myGoogleRoom.getRealTimeMultiplayerClient() != null));
        Log.d("MACHENESACC", "toSend " + Boolean.toString(toSend != null));
        Log.d("MACHENESACC", "roomId " + Boolean.toString(roomId != null));
        Log.d("MACHENESACC", "player " + Boolean.toString(player != null));
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
    public void broadcastReliable(){
        for(String playerId: myGoogleRoom.getParticipantIds())
            if(!playerId.equals(myGoogleRoom.getPlayerId()))
                sendReliable(playerId, false);
        clearBuffer();
    }

    @Override
    public void broadcastUnreliable() {
        final byte[] toSend = (byte[]) bufferPool.newObject();
        System.arraycopy(buffer, 0, toSend, 0, currentBufferSize);
        if(currentBufferSize < MAX_BUFFER_SIZE)
            toSend[currentBufferSize] = ENDING_CHAR;
        myGoogleRoom.getRealTimeMultiplayerClient()
                .sendUnreliableMessageToOthers(Arrays.copyOf(buffer, currentBufferSize), myGoogleRoom.getRoomId())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        bufferPool.free(toSend);
                    }
                });
        clearBuffer();
    }

    @Override
    public void putInBuffer(GameMessage message) {
        //TODO se ci sono troppi messaggi? RuntimeException?
        GameMessage.Type type = message.getType();
        message.putInBuffer(buffer, currentBufferSize);
        currentBufferSize += type.length;
        buffer[currentBufferSize] = ENDING_CHAR;
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
                message.copyBuffer(messageData, cursor, cursor + length - 1); //TODO check
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
