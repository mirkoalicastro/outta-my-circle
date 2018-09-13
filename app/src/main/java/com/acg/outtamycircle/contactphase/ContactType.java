package com.acg.outtamycircle.contactphase;

import com.acg.outtamycircle.entitycomponent.impl.gameobjects.GameObject;

abstract class ContactType{

    public static int myHashCode(Object a, Object b){
        return a.hashCode() ^ b.hashCode();
    }

    public abstract void handle(GameObject a, GameObject b);
}
