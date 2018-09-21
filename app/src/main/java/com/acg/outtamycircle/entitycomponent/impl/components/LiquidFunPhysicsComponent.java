package com.acg.outtamycircle.entitycomponent.impl.components;

import com.acg.outtamycircle.entitycomponent.PhysicsComponent;
import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.Vec2;
import com.google.fpl.liquidfun.World;

/**
 * Generalizzazione delle componenti fisiche di liquid fun
 */

public class LiquidFunPhysicsComponent extends PhysicsComponent{
    protected Body body;
    protected World world;
    private final Vec2 v = new Vec2();

    private float width, height;

    @Override
    public void applyForce(float x, float y){
        x /= 140; y /= 140;

        x -= body.getLinearVelocity().getX();
        y -= body.getLinearVelocity().getY();

        v.set(x, y);

        body.applyForceToCenter(v,true);
    }

    @Override
    public float getX(){
        return body.getPositionX();
    }

    @Override
    public float getY(){
        return body.getPositionY();
    }

    public LiquidFunPhysicsComponent setBody(Body body){
        this.body = body;
        return this;
    }

    public LiquidFunPhysicsComponent setWidth(float width){
        this.width = width;
        return this;
    }

    public LiquidFunPhysicsComponent setHeight(float height){
        this.height = height;
        return this;
    }

    @Override
    public float getWidth(){
        return width;
    }

    @Override
    public float getHeight(){
        return height;
    }

    @Override
    public void delete(){
        world.destroyBody(body);
    }

    public Body getBody() {
        return body;
    }

    public LiquidFunPhysicsComponent setWorld(World world) {
        this.world = world;
        return this;
    }

}