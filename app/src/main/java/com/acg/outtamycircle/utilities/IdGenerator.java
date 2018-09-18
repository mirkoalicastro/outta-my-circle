package com.acg.outtamycircle.utilities;

public class IdGenerator {
    private static IdGenerator instance = null;

    private IdGenerator(int id) {
        this.id = id;
    }

    public static IdGenerator getInstance(int startId){
        if(instance == null)
            instance = new IdGenerator(startId);
        return instance;
    }

    private int id;
    public int next(){
        return ++id;
    }
}