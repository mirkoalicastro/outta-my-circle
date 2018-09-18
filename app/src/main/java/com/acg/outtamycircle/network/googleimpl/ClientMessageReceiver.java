package com.acg.outtamycircle.network.googleimpl;

import com.acg.outtamycircle.network.GameMessage;

import java.util.ArrayList;
import java.util.List;

public class ClientMessageReceiver implements MessageReceiver {
    private List<GameMessage> messages;
    private static final int DEFAULT_CAPACITY = 40;

    @Override
    public int size(){
        return messages.size();
    }

    public ClientMessageReceiver(int capacity){
        this.messages = new ArrayList<GameMessage>(capacity);
    }

    public ClientMessageReceiver(){
        this(DEFAULT_CAPACITY);
    }

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
