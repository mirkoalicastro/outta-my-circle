package com.acg.outtamycircle.contactphase;

import android.util.Log;
import android.util.SparseArray;

import com.acg.outtamycircle.entitycomponent.impl.gameobjects.GameCharacter;
import com.acg.outtamycircle.entitycomponent.impl.gameobjects.Powerup;
import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.Contact;
import com.google.fpl.liquidfun.ContactListener;
import com.google.fpl.liquidfun.Fixture;

public class ContactHandler extends ContactListener{
    private SparseArray<ContactType> map;

    @Override
    public void beginContact(Contact contact) {
        super.beginContact(contact);
        Fixture fa = contact.getFixtureA(),
                fb = contact.getFixtureB();
        Body ba = fa.getBody(), bb = fb.getBody();

        ContactType contactType = map.get(ContactType.myHashCode(ba.getUserData().getClass(), bb.getUserData().getClass()));

        if (contactType != null)
            contactType.handle();
    }

    public void init() {
        map = new SparseArray<>();

        CharactersContact cc = new CharactersContact();

        CharacterPowerupContact cp = new CharacterPowerupContact();

        map.put(ContactType.myHashCode(GameCharacter.class,GameCharacter.class), cc);
        map.put(ContactType.myHashCode(GameCharacter.class,Powerup.class), cp);
    }
}
