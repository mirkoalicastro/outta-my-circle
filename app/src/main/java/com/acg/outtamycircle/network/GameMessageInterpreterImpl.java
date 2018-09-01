package com.acg.outtamycircle.network;

public class GameMessageInterpreterImpl implements GameMessageInterpreter{
    // SHARED
    private static final int MESSAGE_TYPE_IDX = 0;
    private static final int OBJECT_ID_IDX = 1;
    private static final int POS_X_IDX = 3;
    private static final int POS_Y_IDX = 7;

    // INIT
    private static final int INIT_SKIN_ID_IDX = 1;
    private static final int ATTACK_ID_IDX = 3;

    // CREATE
    private static final int CREATE_SKIN_ID_IDX = 11;

    // MOVE
    private static final int ROTATION_IDX = 11;

    // POWERUP
    private static final int POWER_UP_IDX = 11;

    @Override
    public GameMessage.Type getType(GameMessage message){
        return message.type;
    }

    @Override
    public int getObjectId(GameMessage message){
        return message.getShortAt(OBJECT_ID_IDX);
    }
    
    public short getInitClientSkinId(GameMessage message){
        return message.getShortAt(INIT_SKIN_ID_IDX);
    }

    public byte getInitClientAttackId(GameMessage message){
        return message.getByteAt(ATTACK_ID_IDX);
    }

    public float getPosX(GameMessage message){
        return message.getFloatAt(POS_X_IDX);
    }

    public float getPosY(GameMessage message){
        return message.getFloatAt(POS_Y_IDX);
    }

    public short getSkinId(GameMessage message){
        return message.getShortAt(CREATE_SKIN_ID_IDX);
    }

    public float getRotation(GameMessage message){
        return message.getFloatAt(ROTATION_IDX);
    }

    public byte getPowerUpId(GameMessage message){
        return message.getByteAt(POWER_UP_IDX);
    }

    /*------------ WRITER ------------*/

    public void makeMessage(GameMessage message, GameMessage.Type type) {
        message.putByte(MESSAGE_TYPE_IDX, (byte)(type.ordinal()));
    }

    public void makeMessage(GameMessage message, GameMessage.Type type, int objectId) {
        makeMessage(message, type);
        message.putShort(OBJECT_ID_IDX, (short) objectId);
    }

    @Override
    public void makeInitClientMessage(GameMessage message, short skinId, byte attackId) {
        makeMessage(message, message.type.INIT_CLIENT);
        message.putShort(INIT_SKIN_ID_IDX, skinId).putByte(ATTACK_ID_IDX, attackId);
    }

    @Override
    public void makeCreateMessage(GameMessage message, short objectId, float posX, float posY, short skinId) {
        makeMessage(message, GameMessage.Type.CREATE, objectId);
        message.putFloat(POS_X_IDX, posX).putFloat(POS_Y_IDX, posY).putShort(CREATE_SKIN_ID_IDX, skinId);
    }

    @Override
    public void makeDestroyMessage(GameMessage message, short objectId) {
        makeMessage(message, GameMessage.Type.DESTROY, objectId);
    }

    @Override
    public void makeMoveServerMessage(GameMessage message, short objectId, float posX, float posY, float rotation) {
        makeMessage(message, GameMessage.Type.MOVE_SERVER, objectId);
        message.putFloat(POS_X_IDX, posX).putFloat(POS_Y_IDX, posY).putFloat(ROTATION_IDX, rotation);
    }

    @Override
    public void makeMoveClientMessage(GameMessage message, short objectId, float posX, float posY) {
        makeMessage(message, GameMessage.Type.MOVE_CLIENT, objectId);
        message.putFloat(POS_X_IDX, posX).putFloat(POS_Y_IDX, posY);
    }

    @Override
    public void makePowerUpMessage(GameMessage message, short objectId, float posX, float posY, byte powerupId) {
        makeMessage(message, GameMessage.Type.POWERUP, objectId);
        message.putFloat(POS_X_IDX, posX).putFloat(POS_Y_IDX, posY).putByte(POWER_UP_IDX, powerupId);
    }

    @Override
    public void makePowerUpAssign(GameMessage message, short objectId, byte powerupId) {
        makeMessage(message, GameMessage.Type.POWERUP_ASSIGN, objectId);
        //TODO CONTROLLAAAA
    }

    @Override
    public void makeAttackMessage(GameMessage message, short objectId) {
        makeMessage(message, GameMessage.Type.ATTACK, objectId);
    }

    @Override
    public void makeEndMessage(GameMessage message, short winnerId) {
        makeMessage(message, GameMessage.Type.END, winnerId);
    }

}
