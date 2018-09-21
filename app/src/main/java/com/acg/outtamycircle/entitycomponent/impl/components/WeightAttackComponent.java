package com.acg.outtamycircle.entitycomponent.impl.components;

import com.acg.outtamycircle.GameStatus;
import com.acg.outtamycircle.entitycomponent.AttackComponent;
import com.acg.outtamycircle.entitycomponent.impl.factories.PhysicsComponentFactory;
import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.BodyType;
import com.google.fpl.liquidfun.CircleShape;
import com.google.fpl.liquidfun.Shape;
import com.google.fpl.liquidfun.Vec2;
import com.google.fpl.liquidfun.World;

public class WeightAttackComponent extends AttackComponent {
    private static final long DURATION = 2000;
    private static final float DENSITY_MULTIPLIER = 4f;
    private static final float VELOCITY_MULTIPLIER = 0.65f;
    private static final float BASE_DENSITY = 1f;

    private PhysicsComponentFactory factory;
    private World world;
    private static final Shape SHAPE = new CircleShape();
    private float x, y, radius;
    private long firstCalled;
    private boolean active;

    @Override
    public void start(GameStatus status, float x, float y) {
        LiquidFunPhysicsComponent lastComponent = (LiquidFunPhysicsComponent) owner.getComponent(Type.Physics);
        Body body= lastComponent.body;
        Vec2 linearVelocity = body.getLinearVelocity();
        active = true;
        if(factory==null) {
            this.world = status.getWorld();
            factory = new PhysicsComponentFactory(world);
            factory.setAwake(body.isAwake()).setShape(SHAPE).setBullet(body.isBullet())
                    .setFriction(1f).setDensity(1f).setRestitution(1f)
                    .setType(BodyType.dynamicBody).setSleepingAllowed(body.isSleepingAllowed());
        }

        radius = lastComponent.getHeight()/2f;
        this.x = lastComponent.getX();
        this.y = lastComponent.getY();

        factory.setPosition(this.x, this.y);
        factory.setRadius(radius);
        factory.setOwner(owner);

        // create new
        factory.setDensity(BASE_DENSITY * DENSITY_MULTIPLIER);
        LiquidFunPhysicsComponent physicsComponent = (LiquidFunPhysicsComponent) factory.makeComponent();
        Body tmpBody = physicsComponent.body;

        linearVelocity.setX(linearVelocity.getX()*VELOCITY_MULTIPLIER);
        linearVelocity.setY(linearVelocity.getY()*VELOCITY_MULTIPLIER);
        tmpBody.setLinearVelocity(linearVelocity);

        // delete old
        lastComponent.delete();

        // update owner
        owner.addComponent(physicsComponent);
        physicsComponent.setOwner(owner);

        firstCalled = System.currentTimeMillis();
    }

    @Override
    public void attack() {
        if(System.currentTimeMillis()-firstCalled >= DURATION)
            active = false;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void stop() {
        LiquidFunPhysicsComponent tmpComponent = (LiquidFunPhysicsComponent) owner.getComponent(Type.Physics);
        Body tmpBody = tmpComponent.body;
        Vec2 linearVelocity = tmpBody.getLinearVelocity();

        factory.setDensity(BASE_DENSITY);
        factory.setPosition(tmpBody.getPositionX(), tmpBody.getPositionY());

        LiquidFunPhysicsComponent physicsComponent = (LiquidFunPhysicsComponent) factory.makeComponent();
        Body newBody = physicsComponent.body;
        newBody.setLinearVelocity(linearVelocity);

        owner.addComponent(physicsComponent);
        physicsComponent.setOwner(owner);

        tmpComponent.delete();
    }
}
