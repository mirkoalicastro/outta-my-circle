package com.acg.outtamycircle.network.googleimpl;

import android.support.annotation.NonNull;

import com.acg.outtamycircle.network.GameMessage;
import com.acg.outtamycircle.network.GameMessageInterpreter;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ServerMessageReceiver implements MessageReceiver {
    private final GameMessage[][] messages;
    private GameMessageInterpreter interpreter;
    private int count = 0;

    public ServerMessageReceiver(GameMessageInterpreter interpreter, int numberOfPlayers) {
        this.interpreter = interpreter;
        messages = new GameMessage[numberOfPlayers][2];
    }

    private final Iterable<GameMessage> iterable = new Iterable<GameMessage>() {
        class MyIterator implements Iterator<GameMessage>{
            int player=0, message=0;

            void reset(){
                player=0; message=0;
                if(messages[player][message]==null)
                    findNext();
            }

            void findNext() {
                do {
                    if (message == messages[0].length - 1) {
                        player++;
                        message = 0;
                    } else {
                        message++;
                    }
                } while (player != messages.length && messages[player][message] == null);
            }

            @Override
            public boolean hasNext() {
                return player!=messages.length;
            }

            @Override
            public GameMessage next() {
                if(!hasNext())
                    throw new NoSuchElementException();
                GameMessage ret = messages[player][message];
                findNext();
                return ret;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Not implemented yet");
            }

        }

        private MyIterator iterator = new MyIterator();

        @NonNull
        @Override
        public Iterator<GameMessage> iterator() {
            iterator.reset();
            return iterator;
        }
    };

    @Override
    public int size() {
        return count;
    }

    @Override
    public void storeMessage(GameMessage message) {
        int j;
        int player = interpreter.getObjectId(message);
        GameMessage.Type type = message.getType();

        if(type == GameMessage.Type.MOVE_CLIENT)
            j = 0;
        else if(type == GameMessage.Type.ATTACK)
            j = 1;
        else
            return;

        if(messages[player][j]==null) count++;

        messages[player][j] = message;
    }

    @Override
    public Iterable<GameMessage> getMessages() {
        return iterable;
    }

    @Override
    public void clear() {
        for(int i=0 ; i< messages.length ; i++)
            for(int j=0 ; j<messages[0].length ; j++)
                messages[i][j] = null;
        count = 0;
    }
}
