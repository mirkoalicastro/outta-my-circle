package com.example.mfaella.physicsapp.network;
import android.support.v4.util.Pools.SimplePool;
import android.support.v4.util.Pools.Pool;

public abstract class NetworkMessageHandler {

    protected Pool<GameMessage> messagePool = new SimplePool<>(10);


    abstract void write(/*COSA?*/);

    public abstract void broadcast();

    abstract GameMessage getMessage();
}
