package com.acg.outtamycircle.contactphase;

import com.acg.outtamycircle.Assets;
import com.acg.outtamycircle.Settings;
import com.acg.outtamycircle.entitycomponent.Component;
import com.acg.outtamycircle.entitycomponent.impl.components.LiquidFunPhysicsComponent;
import com.acg.outtamycircle.entitycomponent.impl.gameobjects.GameCharacter;
import com.acg.outtamycircle.entitycomponent.impl.gameobjects.GameObject;
import com.acg.outtamycircle.entitycomponent.impl.gameobjects.Powerup;

class CharacterPowerupContact extends ContactType {

    @Override
    public void handle(GameObject a, GameObject b) {
        Powerup powerup = null;
        GameCharacter character = null;
        if(a.getType() == GameObject.Type.POWERUP) {
            powerup = (Powerup)a;
            character = (GameCharacter)b;
        } else {
            powerup = (Powerup)b;
            character = (GameCharacter)a;
        }

        synchronized (powerup) {
            if (powerup.getAssigned())
                return;
            powerup.setAssigned(true);
        }

        if(Settings.soundEnabled)
            Assets.powerupCollision.play(Settings.volume);

        powerup.setGameCharacter(character);

        LiquidFunPhysicsComponent component = (LiquidFunPhysicsComponent)powerup.getComponent(Component.Type.Physics);
        component.getBody().setAwake(false);
        component.getBody().setActive(false);

        status.setPowerup(null);

        // world can't be changed during collision handling
        status.getPowerupsToActivate().add(powerup);

    }
}