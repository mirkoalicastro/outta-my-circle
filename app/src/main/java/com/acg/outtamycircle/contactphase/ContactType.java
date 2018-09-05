package com.acg.outtamycircle.contactphase;

import com.acg.outtamycircle.entitycomponent.impl.GameObject;
import com.badlogic.androidgames.framework.Sound;


abstract class ContactType{

    public static int myHashCode(Object a, Object b){
        return a.getClass().hashCode() ^ b.getClass().hashCode();
    }

    public abstract void handle();
}
