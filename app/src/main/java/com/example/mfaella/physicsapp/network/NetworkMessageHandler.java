package com.example.mfaella.physicsapp.network;
import android.support.v4.util.Pools.SimplePool;
import android.support.v4.util.Pools.Pool;

import java.util.List;

public interface NetworkMessageHandler {

    void writeReliable(String player, GameMessage message);

    void writeUnreliable(String player, GameMessage message);

    void broadcastReliable(GameMessage message);

    void broadcastUnreliable(GameMessage message);

    List<GameMessage> getMessages();
}
