package com.example.mfaella.physicsapp;

import com.example.mfaella.physicsapp.entitycomponent.PositionComponent;
import com.google.fpl.liquidfun.BodyDef;
import com.google.fpl.liquidfun.BodyType;
import com.google.fpl.liquidfun.CircleShape;
import com.google.fpl.liquidfun.FixtureDef;
import com.google.fpl.liquidfun.World;

class CirclePhysicsComponent extends LiquidFunPhysicsComponent{
    public float radius;
    private float x, y;

    public CirclePhysicsComponent(World world, float radius) {
        PositionComponent pos = (PositionComponent)
                owner.getComponent(Type.Position);
        x = pos.x;
        y = pos.y;

        this.radius = radius;

        BodyDef bodyDef = new BodyDef();
        bodyDef.setPosition(x, y);
        bodyDef.setType(BodyType.dynamicBody);

        body = world.createBody(bodyDef);
        body.setSleepingAllowed(false);

        CircleShape shape = new CircleShape();
        shape.setRadius(radius);
        shape.setPosition(x, y);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.setShape(shape);
        fixtureDef.setFriction(0.1f); // attrito (tra 0 e 1, default 0.2)
        fixtureDef.setRestitution(0.4f); // elasticità (tra 0 e 1, default 0)
        fixtureDef.setDensity(0.5f); // densità (kg/m2, default 0)
        body.createFixture(fixtureDef);

        // release native objects
        bodyDef.delete();
        shape.delete();
        fixtureDef.delete();
    }
}