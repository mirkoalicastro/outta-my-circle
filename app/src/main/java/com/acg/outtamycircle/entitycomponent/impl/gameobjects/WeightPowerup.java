package com.acg.outtamycircle.entitycomponent.impl.gameobjects;

import com.acg.outtamycircle.GameStatus;
import com.acg.outtamycircle.entitycomponent.Component;
import com.acg.outtamycircle.entitycomponent.impl.components.LiquidFunPhysicsComponent;
import com.acg.outtamycircle.entitycomponent.impl.factories.PhysicsComponentFactory;
import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.BodyType;
import com.google.fpl.liquidfun.CircleShape;
import com.google.fpl.liquidfun.Shape;
import com.google.fpl.liquidfun.Vec2;

public class WeightPowerup extends Powerup {
    private static final long DURATION = 4000;
    private static final float DENSITY_MULTIPLIER = 2.5f;
    private static final float VELOCITY_MULTIPLIER = 0.8f;
    private static final float BASE_DENSITY = 1f;
    public static final int ID = 0 ;

    private PhysicsComponentFactory factory;
    private static final Shape SHAPE = new CircleShape();
    private long firstCalled;
    private boolean ended;

    @Override
    public byte getCode(){
        return ID;
    }

    public WeightPowerup(GameStatus status, int id) {
        super(status, id);
        factory = new PhysicsComponentFactory(status.getWorld());

        factory = new PhysicsComponentFactory(status.getWorld());
        factory.setAwake(true).setShape(SHAPE).setBullet(true)
                .setFriction(1f).setDensity(1f).setRestitution(1f)
                .setType(BodyType.dynamicBody).setSleepingAllowed(true);
    }

    @Override
    public void start() {
        LiquidFunPhysicsComponent lastComponent = (LiquidFunPhysicsComponent) character.getComponent(Component.Type.Physics);
        Body body= lastComponent.getBody();
        Vec2 linearVelocity = body.getLinearVelocity();
        float radius = lastComponent.getHeight()/2f;

        factory.setPosition(lastComponent.getX(), lastComponent.getY());
        factory.setRadius(radius);
        factory.setOwner(character);

        // create new
        factory.setDensity(BASE_DENSITY * DENSITY_MULTIPLIER).setRestitution(.6f);
        factory.setWidth(radius*2).setHeight(radius*2);

        LiquidFunPhysicsComponent physicsComponent = (LiquidFunPhysicsComponent) factory.makeComponent();
        Body tmpBody = physicsComponent.getBody();

        linearVelocity.setX(linearVelocity.getX()*VELOCITY_MULTIPLIER);
        linearVelocity.setY(linearVelocity.getY()*VELOCITY_MULTIPLIER);
        tmpBody.setLinearVelocity(linearVelocity);

        // update owner
        character.addComponent(physicsComponent);
        physicsComponent.setOwner(character);

        // delete old
        lastComponent.delete();

        firstCalled = System.currentTimeMillis();
    }

    @Override
    public void work(){
        if(System.currentTimeMillis()-firstCalled >= DURATION)
            ended = true;
    }

    @Override
    public void stop() {
        LiquidFunPhysicsComponent tmpComponent = (LiquidFunPhysicsComponent) character.getComponent(Component.Type.Physics);
        Body tmpBody = tmpComponent.getBody();
        Vec2 linearVelocity = tmpBody.getLinearVelocity();

        factory.setDensity(BASE_DENSITY).setRestitution(1f);
        factory.setPosition(tmpBody.getPositionX(), tmpBody.getPositionY());
        factory.setOwner(character);
        LiquidFunPhysicsComponent physicsComponent = (LiquidFunPhysicsComponent) factory.makeComponent();
        Body newBody = physicsComponent.getBody();
        newBody.setLinearVelocity(linearVelocity);

        character.addComponent(physicsComponent);
        physicsComponent.setOwner(character);

        tmpComponent.delete();
    }

    @Override
    public boolean isEnded() {
        return ended;
    }
}
