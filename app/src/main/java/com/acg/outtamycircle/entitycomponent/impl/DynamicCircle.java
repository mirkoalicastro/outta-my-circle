package com.acg.outtamycircle.entitycomponent.impl;

import com.google.fpl.liquidfun.BodyDef;
import com.google.fpl.liquidfun.BodyType;
import com.google.fpl.liquidfun.CircleShape;
import com.google.fpl.liquidfun.FixtureDef;
import com.google.fpl.liquidfun.PolygonShape;
import com.google.fpl.liquidfun.World;

public class DynamicCircle extends LiquidFunPhysicsComponent{
    public float radius;
    public DynamicCircle(World world, float radius, float x, float y) {
        this.radius = radius;

        BodyDef bodyDef = new BodyDef();
        bodyDef.setPosition(x, y);
        bodyDef.setType(BodyType.dynamicBody);

        body = world.createBody(bodyDef);
        body.setSleepingAllowed(false);
        body.setUserData(owner);

        CircleShape shape = new CircleShape();
        shape.setRadius(radius);
 //       shape.setPosition(x, y);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.setShape(shape);
        fixtureDef.setFriction(1f);       // attrito (tra 0 e 1, default 0.2)
        fixtureDef.setRestitution(1f);    // elasticità (tra 0 e 1, default 0)
        fixtureDef.setDensity(1);        // densità (kg/m2, default 0)
        body.createFixture(fixtureDef);

        body.setBullet(true);

        // release native objects
        bodyDef.delete();
        shape.delete();
        fixtureDef.delete();
    }
}