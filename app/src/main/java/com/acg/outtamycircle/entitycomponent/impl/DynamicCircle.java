package com.acg.outtamycircle.entitycomponent.impl;

import com.acg.outtamycircle.entitycomponent.Entity;
import com.acg.outtamycircle.entitycomponent.PositionComponent;
import com.google.fpl.liquidfun.BodyDef;
import com.google.fpl.liquidfun.BodyType;
import com.google.fpl.liquidfun.CircleShape;
import com.google.fpl.liquidfun.FixtureDef;
import com.google.fpl.liquidfun.World;

public class DynamicCircle extends LiquidFunPhysicsComponent{
    public float radius;
    public DynamicCircle(World world, float radius, float x, float y) {
        this.radius = radius;

        BodyDef bodyDef = new BodyDef();
        bodyDef.setPosition(x, y);
        bodyDef.setType(BodyType.dynamicBody);

        body = world.createBody(bodyDef);
        body.setSleepingAllowed(false); //TODO meglio senza?

        CircleShape shape = new CircleShape();
        shape.setRadius(radius);
        shape.setPosition(x, y);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.setShape(shape);
        //fixtureDef.setFriction(1f);       // attrito (tra 0 e 1, default 0.2)
        fixtureDef.setRestitution(0.4f);    // elasticità (tra 0 e 1, default 0)
        //fixtureDef.setDensity(0);        // densità (kg/m2, default 0)
        body.createFixture(fixtureDef);

        // release native objects
        bodyDef.delete();
        shape.delete();
        fixtureDef.delete();
    }
}