package com.acg.outtamycircle.contactphase;

import com.acg.outtamycircle.Assets;
import com.acg.outtamycircle.Settings;
import com.acg.outtamycircle.entitycomponent.impl.gameobjects.GameObject;

public class CharactersContact extends ContactType{
    @Override
    public void handle(GameObject a, GameObject b){
        if(Settings.soundEnabled)
            Assets.gameCharacterCollision.play(Settings.volume);
        status.setCollisionDetected(true);
    }
}
