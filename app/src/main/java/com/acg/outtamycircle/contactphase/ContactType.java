package com.acg.outtamycircle.contactphase;

import com.acg.outtamycircle.entitycomponent.impl.gameobjects.GameObject;

import java.util.Objects;

abstract class ContactType{

    public static int myHashCode(Object a, Object b){
        //TODO Objects.hash(a,b)
        return a.hashCode() ^ b.hashCode();
    }

    public abstract void handle(GameObject a, GameObject b);
}
