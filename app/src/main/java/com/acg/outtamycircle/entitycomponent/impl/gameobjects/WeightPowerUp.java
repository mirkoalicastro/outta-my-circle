package com.acg.outtamycircle.entitycomponent.impl.gameobjects;

import android.util.Log;

import com.acg.outtamycircle.GameStatus;
import com.acg.outtamycircle.entitycomponent.Component;
import com.acg.outtamycircle.entitycomponent.Entity;
import com.acg.outtamycircle.entitycomponent.impl.components.LiquidFunPhysicsComponent;
import com.acg.outtamycircle.entitycomponent.impl.factories.PhysicsComponentFactory;
import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.BodyType;
import com.google.fpl.liquidfun.CircleShape;
import com.google.fpl.liquidfun.Shape;
import com.google.fpl.liquidfun.Vec2;
import com.google.fpl.liquidfun.World;

public class WeightPowerUp extends Powerup {
    private static final long DURATION = 2000;
    private static final float DENSITY_MULTIPLIER = 4f;
    private static final float VELOCITY_MULTIPLIER = 0.65f;
    private static final float BASE_DENSITY = 1f;
    public static final short ID = 0 ;

    private PhysicsComponentFactory factory;
    private World world;
    private static final Shape SHAPE = new CircleShape();
    private long firstCalled;
    private boolean ended;

    public WeightPowerUp(GameStatus status, short id) {
        super(status, id);
        world = status.getWorld();
        factory = new PhysicsComponentFactory(world);

        factory = new PhysicsComponentFactory(world);
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
        factory.setDensity(BASE_DENSITY * DENSITY_MULTIPLIER);
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
        lastComponent.deleteBody();
        //TODO drawable?

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

        factory.setDensity(BASE_DENSITY);
        factory.setPosition(tmpBody.getPositionX(), tmpBody.getPositionY());
        factory.setOwner(character);
        LiquidFunPhysicsComponent physicsComponent = (LiquidFunPhysicsComponent) factory.makeComponent();
        Body newBody = physicsComponent.getBody();
        newBody.setLinearVelocity(linearVelocity);

        character.addComponent(physicsComponent);
        physicsComponent.setOwner(character);

        tmpComponent.deleteBody();
    }

    @Override
    public boolean isEnded() {
        return ended;
    }
}
