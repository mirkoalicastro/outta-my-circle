package com.acg.outtamycircle.entitycomponent.impl.factories;

import com.acg.outtamycircle.entitycomponent.AttackComponent;
import com.acg.outtamycircle.entitycomponent.impl.components.BoostAttackComponent;
import com.acg.outtamycircle.entitycomponent.impl.components.RadialAttackComponent;
import com.acg.outtamycircle.entitycomponent.impl.components.WeightAttackComponent;

public class AttackComponentFactory {
    public static final int BOOST = 0;
    public static final int RADIAL = 1;
    public static final int WEIGHT = 2;

    public AttackComponent makeAttackComponent(int id){
        switch(id){
            case BOOST: return new BoostAttackComponent();
            case RADIAL: return new RadialAttackComponent();
            case WEIGHT: return new WeightAttackComponent();
            default:
                throw new IllegalArgumentException("Id = "+id);
        }
    }
}
