package com.acg.outtamycircle.network;

public class GameMessage {
    //TODO sender??
    public Type type;
    private static int MAX_BUFFER_SIZE = 40;

    byte buffer[];

    public GameMessage(){
        buffer = new byte[MAX_BUFFER_SIZE];
    }

    public void putInBuffer(byte dest[], int start){
        int n = type.length;
        for(int i=0 ; i<n ; i++)
            dest[start++] = buffer[i];
    }

    public void copyBuffer(byte buffer[], int start, int end){
        for( ; start<=end ; start++){
            this.buffer[start] = buffer[start];
        }
    }

    public enum Type {
        //TODO message length
        PLAYER((byte)0),
        DESTROY((byte)0),
        MOVE((byte)0),
        POWERUP((byte)0),
        POWERUP_ASSIGN((byte)0),
        ATTACK((byte)0),
        END((byte)0);

        final byte length;

        Type(byte length){
            this.length = length;
        }
    }


    /* STATIC FACTORY-LIKE METHOD */
    // No allocation
    // Object recycle

    //TODO valutare dove spostare

    /**
     * Puts a short value into the message buffer.
     * @param pos
     * @param value
     */
    private void putShort(int pos, short value){
        buffer[pos] = (byte)(value>>>8);
        buffer[pos+1] = (byte)(value);
    }

    /**
     * Puts a int value into the message buffer.
     * @param pos
     * @param value
     */
    private void putInt(int pos, int value){
        buffer[pos++] = (byte) (value>>>24);
        buffer[pos++] = (byte) (value>>>16);
        buffer[pos++] = (byte) (value>>>8);
        buffer[pos] = (byte) value;
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
     * Gets the short value at the specified position of the message buffer.
     * @param pos
     */
    protected int getIntAt(int pos){
        int value = 0, tmp, i;

        for(i=0; i<4 ; i++){
            //Handle negative bytes
            tmp = (buffer[pos]&0x7F);
            if(buffer[pos++]<0) tmp = tmp ^ 0x00000080;

            value = (value<<8) + tmp;
        }

        return value;
    }

    //TODO
    static void makeMessage(GameMessage gameMessage, int gameObject, Type type){
        byte buffer[] = gameMessage.buffer;
        buffer[0] = (byte)type.ordinal();
    }

    //TODO
    //public static void makeCreateMessage(GameMessage gameMessage);

    //TODO
    //public static void makeDestroyMessage(GameMessage gameMessage);

    //TODO
    //public static void makeMoveMessage(GameMessage gameMessage);

    //TODO
    //public static void makePowerUpMessage(GameMessage gameMessage);

}
