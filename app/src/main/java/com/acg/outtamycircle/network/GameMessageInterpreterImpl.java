package com.acg.outtamycircle.network;

public class GameMessageInterpreterImpl implements GameMessageInterpreter{
    // SHARED
    private static final int MESSAGE_TYPE_IDX = 0;
    private static final int OBJECT_ID_IDX = 1;
    private static final int POS_X_IDX = 3;
    private static final int POS_Y_IDX = 5;

    // INIT
    private static final int SKIN_ID_IDX = 1;
    private static final int ATTACK_ID_IDX = 3;

    // MOVE
    private static final int ROTATION_IDX = 11;

    // POWERUP
    private static final int POWER_UP_IDX = 11;

    @Override
    public GameMessage.Type getType(GameMessage gameMessage){
        return gameMessage.type;
    }

    @Override
    public int getObjectId(GameMessage gameMessage){
        return gameMessage.getShortAt(OBJECT_ID_IDX);
    }

    /*------------ WRITER ------------*/

    public void makeMessage(GameMessage gameMessage, GameMessage.Type type) {
        gameMessage.putByte(MESSAGE_TYPE_IDX, (byte)(type.ordinal()));
    }

    public void makeMessage(GameMessage gameMessage, GameMessage.Type type, int objectId) {
        makeMessage(gameMessage, type);
        gameMessage.putShort(OBJECT_ID_IDX, (short) objectId);
    }

    @Override
    public void makeInitClientMessage(GameMessage gameMessage, short skinId, byte attackId) {
        makeMessage(gameMessage, gameMessage.type.INIT_CLIENT);
    }

    @Override
    public void makeCreateMessage(GameMessage gameMessage, short objectId, float posX, float posY, short skinId) {

    }

    @Override
    public void makeDestroyMessage(GameMessage gameMessage, short objectId) {

    }

    @Override
    public void makeMoveServerMessage(GameMessage gameMessage, short objectId, float posX, float posY, float rotation) {

    }

    @Override
    public void makeMoveClientMessage(GameMessage gameMessage, short objectId, float posX, float posY) {

    }

    @Override
    public void makePowerUpMessage(GameMessage gameMessage, short objectId, float posX, float posY, byte powerupId) {

    }

    @Override
    public void makePowerUpAssign(GameMessage gameMessage, short objectId, byte powerupId) {

    }

    @Override
    public void makeAttackMessage(GameMessage gameMessage, short objectId) {

    }

    @Override
    public void makeEndMessage(GameMessage gameMessage, short winnerId) {

    }

}
