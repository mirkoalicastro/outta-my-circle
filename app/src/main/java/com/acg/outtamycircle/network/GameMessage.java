package com.acg.outtamycircle.network;

public class GameMessage {
    public GameMessageType type;
    private static int MAX_BUFFER_SIZE = 40;
    int gameObject;

    byte buffer[];

    public GameMessage(){
        buffer = new byte[MAX_BUFFER_SIZE];
    }

    public void putInBuffer(byte dest[]){
        int n = GameMessageType.lengthInBytes(type);
        for(int i=0 ; i<n ; i++)
            dest[i] = buffer[i];
    }


    public enum GameMessageType {
        //TODO draft
        CREATE, DESTROY, MOVE, POWERUP, POWERUP_ASSING, END;

        byte toByte(){
            switch (this){
                case CREATE: return 'C';
                case DESTROY: return 'D';
                case MOVE: return 'M';
                case POWERUP: return 'P';
                case POWERUP_ASSING: return 'A';
                case END: return 'E';
                default: return 0;
            }
        }


        static GameMessageType fromByte(byte c){
            switch(c){
                case 'C': return CREATE;
                case 'D': return DESTROY;
                case 'M': return MOVE;
                case 'P': return POWERUP;
                case 'A': return POWERUP_ASSING;
                case 'E': return END;
                default: return null;
            }
        }

        public static int lengthInBytes(GameMessageType type){
            //TODO
            switch(type){
                case CREATE:
                    return 0;
                case DESTROY:
                    return 0;
                case MOVE:
                    return 0;
                case POWERUP:
                    return 0;
                case POWERUP_ASSING:
                    return 0;
                case END:
                    return 0;
                default:
                    return -1;
            }
        }

    }


    /* STATIC FACTORY-LIKE METHOD */
    // No allocation
    // Object recycle
    //TODO
    //public static void makeCreateMessage(GameMessage gameMessage);

    //TODO
    //public static void makeDestroyMessage(GameMessage gameMessage);

    //TODO
    //public static void makeMoveMessage(GameMessage gameMessage);

    //TODO
    //public static void makePowerUpMessage(GameMessage gameMessage);

}
