package com.acg.outtamycircle.network;

import android.support.annotation.NonNull;
import android.util.Log;

import com.acg.outtamycircle.network.googleimpl.MyGoogleRoom;
import com.acg.outtamycircle.network.googleimpl.MessageReceiver;
import com.badlogic.androidgames.framework.Pool;
import com.google.android.gms.games.multiplayer.Participant;
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

    private final Pool<Object> bufferPool = new Pool<>(new Pool.PoolObjectFactory<Object>() {
        @Override
        public Object createObject() {
            return new byte[MAX_BUFFER_SIZE];
        }
    }, 10);

    private void sendReliable(final String playerId, boolean clearBuffer) {
        byte[] toSenx;
        synchronized (bufferPool) {
            toSenx = (byte[]) bufferPool.newObject();
        }
        final byte[] toSend_ = toSenx;
        for(int i=0;i<currentBufferSize;i++)
            toSend_[i] = buffer[i];
        if(currentBufferSize != MAX_BUFFER_SIZE)
            toSend_[currentBufferSize] = 127;
        for(int i=currentBufferSize+1; i<MAX_BUFFER_SIZE; i++)
            toSend_[i] = 0;
        final byte toSend[] = Arrays.copyOf(buffer,currentBufferSize); //TODO no allocation!
//        Log.d("LAGLOG", "sono uguali? " + Arrays.equals(toSend, toSend_));
//        Log.d("LAGLOG", "to send: \n" + Arrays.toString(toSend) + "\ne invece tosend__: \n" + Arrays.toString(toSend_));

        final Task<Integer> sendTask = myGoogleRoom.getRealTimeMultiplayerClient()
                .sendReliableMessage(toSend, myGoogleRoom.getRoomId(), playerId, null); //TODO toSend_
        sendTask.addOnCompleteListener(new OnCompleteListener<Integer>() {
            @Override
            public void onComplete(@NonNull Task<Integer> task) {
                synchronized (bufferPool) {
                    bufferPool.free(toSend_);
                //    bufferPool.free(toSend);
                }
            }
        });
        if(clearBuffer)
            clearBuffer();
    }

    @Override
    public void sendUnreliable(String player) {
        myGoogleRoom.getRealTimeMultiplayerClient().sendUnreliableMessage(Arrays.copyOf(buffer, currentBufferSize), myGoogleRoom.getRoomId(), player);
        clearBuffer();
    }

    @Override
    public void broadcastReliable(){
        //TODO
        for(Participant player: myGoogleRoom.getRoom().getParticipants()) {
            String playerId = player.getParticipantId();
            if(!playerId.equals(myGoogleRoom.getPlayerId())) {
                sendReliable(playerId, false);
            }
        }
        clearBuffer();
    }

    @Override
    public void broadcastUnreliable() {
        final Task<Void> task = myGoogleRoom.getRealTimeMultiplayerClient().sendUnreliableMessageToOthers(Arrays.copyOf(buffer, currentBufferSize), myGoogleRoom.getRoomId());
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
                GameMessage message = GameMessage.createInstance();//pool.acquire(); //TODO pool handle
                message.setSender(realTimeMessage.getSenderParticipantId());
                int length = GameMessage.Type.values()[messageData[cursor]].length;
                Log.d("LAGLOG", Arrays.toString(messageData) + "\n\n\n" + cursor + " ; " + (cursor + length - 1));
                message.copyBuffer(messageData, cursor, cursor + length - 1); //TODO check
                Log.d("JUANNINO", "Il tipo e' " + message.getType().toString());
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
