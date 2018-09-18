package com.acg.outtamycircle.contactphase;

import com.acg.outtamycircle.GameStatus;
import com.acg.outtamycircle.entitycomponent.impl.gameobjects.GameObject;

public abstract class ContactType{
    protected GameStatus status;
    public abstract void handle(GameObject a, GameObject b);

    public void setStatus(GameStatus status) {
        this.status = status;
    }
}
