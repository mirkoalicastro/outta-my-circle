package com.acg.outtamycircle.entitycomponent.impl.components;

import android.util.Log;

import com.acg.outtamycircle.GameStatus;
import com.acg.outtamycircle.entitycomponent.AttackComponent;
import com.acg.outtamycircle.entitycomponent.Component;
import com.acg.outtamycircle.entitycomponent.PhysicsComponent;
import com.acg.outtamycircle.entitycomponent.impl.gameobjects.GameCharacter;
import com.acg.outtamycircle.entitycomponent.impl.gameobjects.GameObject;

public final class RadialAttackComponent extends AttackComponent {
    private static final float MAX_FORCE = 7500;
    private static final float MIN_FORCE = 3000;
    private static final float DELTA_FORCE = MAX_FORCE-MIN_FORCE;

    @Override
    public void start(GameStatus status, float x, float y) {
        short myId = ((GameObject) owner).getObjectId();
        PhysicsComponent component = (PhysicsComponent) owner.getComponent(Type.Physics);
        float myX = component.getX();
        float myY = component.getY();

        float forceX, forceY;

        final float maxDistance = component.getHeight()*2.5f;

        for(GameCharacter ch : status.getLiving()){
            if(ch.getObjectId() == myId) continue;
            component = (PhysicsComponent)ch.getComponent(Component.Type.Physics);
            float distanceX = component.getX()-myX;
            float distanceY = component.getY()-myY;
            int signX, signY;

            if(distanceX>=0) {
                signX = 1;
            }
            else {
                distanceX = -distanceX;
                signX = -1;
            }

            if(distanceY>=0) {
                signY = 1;
            }
            else {
                distanceY = -distanceY;
                signY = -1;
            }

            if(distanceX>maxDistance || distanceY>maxDistance)
                continue;

            /*if(distanceX<=diameter)
                forceX = MAX_FORCE * signX;
            else*/
                forceX = signX * (MAX_FORCE - DELTA_FORCE*(distanceX/maxDistance));


            /*if(distanceY<=diameter)
                forceY = MAX_FORCE * signY;
            else*/
                forceY = signY * (MAX_FORCE - DELTA_FORCE*(distanceY/maxDistance));

            Log.d("ATTACKS", String.format("My position (%f,%f)\ndeltaX (%f,%f)\nforce (%f, %f)", myX,myY, distanceX, distanceY, forceX, forceY));

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
