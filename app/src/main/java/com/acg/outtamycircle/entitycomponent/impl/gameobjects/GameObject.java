package com.acg.outtamycircle.entitycomponent.impl.gameobjects;

import com.acg.outtamycircle.entitycomponent.Entity;

public class GameObject extends Entity {
    private final short objectId;

    public GameObject(short objectId){
        this.objectId = objectId;
    }

    public short getObjectId(){ return objectId; }
}
