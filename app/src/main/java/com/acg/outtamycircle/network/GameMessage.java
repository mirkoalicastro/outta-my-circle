package com.acg.outtamycircle.network;

import com.acg.outtamycircle.network.googleimpl.ClientMessageReceiver;
import com.acg.outtamycircle.network.googleimpl.ServerMessageReceiver;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessage;

import java.util.Arrays;

public class GameMessage {
    //TODO sender??
    private String sender;
    private Type type; //TODO delete?
    private static int MAX_BUFFER_SIZE = 40;

    byte buffer[];

    public GameMessage(){
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
        int i = 0;
        for( ; start<=end ; start++){
            this.buffer[i++] = buffer[start];
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
        END((byte)3),
        HOST_OR_CLIENT((byte)0); //TODO length

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

    //TODO test case
    public static void main(String args[]){
        GameMessage m1 = new GameMessage(); // move server
        GameMessage m2 = new GameMessage(); // move client
        GameMessage m3 = new GameMessage(); //

        GameMessageInterpreterImpl interpreter = new GameMessageInterpreterImpl();

        final ClientMessageReceiver clientReceiver = new ClientMessageReceiver();
        ClientMessageReceiver clientReceiver2 = new ClientMessageReceiver();
        final ServerMessageReceiver serverReceiver = new ServerMessageReceiver(interpreter,4);
        ServerMessageReceiver serverReceiver2 = new ServerMessageReceiver(interpreter,4);


        interpreter.makeMoveClientMessage(m1, (short)1, 3.1f, 2.8f);
        interpreter.makeAttackMessage(m2, (short) 3);

        ServerClientMessageHandler handler = new ServerClientMessageHandler(){
            @Override
            public void onRealTimeMessageReceived(RealTimeMessage message){
                byte[] messageData = buffer;
                int cursor = 0;

                while( cursor < messageData.length && messageData[cursor]!=127) {
                    GameMessage gameMessage = new GameMessage();
                    int length = GameMessage.Type.values()[messageData[cursor]].length;
                    gameMessage.copyBuffer(messageData, cursor, cursor + length - 1); //TODO check
                    serverReceiver.storeMessage(gameMessage);
                    cursor += length;
                }
            }
        };

        handler.setReceivers(serverReceiver, serverReceiver2);

        handler.putInBuffer(m1);
        handler.putInBuffer(m2);

        System.out.println(Arrays.toString(handler.buffer));

        handler.onRealTimeMessageReceived(null);

        for(GameMessage mx : handler.getMessages()){
            System.out.println("CIAO");
            System.out.println(interpreter.toString(mx));
        }

        //clientReceiver.storeMessage(m1);
        //clientReceiver.storeMessage(m2);


    }
}
