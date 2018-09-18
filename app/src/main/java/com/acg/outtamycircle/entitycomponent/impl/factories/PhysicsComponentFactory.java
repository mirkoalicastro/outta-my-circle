package com.acg.outtamycircle.entitycomponent.impl.factories;

import com.acg.outtamycircle.entitycomponent.Entity;
import com.acg.outtamycircle.entitycomponent.PhysicsComponent;
import com.acg.outtamycircle.entitycomponent.impl.components.LiquidFunPhysicsComponent;
import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.BodyDef;
import com.google.fpl.liquidfun.BodyType;
import com.google.fpl.liquidfun.FixtureDef;
import com.google.fpl.liquidfun.Shape;
import com.google.fpl.liquidfun.World;

public class PhysicsComponentFactory {
    private final World world;
    private final BodyDef bodyDef = new BodyDef();
    private final FixtureDef fixDef = new FixtureDef();

    private Entity owner;

    private Shape shape;
    private float radius, width, height;

    private boolean sleepingAllowed = true;
    private boolean awake = false;
    private boolean bullet = false;

    public PhysicsComponentFactory(World world){
        this.world = world;
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
        component.setWorld(world).setWidth(width).setHeight(height).setOwner(owner);

        Body body = world.createBody(bodyDef);
        body.setSleepingAllowed(sleepingAllowed);
        body.setUserData(component.getOwner());

        if(shape == null) {
            throw new IllegalStateException("Set the shape first");
        }
        if(owner == null) {
            throw new IllegalStateException("Set the owner first");
        }

        shape.setRadius(radius);
        fixDef.setShape(shape);

        body.createFixture(fixDef);

        body.setBullet(bullet);
        body.setAwake(awake);

        component.setBody(body);

        return component;
    }

    public void resetFactory(){
        owner = null; shape = null;
        radius = width = height = 0f;

        sleepingAllowed = true;
        awake = bullet = false;
    }
}
