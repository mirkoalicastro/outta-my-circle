package com.acg.outtamycircle.entitycomponent.impl;

public abstract class PowerUp extends GameObject {
    //protected Character character = null;

    public abstract void modify();
    public abstract void restore();
}