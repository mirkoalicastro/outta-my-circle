package com.acg.outtamycircle.entitycomponent.impl;

import com.acg.outtamycircle.entitycomponent.PhysicsComponent;
import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.Vec2;

/**
 * Generalizzazione delle componenti fisiche di liquid fun
 */
public class LiquidFunPhysicsComponent extends PhysicsComponent{
    protected Body body;
    private final Vec2 v = new Vec2();

    private float width, height;

    public void applyForce(float x, float y){
        x *= 15; y *= 15;

        //Simulazione attrito con l'arena
        x -= body.getLinearVelocity().getX();
        y -= body.getLinearVelocity().getY();

        v.set(x, y);

        body.applyForceToCenter(v,true);
    }

    public float getX(){
        return body.getPositionX();
    }

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

    public float getWidth(){ return width; }
    public float getHeight(){ return height; }

    public void deleteBody(){
        body.getWorld().destroyBody(body);
        //body.delete(); //TODO
    }
}