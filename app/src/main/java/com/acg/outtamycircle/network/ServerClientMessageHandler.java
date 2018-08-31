package com.acg.outtamycircle.network;

import android.support.annotation.NonNull;
import android.support.v4.util.Pools;

import com.acg.outtamycircle.GameStatus;
import com.acg.outtamycircle.network.googleimpl.MessageReceiver;
import com.google.android.gms.games.RealTimeMultiplayerClient;
import com.google.android.gms.games.multiplayer.realtime.OnRealTimeMessageReceivedListener;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class ServerClientMessageHandler implements NetworkMessageHandler {
    private static final int MAX_BUFFER_SIZE = 400; //TODO
    private static final byte ENDING_CHAR = 127;
    private static final int DEFAULT_CAPACITY = 30;

    private RealTimeMultiplayerClient client;
    private MessageReceiver first, second;
    private Pools.Pool<GameMessage> pool;
    private String roomId;

    private byte[] buffer = new byte[MAX_BUFFER_SIZE];
    private int currentBufferSize = 0;

    public ServerClientMessageHandler setRoomId(String roomId){
        this.roomId = roomId;
        return this;
    }

    public ServerClientMessageHandler setClient(RealTimeMultiplayerClient client){
        this.client = client;
        return this;
    }

    @Override
    public void sendReliable(String player) {

        final Task<Integer> sendTask = client.sendReliableMessage(buffer, roomId, player, null);
        //TODO Check
        sendTask.addOnCompleteListener(new OnCompleteListener<Integer>() {
            @Override
            public void onComplete(@NonNull Task<Integer> task) {
                synchronized(task){
                    task.notify();
                }
            }
        });

        synchronized (sendTask){
            while(!sendTask.isComplete()){
                try {
                    sendTask.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    public void sendUnreliable(String player) {
        client.sendUnreliableMessage(buffer, roomId, player);
    }

    @Override
    public void broadcastReliable(){
        //TODO
    }

    @Override
    public void broadcastUnreliable() {
        client.sendUnreliableMessageToOthers(buffer,roomId);
    }

    @Override
    public void putInBuffer(GameMessage message) {
        //TODO se ci sono troppi messaggi? RuntimeException?
        message.copyBuffer(buffer, currentBufferSize, message.type.length);
        currentBufferSize += message.type.length;
        buffer[currentBufferSize] = ENDING_CHAR;
    }

    @Override
    public synchronized Iterable<GameMessage> getMessages() {
        MessageReceiver tmp = first;
        second.clear();
        first = second;
        second = tmp;
        return tmp.getMessages();
    }

    @Override
    public synchronized void onRealTimeMessageReceived(@NonNull RealTimeMessage realTimeMessage) {
        byte[] messageData = realTimeMessage.getMessageData();
        int cursor = 0;

        while( cursor < messageData.length && messageData[cursor]!=ENDING_CHAR) {
            GameMessage gameMessage = pool.acquire();
            int length = GameMessage.Type.values()[messageData[cursor]].length;
            gameMessage.copyBuffer(messageData, cursor, cursor + length - 1); //TODO check
            first.storeMessage(gameMessage);
            cursor += length;
        }
    }

    @Override
    public void clearBuffer(){
        currentBufferSize = 0;
        buffer[0] = ENDING_CHAR;
    }
}
