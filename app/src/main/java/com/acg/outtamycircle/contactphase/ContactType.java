package com.acg.outtamycircle.contactphase;

abstract class ContactType{

    public static int myHashCode(Object a, Object b){
        return a.getClass().hashCode() ^ b.getClass().hashCode();
    }

    public abstract void handle();
}
