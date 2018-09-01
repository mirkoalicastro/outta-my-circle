package com.acg.outtamycircle.network;
import android.support.v4.util.Pools.SimplePool;
import android.support.v4.util.Pools.Pool;

import com.google.android.gms.games.multiplayer.realtime.OnRealTimeMessageReceivedListener;

import java.util.List;

/**
 * Handles the sending and the receiving of messages.
 */
public interface NetworkMessageHandler extends OnRealTimeMessageReceivedListener {

    /**
     * Sends reliably the messages in the buffer to one player.
     * @param player
     */
    void sendReliable(String player);

    /**
     * Sends unreliably the messages in the buffer to one player.
     * @param player
     */
    void sendUnreliable(String player);

    /**
     * Broadcasts reliably the messages in the buffer to all player.
     */
    void broadcastReliable();

    /**
     * Broadcasts unreliably the messages in the buffer to all player.
     */
    void broadcastUnreliable();

    /**
     * Puts a message in the buffer.
     * @param message
     */
    void putInBuffer(GameMessage message);

    /**
     * Returns all current inbox messages. The method should not be called before all the messages have been read.
     *
     * @return an object that allows the iteration through the messages.
     */
    Iterable<GameMessage> getMessages();

    /**
     * Clears the message buffer.
     */
    void clearBuffer();
}
