package com.acg.outtamycircle.contactphase;

import android.util.Log;

import com.acg.outtamycircle.Assets;
import com.acg.outtamycircle.Settings;
import com.acg.outtamycircle.entitycomponent.impl.gameobjects.GameObject;
import com.badlogic.androidgames.framework.Sound;

public class CharactersContact extends ContactType{
    @Override
    public void handle(GameObject a, GameObject b){
        if(Settings.soundEnabled)
            Assets.gameCharacterCollision.play(Settings.volume);
    }
}
