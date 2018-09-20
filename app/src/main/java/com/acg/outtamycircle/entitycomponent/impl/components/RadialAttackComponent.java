package com.acg.outtamycircle.entitycomponent.impl.components;

import com.acg.outtamycircle.GameStatus;
import com.acg.outtamycircle.entitycomponent.AttackComponent;
import com.acg.outtamycircle.entitycomponent.Component;
import com.acg.outtamycircle.entitycomponent.PhysicsComponent;
import com.acg.outtamycircle.entitycomponent.impl.gameobjects.GameCharacter;
import com.acg.outtamycircle.entitycomponent.impl.gameobjects.GameObject;

public final class RadialAttackComponent extends AttackComponent {
    private static final float MAX_FORCE = 10_250;
    private static final float DELTA_FORCE = 5_000;

    @Override
    public void start(GameStatus status, float x, float y) {
        int myId = ((GameObject) owner).getObjectId();
        PhysicsComponent component = (PhysicsComponent) owner.getComponent(Type.Physics);
        float myX = component.getX();
        float myY = component.getY();
        final float maxDistance = component.getHeight()*2.5f;
        float diameter = component.getHeight();

        for(GameCharacter ch : status.getLiving()){
            if(ch.getObjectId() == myId) continue;
            component = (PhysicsComponent)ch.getComponent(Component.Type.Physics);
            float distanceX = component.getX()-myX;
            float distanceY = component.getY()-myY;

            float distance = (float) Math.sqrt(distanceX*distanceX + distanceY*distanceY);
            if(distance>maxDistance)
                continue;
            float totalForce = MAX_FORCE - DELTA_FORCE*((distance-diameter)/(maxDistance-diameter));

            float forceX = totalForce * (distanceX/distance); //cosine
            float forceY = totalForce * (distanceY/distance); //sine

            component.applyForce(forceX, forceY);
        }
    }

    @Override
    public void attack() {

    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public void stop() {

    }
}
