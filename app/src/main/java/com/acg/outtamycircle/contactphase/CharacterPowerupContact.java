package com.acg.outtamycircle.contactphase;

import android.util.Log;

import com.acg.outtamycircle.Assets;
import com.acg.outtamycircle.GameStatus;
import com.acg.outtamycircle.Settings;
import com.acg.outtamycircle.entitycomponent.impl.gameobjects.GameCharacter;
import com.acg.outtamycircle.entitycomponent.impl.gameobjects.GameObject;
import com.acg.outtamycircle.entitycomponent.impl.gameobjects.Powerup;

class CharacterPowerupContact extends ContactType{
    private final GameStatus status;

    public CharacterPowerupContact(GameStatus status){
        this.status = status;
    }

    @Override
    public void handle(GameObject a, GameObject b) {
        if(Settings.soundEnabled)
            Assets.powerupCollision.play(Settings.volume);

        Powerup powerup = null;
        GameCharacter character = null;
        if(a.getType() == GameObject.Type.POWERUP) {
            powerup = (Powerup)a;
            character = (GameCharacter)b;
        }
        else{
            powerup = (Powerup)b;
            character = (GameCharacter)a;
        }

        powerup.setGameCharacter(character);

        status.getInactivePowerups().remove(powerup);
        status.getActivePowerups().add(powerup);
    }
}