package com.acg.outtamycircle.entitycomponent.impl;

import android.util.Log;

import com.google.fpl.liquidfun.BodyDef;
import com.google.fpl.liquidfun.BodyType;
import com.google.fpl.liquidfun.CircleShape;
import com.google.fpl.liquidfun.Fixture;
import com.google.fpl.liquidfun.FixtureDef;
import com.google.fpl.liquidfun.PolygonShape;
import com.google.fpl.liquidfun.Shape;
import com.google.fpl.liquidfun.World;

public class DynamicCircle extends LiquidFunPhysicsComponent {
    private float radius;
    //volatile utile all'accesso concorrente di drawCharacters e thread di controllo

    public DynamicCircle(World world, float radius, BodyDef bodyDef, FixtureDef fixtureDef) {
        this.radius = radius;

        body = world.createBody(bodyDef);
        body.setSleepingAllowed(false);
        body.setUserData(owner);

        CircleShape shape = new CircleShape();
        shape.setRadius(radius);
        //shape.setPosition(x, y);

        body.createFixture(fixtureDef);

        body.setBullet(true);
    }

    public float getRadius(){ return radius; }
}