package com.acg.outtamycircle.network;

/**
 * Writer and Reader of game messages.
 */
public interface GameMessageInterpreter {

    /*------------ READER ------------*/

    //TODO
    GameMessage.Type getType(GameMessage gameMessage);

    int getObjectId(GameMessage gameMessage);



    /*------------ WRITER ------------*/

    short getInitClientSkinId(GameMessage message);

    byte getInitClientAttackId(GameMessage message);

    float getPosX(GameMessage message);

    float getPosY(GameMessage message);

    short getSkinId(GameMessage message);

    float getRotation(GameMessage message);

    byte getPowerUpId(GameMessage message);

    int getTimeMillis(GameMessage message);

    //TODO
    void makeInitClientMessage(GameMessage gameMessage, short skinId, byte attackId);

    void makeCreateMessage(GameMessage gameMessage, short objectId, float posX, float posY, short skinId);

    void makeDestroyMessage(GameMessage gameMessage, short objectId);

    void makeMoveServerMessage(GameMessage gameMessage, short objectId, float posX, float posY, float rotation);

    void makeMoveClientMessage(GameMessage gameMessage, short objectId, float posX, float posY);

    void makePowerUpMessage(GameMessage gameMessage, short objectId, float posX, float posY, byte powerupId);

    void makePowerUpAssign(GameMessage gameMessage, short objectId, byte powerupId);

    void makeAttackMessage(GameMessage gameMessage, short objectId);

    void makeEndMessage(GameMessage gameMessage, short winnerId);

    void makeHostOrClientMessage(GameMessage message, int time);

    void makeStartMessage(GameMessage message);
}
