package com.acg.outtamycircle.entitycomponent.impl.components;

import com.acg.outtamycircle.entitycomponent.AttackComponent;
import com.acg.outtamycircle.entitycomponent.Component;
import com.acg.outtamycircle.entitycomponent.PhysicsComponent;
import com.acg.outtamycircle.entitycomponent.impl.gameobjects.GameCharacter;

public final class BoostAttackComponent extends AttackComponent {
    private static final long DURATION = 500;
    private boolean active;
    private float x, y;
    private long firstCalled;
    private PhysicsComponent comp;

    @Override
    public void start(float x, float y){
        firstCalled = System.currentTimeMillis();
        this.x = x/150;
        this.y = y/150;
        active = true;
        comp = (PhysicsComponent) getOwner().getComponent(Type.Physics);
    }

    @Override
    public void attack() {
        comp.applyForce(x,x); //TODO che force??

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
