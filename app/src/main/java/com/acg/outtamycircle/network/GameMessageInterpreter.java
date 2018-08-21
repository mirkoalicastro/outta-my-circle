package com.acg.outtamycircle.network;

public interface GameMessageInterpreter {
    GameMessage.Type getType(GameMessage gameMessage);
    int getObjectId(GameMessage gameMessage);
}
