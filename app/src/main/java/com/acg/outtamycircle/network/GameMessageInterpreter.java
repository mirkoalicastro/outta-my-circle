package com.acg.outtamycircle.network;

/**
 * Writer and Reader of game messages.
 */
public interface GameMessageInterpreter {

    /*------------ READER ------------*/

    /**
     * Returns the type of message.
     * @param gameMessage
     * @return
     */
    GameMessage.Type getType(GameMessage gameMessage);

    /**
     * Returns the id of the referred object in the message.
     * @param gameMessage
     * @return
     */
    int getObjectId(GameMessage gameMessage);

    int getInitClientSkinId(GameMessage message);

    int getInitClientAttackId(GameMessage message);

    int getPosX(GameMessage message);

    int getPosY(GameMessage message);

    int getSkinId(GameMessage message);

    float getRotation(GameMessage message);

    int getPowerupId(GameMessage message);

    int getPowerupType(GameMessage message);

    int getTimeMillis(GameMessage message);

    int getRound(GameMessage message);

        /*------------ WRITER ------------*/

    void makeInitClientMessage(GameMessage gameMessage, int skinId, int attackId);

    void makeCreateMessage(GameMessage gameMessage, int objectId, int posX, int posY, int skinId);

    void makeDestroyMessage(GameMessage gameMessage, int objectId, int round);

    void makeMoveServerMessage(GameMessage gameMessage, int objectId, int posX, int posY, float rotation);

    void makeMoveClientMessage(GameMessage gameMessage, int objectId, int posX, int posY);

    void makePowerupMessage(GameMessage gameMessage, int objectId, int posX, int posY, int powerupType);

    void makePowerupAssign(GameMessage gameMessage, int objectId, int powerupId, int powerupType);

    void makeAttackMessage(GameMessage gameMessage, int objectId, int xDirection, int yDirection);

    void makeEndMessage(GameMessage gameMessage, int winnerId);

    void makeHostOrClientMessage(GameMessage message, int time);

    void makeStartMessage(GameMessage message);

    void makeCollisionMessage(GameMessage message);
}
