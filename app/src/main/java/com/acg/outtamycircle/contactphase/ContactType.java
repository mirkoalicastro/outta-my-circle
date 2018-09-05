package com.acg.outtamycircle.contactphase;

import android.util.Log;

import java.util.Objects;

abstract class ContactType{

    public static int myHashCode(Object a, Object b){
        Log.d("PROVAAAA", (a == null ? "null" : "") + "," + (b == null ? "null2" : "") + "<-");
        return a.hashCode() ^ b.hashCode();
    }

    public abstract void handle();
}
