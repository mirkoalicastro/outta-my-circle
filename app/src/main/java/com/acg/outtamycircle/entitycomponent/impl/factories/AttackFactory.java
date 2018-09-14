package com.acg.outtamycircle.entitycomponent.impl.factories;

import android.util.Log;

import com.acg.outtamycircle.entitycomponent.AttackComponent;
import com.acg.outtamycircle.entitycomponent.impl.components.BoostAttackComponent;

public class AttackFactory {
    public static final int BOOST = 0;
    public static final int WEIGHT = 1;

    public AttackComponent makeAttackComponent(int id){
        AttackComponent ret = null;
        switch(id){
            case BOOST: return new BoostAttackComponent();
            case WEIGHT:
                //TODO
                break;
            default:
                throw new IllegalArgumentException("Id = "+id);
        }
        return ret;
    }
}
