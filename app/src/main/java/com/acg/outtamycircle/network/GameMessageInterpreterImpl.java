package com.acg.outtamycircle.network;

public abstract class GameMessageInterpreterImpl implements GameMessageInterpreter{

    protected short getShortAt(GameMessage gameMessage, int pos){
        byte[] buffer = gameMessage.getBuffer();
        int s = buffer[pos+1];
        s = s>>>4;
        s += buffer[pos];
        return (short) s;
    }

    @Override
    public GameMessage.Type getType(GameMessage gameMessage){
        return GameMessage.Type.fromByte(gameMessage.getBuffer()[0]);
    }

    @Override
    public int getObjectId(GameMessage gameMessage){
        //TODO implement
        return 1;
    }
}
