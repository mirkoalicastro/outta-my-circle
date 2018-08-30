package com.acg.outtamycircle.network.googleimpl;
import com.acg.outtamycircle.network.GameMessage;


public interface MessageReceiver {
    void storeMessage(GameMessage message);
    Iterable<GameMessage> getMessages();
    void clear();
}
