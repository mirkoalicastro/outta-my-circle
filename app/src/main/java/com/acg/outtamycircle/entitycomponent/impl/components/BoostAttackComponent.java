package com.acg.outtamycircle.entitycomponent.impl.components;

import com.acg.outtamycircle.entitycomponent.AttackComponent;
import com.acg.outtamycircle.entitycomponent.Component;
import com.acg.outtamycircle.entitycomponent.PhysicsComponent;
import com.acg.outtamycircle.entitycomponent.impl.gameobjects.GameCharacter;

public final class BoostAttackComponent extends AttackComponent {
    private static final long DURATION = 25;
    private boolean active;
    private float x, y;
    private long firstCalled;
    private PhysicsComponent comp;
    private static float FORCE_MULTIPLIER = 7;

    @Override
    public void start(float x, float y){
        firstCalled = System.currentTimeMillis();
        this.x = x*FORCE_MULTIPLIER;
        this.y = y*FORCE_MULTIPLIER;
        active = true;
        comp = (PhysicsComponent) getOwner().getComponent(Type.Physics);
    }

    @Override
    public void attack() {
        comp.applyForce(x,y);

        if(System.currentTimeMillis()-firstCalled >= DURATION)
            active = false;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void stop(){}
}
