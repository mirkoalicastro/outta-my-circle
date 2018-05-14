package com.example.mfaella.physicsapp;

import com.example.mfaella.physicsapp.entitycomponent.PhysicsComponent;
import com.google.fpl.liquidfun.Body;

class CirclePhysicsComponent extends PhysicsComponent{
    /*public CirclePhysicsComponent() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.setPosition(owner.x, owner.y);
        bodyDef.setType(BodyType.dynamicBody);
        Body body = world.createBody(bodyDef);
        body.setSleepingAllowed(false);
        PolygonShape box = new PolygonShape();
        box.setAsBox(width / 2, height / 2); // misure in metri
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.setShape(box);
        fixtureDef.setFriction(0.1f); // attrito (tra 0 e 1, default 0.2)
        fixtureDef.setRestitution(0.4f); // elasticità (tra 0 e 1, default 0)
        fixtureDef.setDensity(0.5f); // densità (kg/m2, default 0)
        body.createFixture(fixtureDef);

        // release native objects
        bodyDef.delete();
        box.delete();
        fixtureDef.delete();
    }*/

    private Body body;
}