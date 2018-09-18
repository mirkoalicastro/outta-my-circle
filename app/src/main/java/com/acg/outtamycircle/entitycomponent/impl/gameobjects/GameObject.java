package com.acg.outtamycircle.entitycomponent.impl.gameobjects;

import com.acg.outtamycircle.entitycomponent.Entity;

public class GameObject extends Entity {
    private final int objectId;
    private final Type type;

    public enum Type {CHARACTER, POWERUP}

    public GameObject(int objectId, Type type){
        this.objectId = objectId;
        this.type = type;
    }

    public int getObjectId(){ return objectId; }

    public Type getType() {
        return type;
    }
}
