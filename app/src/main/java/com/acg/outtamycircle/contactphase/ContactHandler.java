package com.acg.outtamycircle.contactphase;

import android.util.SparseArray;

import com.acg.outtamycircle.entitycomponent.impl.GameCharacter;
import com.acg.outtamycircle.entitycomponent.impl.Powerup;
import com.badlogic.androidgames.framework.Audio;
import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.Contact;
import com.google.fpl.liquidfun.ContactListener;
import com.google.fpl.liquidfun.Fixture;

//TODO valutare posizione nei package
//l'idea è di creare un package per ogni fase di gioco

public class ContactHandler extends ContactListener{
    private Audio audio;
    private SparseArray<ContactType> map;

    @Override
    public void beginContact(Contact contact){
        Fixture fa = contact.getFixtureA(),
                fb = contact.getFixtureB();
        Body ba = fa.getBody(), bb = fb.getBody();

        ContactType contactType = map.get(ContactType.myHashCode(ba.getUserData(), bb.getUserData()));

        if(contactType != null) contactType.handle();

        //se generalizzo myHashCode a Class<?> non devo fare il cast esplicito
    }

    public void init(Audio audio){
        this.audio = audio;
        map = new SparseArray<>();

        CharactersContact cc = new CharactersContact();
        CharacterPowerupContact cp = new CharacterPowerupContact();

        map.put(ContactType.myHashCode(GameCharacter.class,GameCharacter.class), cc);
        map.put(ContactType.myHashCode(GameCharacter.class,Powerup.class), cp);
    }
}
