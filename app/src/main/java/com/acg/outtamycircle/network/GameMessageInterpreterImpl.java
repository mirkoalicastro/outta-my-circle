package com.acg.outtamycircle.network;

public class GameMessageInterpreterImpl implements GameMessageInterpreter {
    // SHARED
    private static final int MESSAGE_TYPE_IDX = 0;
    private static final int OBJECT_ID_IDX = 1;
    private static final int POS_X_IDX = 3;
    private static final int POS_Y_IDX = 5;

    // DESTROY
    private static final int ROUND_IDX = 3;

    // INIT
    private static final int INIT_SKIN_ID_IDX = 1;
    private static final int ATTACK_ID_IDX = 2;

    // CREATE
    private static final int CREATE_SKIN_ID_IDX = 7;

    // MOVE
    private static final int ROTATION_IDX = 7;

    // POWERUP
    private static final int POWERUP_TYPE_IDX = 7;

    // POWERUP_ASSIGN
    private static final int ASSIGN_POWERUP_ID_IDX = 3;
    private static final int ASSIGN_POWERUP_TYPE_IDX = 5;

    // HOST_OR_CLIENT
    private static final int HOST_OR_CLIENT_IDX = 1;

    @Override
    public GameMessage.Type getType(GameMessage message){
        return message.getType();
    }

    @Override
    public int getObjectId(GameMessage message){
        return message.getShortAt(OBJECT_ID_IDX);
    }

    @Override
    public int getInitClientSkinId(GameMessage message){
        return message.getByteAt(INIT_SKIN_ID_IDX);
    }

    @Override
    public int getInitClientAttackId(GameMessage message){
        return message.getByteAt(ATTACK_ID_IDX);
    }

    @Override
    public int getRound(GameMessage message) {
        return message.getByteAt(ROUND_IDX);
    }

    @Override
    public int getPosX(GameMessage message){
        return message.getShortAt(POS_X_IDX);
    }

    @Override
    public int getPosY(GameMessage message){
        return message.getShortAt(POS_Y_IDX);
    }

    @Override
    public int getSkinId(GameMessage message){
        return message.getByteAt(CREATE_SKIN_ID_IDX);
    }

    @Override
    public float getRotation(GameMessage message){
        return message.getFloatAt(ROTATION_IDX);
    }

    @Override
    public int getPowerupId(GameMessage message){
        if(getType(message)== GameMessage.Type.POWERUP)
            return getObjectId(message);
        else
            return message.getShortAt(ASSIGN_POWERUP_ID_IDX);

    }

    @Override
    public int getPowerupType(GameMessage message){
        if(getType(message)==GameMessage.Type.POWERUP)
            return message.getByteAt(POWERUP_TYPE_IDX);
        else
            return message.getByteAt(ASSIGN_POWERUP_TYPE_IDX);
    }

    @Override
    public int getTimeMillis(GameMessage message){
        return message.getIntAt(HOST_OR_CLIENT_IDX);
    }

    /*------------ WRITER ------------*/

    private void makeMessage(GameMessage message, GameMessage.Type type) {
        message.putByte(MESSAGE_TYPE_IDX, (byte)(type.ordinal()));
    }


    private void makeMessage(GameMessage message, GameMessage.Type type, int objectId) {
        makeMessage(message, type);
        message.putShort(OBJECT_ID_IDX, (short) objectId);
    }

    @Override
    public void makeInitClientMessage(GameMessage message, int skinId, int attackId) {
        makeMessage(message, GameMessage.Type.INIT_CLIENT);
        message.putByte(INIT_SKIN_ID_IDX, (byte) skinId).putByte(ATTACK_ID_IDX, (byte) attackId);
    }

    @Override
    public void makeCreateMessage(GameMessage message, int objectId, int posX, int posY, int skinId) {
        makeMessage(message, GameMessage.Type.CREATE, objectId);
        message.putShort(POS_X_IDX, (short) posX).putShort(POS_Y_IDX, (short) posY).putByte(CREATE_SKIN_ID_IDX, (byte) skinId);
    }

    @Override
    public void makeDestroyMessage(GameMessage message, int objectId, int round) {
        makeMessage(message, GameMessage.Type.DESTROY, objectId);
        message.putByte(POS_X_IDX, (byte) round);
    }

    @Override
    public void makeMoveServerMessage(GameMessage message, int objectId, int posX, int posY, float rotation) {
        makeMessage(message, GameMessage.Type.MOVE_SERVER, objectId);
        message.putShort(POS_X_IDX, (short) posX).putShort(POS_Y_IDX, (short) posY).putFloat(ROTATION_IDX, rotation);
    }

    @Override
    public void makeMoveClientMessage(GameMessage message, int objectId, int posX, int posY) {
        makeMessage(message, GameMessage.Type.MOVE_CLIENT, objectId);
        message.putShort(POS_X_IDX, (short) posX).putShort(POS_Y_IDX, (short) posY);
    }

    @Override
    public void makePowerupMessage(GameMessage message, int objectId, int posX, int posY, int powerupType) {
        makeMessage(message, GameMessage.Type.POWERUP, objectId);
        message.putShort(POS_X_IDX, (short) posX).putShort(POS_Y_IDX, (short) posY).putByte(POWERUP_TYPE_IDX, (byte) powerupType);
    }

    @Override
    public void makePowerupAssign(GameMessage message, int objectId, int powerupId, int powerupType) {
        makeMessage(message, GameMessage.Type.POWERUP_ASSIGN, objectId);
        message.putShort(ASSIGN_POWERUP_ID_IDX, (short) powerupId).putByte(ASSIGN_POWERUP_TYPE_IDX, (byte) powerupType);
    }

    @Override
    public void makeAttackMessage(GameMessage message, int objectId, int xDirection, int yDirection) {
        makeMessage(message, GameMessage.Type.ATTACK, objectId);
        message.putShort(POS_X_IDX,(short) xDirection).putShort(POS_Y_IDX, (short) yDirection);
    }

    @Override
    public void makeEndMessage(GameMessage message, int winnerId) {
        makeMessage(message, GameMessage.Type.END, winnerId);
    }

    @Override
    public void makeHostOrClientMessage(GameMessage message, int time){
        makeMessage(message, GameMessage.Type.HOST_OR_CLIENT);
        message.putInt(HOST_OR_CLIENT_IDX, time);
    }

    @Override
    public void makeStartMessage(GameMessage message){
        makeMessage(message, GameMessage.Type.START);
    }

    @Override
    public void makeCollisionMessage(GameMessage message) {
        makeMessage(message, GameMessage.Type.COLLISION);
    }
}
