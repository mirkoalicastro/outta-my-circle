package com.acg.outtamycircle.entitycomponent.impl;

import com.acg.outtamycircle.entitycomponent.PhysicsComponent;
import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.Vec2;

/**
 * Generalizzazione delle componenti fisiche di liquid fun
 */
public class LiquidFunPhysicsComponent extends PhysicsComponent{
    protected Body body;

    public void move(float x, float y){
        body.setLinearVelocity(new Vec2(x, y));
    }
}