package com.acg.outtamycircle.entitycomponent.impl.components;

import com.acg.outtamycircle.entitycomponent.PhysicsComponent;
import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.Vec2;

/**
 * Generalizzazione delle componenti fisiche di liquid fun
 */

//TODO generalizzare i metodi e rendere la classe interna al factory
public class LiquidFunPhysicsComponent extends PhysicsComponent{
    protected Body body;
    private final Vec2 v = new Vec2();

    private float width, height;

    @Override
    public void applyForce(float x, float y){
        x /= 150; y /= 150;

        //Simulazione attrito con l'arena
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

    public float getWidth(){ return width; }
    public float getHeight(){ return height; }

    public void deleteBody(){
        body.getWorld().destroyBody(body);
    }
}