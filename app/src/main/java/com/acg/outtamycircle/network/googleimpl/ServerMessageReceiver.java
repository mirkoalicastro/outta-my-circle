package com.acg.outtamycircle.network.googleimpl;

import android.support.annotation.NonNull;

import com.acg.outtamycircle.network.GameMessage;
import com.acg.outtamycircle.network.GameMessageInterpreter;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ServerMessageReceiver implements MessageReceiver {
    private GameMessage messages[][];
    private GameMessageInterpreter interpreter;

    public ServerMessageReceiver(GameMessageInterpreter interpreter, int numberOfPlayers) {
        this.interpreter = interpreter;
        messages = new GameMessage[numberOfPlayers][2];
    }

    private Iterable<GameMessage> iterable = new Iterable<GameMessage>() {
        private Iterator<GameMessage> iterator = new Iterator<GameMessage>() {
            int player=0, message=0;
            @Override
            public boolean hasNext() {
                if(player==messages.length && message==0){
                    player = 0;
                    return false;
                }
                return true;
            }

            @Override
            public GameMessage next() {
                if(!hasNext()) throw new NoSuchElementException();
                GameMessage ret = messages[player][message];

                if(message==messages[0].length){
                    player++;
                    message = 0;
                } else {
                    message++;
                }
                
                return ret;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Not implemented yet");
            };
        };

        @NonNull
        @Override
        public Iterator<GameMessage> iterator() {
            return iterator;
        }
    };

    @Override
    public void storeMessage(GameMessage message) {
        int player = interpreter.getObjectId(message);
        GameMessage.Type type = message.type;

        if(type == GameMessage.Type.MOVE_CLIENT)
            messages[player][0] = message;
        else if(type == GameMessage.Type.ATTACK)
            messages[player][1] = message;
        else
            throw new RuntimeException(); //TODO????
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
    }
}
