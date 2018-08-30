package com.acg.outtamycircle.entitycomponent.impl;

import com.acg.outtamycircle.entitycomponent.*;

/**
 * Created by mfaella on 27/02/16.
 */
public abstract class GameObject extends Entity{
    //protected String name;
    public enum TYPE {CHARACTER, POWERUP, WALL}

    //public int id;
    private TYPE type;

    public TYPE getType(){
        return type;
    }

}
