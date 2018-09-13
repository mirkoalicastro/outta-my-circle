package com.acg.outtamycircle.entitycomponent.impl.gameobjects;

import com.acg.outtamycircle.GameStatus;
import com.acg.outtamycircle.entitycomponent.Component;
import com.acg.outtamycircle.entitycomponent.PhysicsComponent;

public class RadialForcePowerup extends Powerup {
    private boolean isEnded = false;
    public static final short id = 0;

    public RadialForcePowerup(GameStatus status) {
        super(status, id);
    }

    @Override
    public void start() {
        PhysicsComponent component = (PhysicsComponent)character.getComponent(Component.Type.Physics);
        float x = component.getX(), y = component.getY();
        float forceX, forceY;

        for(GameCharacter ch : status.getLiving()){
            if(ch.equals(character))
                continue;
            component = (PhysicsComponent)ch.getComponent(Component.Type.Physics);
            forceX = component.getX() - x;
            forceY = component.getY() - y;
            component.applyForce(forceX, forceY);
        }

        isEnded = true;
    }

    @Override
    public void stop() {
        character = null;
    }

    @Override
    public boolean isEnded() {
        return isEnded;
    }
}