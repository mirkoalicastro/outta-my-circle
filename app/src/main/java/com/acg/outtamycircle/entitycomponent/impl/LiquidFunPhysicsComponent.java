package com.acg.outtamycircle.entitycomponent.impl;

import android.util.Log;

import com.acg.outtamycircle.entitycomponent.PhysicsComponent;
import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.Vec2;

/**
 * Generalizzazione delle componenti fisiche di liquid fun
 */
public class LiquidFunPhysicsComponent extends PhysicsComponent{
    protected Body body;
    private final int[] pos = new int[2];
    private final Vec2 v = new Vec2();


    public void move(float x, float y){

       /* v.set(x * 500, -y * 50);

        Log.d("VEC2", String.valueOf(x)+"--"+y);


        //controlla che non raggiunga il limite
        //body.getLinearVelocity

        //body.applyLinearImpulse(v, body.getLocalCenter(),true);
        body.applyForceToCenter(v, true);*/

        x *= 100000;
        y *= -100000;
        x -= body.getLinearVelocity().getX();
        y -= body.getLinearVelocity().getY();
        float mass = body.getMass();

        Log.d("VEC2", String.valueOf(x)+"--"+y);

        //v.set(mass*dx, mass*dy);
        v.set(x, y);

        //body.applyLinearImpulse(v, body.getLocalCenter(), true);

        body.applyForceToCenter(v, true);
    }

    public int[] getPosition(){
        pos[0] = (int)body.getPositionX();
        pos[1] = (int)body.getPositionY();
        return pos;
    }
}