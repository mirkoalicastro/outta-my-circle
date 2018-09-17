package com.acg.outtamycircle.utilities;

public class IdGenerator {
    private static IdGenerator instance = null;

    private IdGenerator(short id) {
        this.id = id;
    }

    public static IdGenerator getInstance(short startId){
        if(instance == null)
            instance = new IdGenerator(startId);
        return instance;
    }

    private short id;
    public short next(){
        return ++id;
    }
}