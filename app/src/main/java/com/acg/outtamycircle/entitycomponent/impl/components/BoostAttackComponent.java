package com.acg.outtamycircle.entitycomponent.impl.components;

import com.acg.outtamycircle.GameStatus;
import com.acg.outtamycircle.entitycomponent.AttackComponent;
import com.acg.outtamycircle.entitycomponent.Entity;
import com.acg.outtamycircle.entitycomponent.PhysicsComponent;

public final class BoostAttackComponent extends AttackComponent {
    private static final long DURATION = 20;
    private boolean active;
    private float x, y;
    private long firstCalled;
    private PhysicsComponent comp;
    private static float FORCE_MULTIPLIER = 9f;

    @Override
    public void start(GameStatus status, float x, float y){
        firstCalled = System.currentTimeMillis();
        this.x = x*FORCE_MULTIPLIER;
        this.y = y*FORCE_MULTIPLIER;
        active = true;
    }

    @Override
    public BoostAttackComponent setOwner(Entity owner) {
        super.setOwner(owner);
        comp = (PhysicsComponent) getOwner().getComponent(Type.Physics);
        return this;
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
