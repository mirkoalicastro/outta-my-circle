package com.acg.outtamycircle.contactphase;

import com.acg.outtamycircle.Settings;
import com.badlogic.androidgames.framework.Sound;

public class CharactersContact extends ContactType{
    private Sound sound;

    public void setSound(Sound sound){ this.sound = sound; }

    @Override
    public void handle(){
        sound.play(Settings.audioVolume);
    }
}
