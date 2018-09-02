package com.acg.outtamycircle.contactphase;

import com.acg.outtamycircle.entitycomponent.impl.GameObject;
import com.badlogic.androidgames.framework.Sound;


abstract class ContactType{

    public static int myHashCode(Class<? extends GameObject> a, Class<? extends GameObject> b){
        return a.hashCode() ^ b.hashCode();
    }

    public abstract void handle();
}
