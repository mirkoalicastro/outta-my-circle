package com.acg.outtamycircle.contactphase;

import android.util.Log;

import com.acg.outtamycircle.Assets;
import com.acg.outtamycircle.Settings;
import com.badlogic.androidgames.framework.Sound;

public class CharactersContact extends ContactType{
    @Override
    public void handle(){
        Log.d("PROVAAAA","handle");
        if(Settings.soundEnabled)
            Assets.gameCharacterCollision.play(Settings.volume);
    }
}
