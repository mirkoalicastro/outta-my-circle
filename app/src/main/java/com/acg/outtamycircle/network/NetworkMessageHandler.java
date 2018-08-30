package com.acg.outtamycircle.network;
import android.support.v4.util.Pools.SimplePool;
import android.support.v4.util.Pools.Pool;

import com.google.android.gms.games.multiplayer.realtime.OnRealTimeMessageReceivedListener;

import java.util.List;

/**
 * Handles the sending and the receiving of messages.
 */
public interface NetworkMessageHandler extends OnRealTimeMessageReceivedListener {

    void sendReliable();

    void sendUnreliable();

    void broadcastReliable();

    void broadcastUnreliable();

    void putInBuffer(GameMessage message);

    Iterable<GameMessage> getMessages();
}
