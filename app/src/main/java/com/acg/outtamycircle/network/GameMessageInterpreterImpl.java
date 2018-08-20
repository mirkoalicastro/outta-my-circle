package com.acg.outtamycircle.network;

public abstract class GameMessageInterpreterImpl implements GameMessageInterpreter{

    protected short getShortAt(GameMessage gameMessage, int pos){
        int s = gameMessage.buffer[pos+1];
        s = s>>>4;
        s += gameMessage.buffer[pos];
        return (short) s;
    }

    @Override
    public GameMessage.Type getType(GameMessage gameMessage){
        return GameMessage.Type.fromByte(gameMessage.buffer[0]);
    }

    @Override
    public int getObjectId(GameMessage gameMessage){
        //TODO implement
        return 1;
    }
}
