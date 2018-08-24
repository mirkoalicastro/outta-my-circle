package com.acg.outtamycircle.network;

public class GameMessageInterpreterImpl implements GameMessageInterpreter{

    @Override
    public GameMessage.Type getType(GameMessage gameMessage){
        return gameMessage.type;
    }

    @Override
    public int getObjectId(GameMessage gameMessage){
        //TODO implement
        return 1;
    }


}
