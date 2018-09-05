package com.acg.outtamycircle.entitycomponent.impl;

import com.google.fpl.liquidfun.BodyDef;
import com.google.fpl.liquidfun.BodyType;
import com.google.fpl.liquidfun.CircleShape;
import com.google.fpl.liquidfun.FixtureDef;
import com.google.fpl.liquidfun.World;

public class StaticCircle extends LiquidFunPhysicsComponent{
    private float radius;
    //volatile utile all'accesso concorrente di drawCharacters e thread di controllo

    public StaticCircle(World world, float radius, float x, float y) {
        this.radius = radius;

        BodyDef bodyDef = new BodyDef();
        bodyDef.setPosition(x, y);
        bodyDef.setType(BodyType.kinematicBody);

        body = world.createBody(bodyDef);
        body.setSleepingAllowed(true);
        body.setUserData(owner);

        CircleShape shape = new CircleShape();
        shape.setRadius(radius);
        //       shape.setPosition(x, y);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.setShape(shape);
        fixtureDef.setFriction(0f);       // attrito (tra 0 e 1, default 0.2)
        fixtureDef.setRestitution(0f);    // elasticità (tra 0 e 1, default 0)
        fixtureDef.setDensity(Float.MIN_VALUE);        // densità (kg/m2, default 0)
        body.createFixture(fixtureDef);

        body.setAwake(false);

        // release native objects
        bodyDef.delete();
        shape.delete();
        fixtureDef.delete();
    }

    public float getRadius(){ return radius; }
}
