package com.acg.outtamycircle.network.googleimpl;
import com.acg.outtamycircle.network.GameMessage;

/**
 * Handles the storage of messages and the iteration through them.
 */
public interface MessageReceiver {

    int size();
    /**
     * Stores a messages.
     * @param message
     */
    void storeMessage(GameMessage message);

    /**
     * Returns a iterable queue of messages.
     * @return
     */
    Iterable<GameMessage> getMessages();

    /**
     * Clears the queue.
     */
    void clear();
}
