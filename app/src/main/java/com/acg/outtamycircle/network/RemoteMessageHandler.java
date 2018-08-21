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

public class RemoteMessageHandler implements NetworkMessageHandler, OnRealTimeMessageReceivedListener{
    private RealTimeMultiplayerClient client;
    private List<GameMessage> buffer;
    private List<GameMessage> second;
    private Pools.Pool<GameMessage> pool;
    private Map<GameMessage.Type,byte[]> messageBuffers;

    public RemoteMessageHandler(int capacity){
        buffer = new ArrayList<>(capacity);
        second = new ArrayList<>(capacity);
        capacity = capacity*2;
        pool = new Pools.SimplePool<>(capacity);
        for(int i=0; i<capacity; i++)
            pool.release(new GameMessage());

        messageBuffers = new EnumMap<GameMessage.Type, byte[]>(GameMessage.Type.class);
        for(GameMessage.Type t : GameMessage.Type.values())
            messageBuffers.put(t, new byte[GameMessage.Type.lengthInBytes(t)]);

    }

    public void setClient(RealTimeMultiplayerClient client){
        this.client = client;
    }

    @Override
    public void writeReliable(String player, GameMessage message) {
        //TODO parametri per GPGS
        //Games.getRealTimeMultiplayerClient()
        GameMessage.Type t = message.getType();
        byte buffer[] = messageBuffers.get(t);

        message.putInBuffer(buffer);

    }

    @Override
    public void writeUnreliable(String player, GameMessage message) {
        //TODO parametri per GPGS
        //Games.getRealTimeMultiplayerClient()

    }

    @Override
    public void broadcastReliable(GameMessage message){

    }

    @Override
    public void broadcastUnreliable(GameMessage message) {

    }

    @Override
    public List<GameMessage> getMessages() {
        //TODO sync
        List<GameMessage> tmp = buffer;
        second.clear();
        buffer = second;
        second = tmp;
        return tmp;
    }

    @Override
    public void onRealTimeMessageReceived(@NonNull RealTimeMessage realTimeMessage) {
        //TODO sync
        GameMessage gameMessage = pool.acquire();
        byte[] messageData = realTimeMessage.getMessageData();

        //TODO getMessage
        gameMessage.setType(GameMessage.Type.fromByte(messageData[0]));


        buffer.add(gameMessage);
    }

}
