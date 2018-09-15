package com.acg.outtamycircle.entitycomponent.impl.factories;

import android.util.Log;

import com.acg.outtamycircle.entitycomponent.AttackComponent;
import com.acg.outtamycircle.entitycomponent.impl.components.BoostAttackComponent;
import com.acg.outtamycircle.entitycomponent.impl.components.WeightAttackComponent;

public class AttackFactory {
    public static final int BOOST = 0;
    public static final int WEIGHT = 1;

    public AttackComponent makeAttackComponent(int id){
        switch(id){
            case BOOST: return new BoostAttackComponent();
            case WEIGHT: return new WeightAttackComponent();
            default:
                throw new IllegalArgumentException("Id = "+id);
        }
    }
}
