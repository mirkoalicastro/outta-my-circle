package com.acg.outtamycircle.contactphase;

import android.util.Log;

import com.acg.outtamycircle.Assets;
import com.acg.outtamycircle.GameStatus;
import com.acg.outtamycircle.Settings;
import com.acg.outtamycircle.entitycomponent.Component;
import com.acg.outtamycircle.entitycomponent.impl.components.LiquidFunPhysicsComponent;
import com.acg.outtamycircle.entitycomponent.impl.gameobjects.GameCharacter;
import com.acg.outtamycircle.entitycomponent.impl.gameobjects.GameObject;
import com.acg.outtamycircle.entitycomponent.impl.gameobjects.Powerup;
import com.acg.outtamycircle.network.GameMessage;
import com.acg.outtamycircle.network.GameMessageInterpreter;
import com.acg.outtamycircle.network.GameMessageInterpreterImpl;

class CharacterPowerupContact extends ContactType{
    private final GameStatus status;
    private final GameMessageInterpreter interpreter;

    public CharacterPowerupContact(GameStatus status, GameMessageInterpreter interpreter){
        this.status = status;
        this.interpreter = interpreter;
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

        LiquidFunPhysicsComponent component = (LiquidFunPhysicsComponent)powerup.getComponent(Component.Type.Physics);
        component.deleteBody();

        Log.d("NULLO", "prova");
        status.setPowerup(null);
        status.getActivePowerups().add(powerup);

        GameMessage message = GameMessage.createInstance();
        interpreter.makePowerUpAssign(message, character.getObjectId(), powerup.getObjectId()); //TODO ricccccardo

        GameMessage.deleteInstance(message);
    }
}