package com.acg.outtamycircle.network;

import com.google.android.gms.games.multiplayer.realtime.OnRealTimeMessageReceivedListener;

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
     * @return true if the buffer can contain the message, false otherwise.
     */
    boolean putInBuffer(GameMessage message);

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
