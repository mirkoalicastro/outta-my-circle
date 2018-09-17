package com.acg.outtamycircle.contactphase;

import com.acg.outtamycircle.entitycomponent.impl.gameobjects.GameObject;

public interface ContactType{
    void handle(GameObject a, GameObject b);
}
