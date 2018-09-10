package com.acg.outtamycircle.entitycomponent.impl;

public class GameCharacter extends GameObject {
    private final short objectId;

    public GameCharacter(short objectId){
        this.objectId = objectId;
    }

    public short getObjectId(){ return objectId; }
}