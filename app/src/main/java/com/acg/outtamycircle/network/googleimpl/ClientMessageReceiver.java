package com.acg.outtamycircle.network.googleimpl;

import com.acg.outtamycircle.network.GameMessage;

import java.util.ArrayList;
import java.util.List;

public class ClientMessageReceiver implements MessageReceiver {
    private List<GameMessage> messages = new ArrayList<>();//TODO capacity

    @Override
    public void storeMessage(GameMessage message) {
        messages.add(message);
    }

    @Override
    public Iterable<GameMessage> getMessages() {
        return messages;
    }

    @Override
    public void clear() {
        messages.clear();
    }
}
