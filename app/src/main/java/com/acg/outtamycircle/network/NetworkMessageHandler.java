package com.acg.outtamycircle.network;
import android.support.v4.util.Pools.SimplePool;
import android.support.v4.util.Pools.Pool;

import java.util.List;

public interface NetworkMessageHandler {

    void sendReliable();

    void sendUnreliable();

    void broadcastReliable();

    void broadcastUnreliable();

    void putInBuffer(GameMessage message);

    List<GameMessage> getMessages();
}
