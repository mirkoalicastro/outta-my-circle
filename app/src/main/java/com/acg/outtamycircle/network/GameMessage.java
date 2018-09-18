package com.acg.outtamycircle.network;

import com.badlogic.androidgames.framework.Pool;

public class GameMessage {
    private String sender;
    private static int MAX_BUFFER_SIZE = 11;

    private static final int MAX_CAPACITY = 40;
    private static final int INITIAL_CAPACITY = 20;
    private static final Pool<GameMessage> pool = new Pool.SynchronizedPool<>(new Pool.PoolObjectFactory<GameMessage>() {
        @Override
        public GameMessage createObject() {
            return new GameMessage();
        }
    }, MAX_CAPACITY);

    byte buffer[];

    static {
        for(int i=0; i<INITIAL_CAPACITY; i++)
            pool.free(new GameMessage());
    }

    public static GameMessage createInstance() {
        return pool.newObject();
    }

    public static void deleteInstance(GameMessage gameMessage) {
        pool.free(gameMessage);
    }

    private GameMessage(){
        buffer = new byte[MAX_BUFFER_SIZE];
    }

    public Type getType(){
        return Type.values()[buffer[0]];
    }

    public void setSender(String sender){
        this.sender = sender;
    }

    public String getSender(){
        return sender;
    }
    /**
     * Puts the content of the message into a byte array from the specified position.
     * @param dest the array into which put the message.
     * @param start the position of the array at which the message must be put.
     */
    public void putInBuffer(byte dest[], int start){
        int n = getType().length;
        for(int i=0; i<n; i++)
            dest[start++] = buffer[i];
    }

    /**
     * Copy the content of a byte array into the GameMessage instance, from the specified position interval.
     * @param buffer the array from which copy the message.
     * @param start the starting position of the message.
     * @param end the ending position of the message.
     */
    public void copyBuffer(byte buffer[], int start, int end){
        int i = 0;
        for(; start<=end ; start++)
            this.buffer[i++] = buffer[start];
    }

    /**
     * The class of all message type.
     * Each type determines the length of the message in bytes.
     */
    public enum Type {
        //TODO type description
        INIT_CLIENT((byte)3),
        CREATE((byte)8),
        DESTROY((byte)4),
        MOVE_SERVER((byte)11),
        MOVE_CLIENT((byte)7),
        POWERUP((byte)8),
        POWERUP_ASSIGN((byte)6),
        ATTACK((byte)7),
        START((byte)1),
        COLLISION((byte)1),
        END((byte)3),
        HOST_OR_CLIENT((byte)5);

        final byte length;

        Type(byte length){ this.length = length; }
    }

    /**
     * Puts a byte value into the message buffer.
     * @param pos
     * @param value
     * @return the instance itself.
     */
    GameMessage putByte(int pos, byte value){
        buffer[pos] = value;
        return this;
    }

    /**
     * Puts a short value into the message buffer.
     * @param pos
     * @param value
     * @return the instance itself
     */
    GameMessage putShort(int pos, short value){
        buffer[pos] = (byte)(value>>>8);
        buffer[pos+1] = (byte)(value);
        return this;
    }

    /**
     * Puts a int value into the message buffer.
     * @param pos
     * @param value
     * @return the instance itself
     */
    GameMessage putInt(int pos, int value){
        buffer[pos++] = (byte) (value>>>24);
        buffer[pos++] = (byte) (value>>>16);
        buffer[pos++] = (byte) (value>>>8);
        buffer[pos] = (byte) value;
        return this;
    }

    /**
     * Puts a float value into the message buffer.
     * @param pos
     * @param value
     * @return the istance itself
     */
    GameMessage putFloat(int pos, float value){
        putInt(pos, Float.floatToRawIntBits(value));
        return this;
    }

    /**
     * Get the byte value at the specified position of the message buffer.
     * @param pos
     * @return
     */
    byte getByteAt(int pos){
        return buffer[pos];
    }

    /**
     * Gets the short value at the specified position of the message buffer.
     * @param pos
     */
    protected short getShortAt(int pos){
        int value = 0, tmp, i;

        for(i=0 ; i<2 ; i++){
            //Handle negative bytes
            tmp = (buffer[pos]&0x7F);
            if(buffer[pos++]<0) tmp = tmp ^ 0x00000080;

            value = (value<<8) + tmp;
        }

        return (short) value;
    }

    /**
     * Gets the int value at the specified position of the message buffer.
     * @param pos
     */
    int getIntAt(int pos){
        int value = 0, tmp, i;

        for(i=0; i<4 ; i++){
            //Handle negative bytes
            tmp = (buffer[pos]&0x7F);
            if(buffer[pos++]<0) tmp = tmp ^ 0x00000080;

            value = (value<<8) + tmp;
        }

        return value;
    }

    /**
     * Get the float value at the specified position.
     * @param pos
     * @return
     */
    float getFloatAt(int pos){
        int rawBits = getIntAt(pos);
        return Float.intBitsToFloat(rawBits);
    }

}
