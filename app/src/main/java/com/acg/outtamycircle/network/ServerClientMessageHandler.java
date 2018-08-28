package com.acg.outtamycircle.network;

import android.support.annotation.NonNull;
import android.support.v4.util.Pools;

import com.google.android.gms.games.RealTimeMultiplayerClient;
import com.google.android.gms.games.multiplayer.realtime.OnRealTimeMessageReceivedListener;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessage;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class ServerClientMessageHandler implements NetworkMessageHandler {
    private RealTimeMultiplayerClient client;
    private List<GameMessage> first, second;
    private Pools.Pool<GameMessage> pool;
    private String player;
    private static final int MAX_BUFFER_SIZE = 400; //TODO

    private String roomId;

    private byte[] buffer = new byte[MAX_BUFFER_SIZE];
    private int currentBufferSize = 0;

    public ServerClientMessageHandler(){
        int capacity = 40;
        first = new ArrayList<>(capacity);
        second = new ArrayList<>(capacity);
        capacity = capacity*2;
        pool = new Pools.SimplePool<>(capacity);
        for(int i=0; i<capacity; i++)
            pool.release(new GameMessage());

    }

    public void setClient(RealTimeMultiplayerClient client){
        this.client = client;
    }

    @Override
    public void sendReliable() {
        //TODO
        client.sendReliableMessage(buffer, roomId, player,null);
    }

    @Override
    public void sendUnreliable() {
        //TODO parametri per GPGS
        //Games.getRealTimeMultiplayerClient()

    }

    @Override
    public void broadcastReliable(){

    }

    @Override
    public void broadcastUnreliable() {

    }

    @Override
    public void putInBuffer(GameMessage message) {
        message.copyBuffer(buffer, currentBufferSize, message.type.length);
        currentBufferSize += message.type.length;
        buffer[currentBufferSize] = 127;
    }

    @Override
    public synchronized List<GameMessage> getMessages() {
        List<GameMessage> tmp = first;
        second.clear();
        first = second;
        second = tmp;
        return tmp;
    }

    @Override
    public synchronized void onRealTimeMessageReceived(@NonNull RealTimeMessage realTimeMessage) {
        byte[] messageData = realTimeMessage.getMessageData();
        int cursor = 0;
        byte endByte = 127;

        while( cursor < messageData.length && messageData[cursor]!=endByte) {
            GameMessage gameMessage = pool.acquire();
            int length = GameMessage.Type.values()[messageData[cursor]].length;
            gameMessage.copyBuffer(messageData, cursor, cursor + length - 1); //TODO check
            first.add(gameMessage);
            cursor += length;
        }
    }

}
