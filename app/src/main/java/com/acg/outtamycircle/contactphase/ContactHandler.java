package com.acg.outtamycircle.contactphase;

import android.util.Log;
import android.util.SparseArray;

import com.acg.outtamycircle.GameStatus;
import com.acg.outtamycircle.entitycomponent.impl.gameobjects.GameCharacter;
import com.acg.outtamycircle.entitycomponent.impl.gameobjects.GameObject;
import com.acg.outtamycircle.entitycomponent.impl.gameobjects.Powerup;
import com.acg.outtamycircle.network.GameMessageInterpreter;
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
        GameObject a = (GameObject)ba.getUserData(), b = (GameObject)bb.getUserData();

        ContactType contactType = map.get(ContactType.myHashCode(a.getClass(), b.getClass()));

        if (contactType != null)
            contactType.handle(a, b);
    }

    public void init(GameStatus status, GameMessageInterpreter interpreter) {
        map = new SparseArray<>();

        CharactersContact cc = new CharactersContact();

        CharacterPowerupContact cp = new CharacterPowerupContact(status, interpreter);

        map.put(ContactType.myHashCode(GameCharacter.class,GameCharacter.class), cc);
        map.put(ContactType.myHashCode(GameCharacter.class,Powerup.class), cp);
    }
}
