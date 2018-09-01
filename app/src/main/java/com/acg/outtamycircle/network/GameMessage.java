package com.acg.outtamycircle.network;

public class GameMessage {
    //TODO sender??
    public Type type;
    private static int MAX_BUFFER_SIZE = 40;

    byte buffer[];

    public GameMessage(){
        buffer = new byte[MAX_BUFFER_SIZE];
    }

    /**
     * Puts the content of the message into a byte array from the specified position.
     * @param dest the array into which put the message.
     * @param start the position of the array at which the message must be put.
     */
    public void putInBuffer(byte dest[], int start){
        int n = type.length;
        for(int i=0 ; i<n ; i++)
            dest[start++] = buffer[i];
    }

    /**
     * Copy the content of a byte array into the GameMessage instance, from the specified position interval.
     * @param buffer the array from which copy the message.
     * @param start the starting position of the message.
     * @param end the ending position of the message.
     */
    public void copyBuffer(byte buffer[], int start, int end){
        for( ; start<=end ; start++){
            this.buffer[start] = buffer[start];
        }
    }

    /**
     * The class of all message type.
     * Each type determines the length of the message in bytes.
     */
    public enum Type {
        //TODO message length and type description
        INIT_CLIENT((byte)4),
        CREATE((byte)13),
        DESTROY((byte)3),
        MOVE_SERVER((byte)15),
        MOVE_CLIENT((byte)11),
        POWERUP((byte)12),
        POWERUP_ASSIGN((byte)0), //TODO
        ATTACK((byte)3),
        END((byte)3);

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
