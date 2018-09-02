package com.acg.outtamycircle.contactphase;

import android.util.SparseArray;

import com.acg.outtamycircle.entitycomponent.impl.GameCharacter;
import com.acg.outtamycircle.entitycomponent.impl.GameObject;
import com.acg.outtamycircle.entitycomponent.impl.PowerUp;
import com.badlogic.androidgames.framework.Audio;
import com.badlogic.androidgames.framework.Sound;
import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.Contact;
import com.google.fpl.liquidfun.ContactListener;
import com.google.fpl.liquidfun.Fixture;

public class ContactHandler extends ContactListener{
    private Audio audio;
    private SparseArray<ContactType> map;

    @Override
    public void beginContact(Contact contact){
        Fixture fa = contact.getFixtureA(),
                fb = contact.getFixtureB();
        Body ba = fa.getBody(), bb = fb.getBody();


        GameObject a = (GameObject)ba.getUserData();
        GameObject b = (GameObject)bb.getUserData();

        map.get(ContactType.myHashCode(a.getClass(), b.getClass())).handle();
        //se, caso strano, capita una collisione tra due powerup, cosa torna map?

        //se generalizzo myHashCode a Class<?> non devo fare il cast esplicito
    }

    public void init(Audio audio){
        this.audio = audio;
        map = new SparseArray<>();

        CharactersContact cc = new CharactersContact();
        CharacterPowerupContact cp = new CharacterPowerupContact();

        map.put(ContactType.myHashCode(GameCharacter.class,GameCharacter.class), cc);
        map.put(ContactType.myHashCode(GameCharacter.class,PowerUp.class), cp);
    }
}
