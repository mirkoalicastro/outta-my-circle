package com.acg.outtamycircle.network;

public class GameMessageInterpreterImpl implements GameMessageInterpreter{

     /**
     * Gets the short value at the specified position of the message buffer.
     * @param gameMessage
     * @param pos
     */
    protected short getShortAt(GameMessage gameMessage, int pos){
        byte buffer[] = gameMessage.buffer;
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
     * @param gameMessage
     * @param pos
     */
    protected int getIntAt(GameMessage gameMessage, int pos){
        byte buffer[] = gameMessage.buffer;
        int value = 0, tmp, i;

        for(i=0; i<4 ; i++){
            //Handle negative bytes
            tmp = (buffer[pos]&0x7F);
            if(buffer[pos++]<0) tmp = tmp ^ 0x00000080;

            value = (value<<8) + tmp;
        }

        return value;
    }

    @Override
    public GameMessage.Type getType(GameMessage gameMessage){
        return GameMessage.Type.fromByte(gameMessage.buffer[0]);
    }

    @Override
    public int getObjectId(GameMessage gameMessage){
        //TODO implement
        return 1;
    }
}
