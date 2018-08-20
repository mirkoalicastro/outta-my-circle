package com.acg.outtamycircle.network;

public interface GameMessageInterpreter {
    public GameMessage.Type getType(GameMessage gameMessage);
    public int getObjectId(GameMessage gameMessage);
}
