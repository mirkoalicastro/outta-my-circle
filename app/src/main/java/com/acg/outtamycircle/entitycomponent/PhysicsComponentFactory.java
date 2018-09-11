package com.acg.outtamycircle.entitycomponent;

import com.acg.outtamycircle.entitycomponent.impl.LiquidFunPhysicsComponent;
import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.BodyDef;
import com.google.fpl.liquidfun.BodyType;
import com.google.fpl.liquidfun.FixtureDef;
import com.google.fpl.liquidfun.Shape;
import com.google.fpl.liquidfun.World;

public class PhysicsComponentFactory {
    private World world;
    private Entity owner;

    private BodyDef bodyDef = new BodyDef();
    private FixtureDef fixDef = new FixtureDef();

    private Shape shape;
    private float radius, width, height;

    private boolean sleepingAllowed = true;
    private boolean awake = false;
    private boolean bullet = false;


    public PhysicsComponentFactory setWorld(World world){
        this.world = world;
        return this;
    }

    public PhysicsComponentFactory setPosition(float x, float y) {
        bodyDef.setPosition(x, y);
        return this;
    }

    public PhysicsComponentFactory setType(BodyType type) {
        bodyDef.setType(type);
        return this;
    }

    public PhysicsComponentFactory setFriction(float friction){
        fixDef.setFriction(friction);
        return this;
    }

    public PhysicsComponentFactory setRestitution(float restitution){
        fixDef.setRestitution(restitution);
        return this;
    }

    public PhysicsComponentFactory setDensity(float density) {
        fixDef.setDensity(density);
        return this;
    }

    public PhysicsComponentFactory setShape(Shape shape){
        this.shape = shape;
        return this;
    }

    public PhysicsComponentFactory setWidth(float width){
        this.width = width;
        return this;
    }


    public PhysicsComponentFactory setHeight(float height){
        this.height = height;
        return this;
    }

    public PhysicsComponentFactory setRadius(float radius){
        this.radius = radius;
        width = radius*2;
        height = radius*2;
        return this;
    }

    public PhysicsComponentFactory setSleepingAllowed(boolean val){
        sleepingAllowed = val;
        return this;
    }

    public PhysicsComponentFactory setAwake(boolean val){
        awake = val;
        return this;
    }

    public PhysicsComponentFactory setBullet(boolean val){
        bullet = val;
        return this;
    }

    public PhysicsComponentFactory setOwner(Entity owner) {
        this.owner = owner;
        return this;
    }

    public PhysicsComponent makeComponent(){
        LiquidFunPhysicsComponent component = new LiquidFunPhysicsComponent();
        component.setWidth(width).setHeight(height).setOwner(owner);

        Body body = world.createBody(bodyDef);
        body.setSleepingAllowed(sleepingAllowed);
        body.setUserData(component.owner);

        if(shape == null)
            throw new NullPointerException("Create a shape before");
        shape.setRadius(radius);
        fixDef.setShape(shape);

        body.createFixture(fixDef);

        body.setBullet(bullet);
        body.setAwake(awake);

        return component.setBody(body);
    }
}
