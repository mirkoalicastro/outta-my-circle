package com.acg.outtamycircle.contactphase;

import android.util.Log;

import com.acg.outtamycircle.Assets;
import com.acg.outtamycircle.Settings;

class CharacterPowerupContact extends ContactType{
    @Override
    public void handle() {
        Log.d("PROVAAAA", "powerupcontact handle");
        if(Settings.soundEnabled)
            Assets.powerupCollision.play(Settings.volume);
    }
}