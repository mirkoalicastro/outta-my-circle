package com.acg.outtamycircle.contactphase;

import android.util.SparseArray;

import com.acg.outtamycircle.GameStatus;
import com.acg.outtamycircle.entitycomponent.impl.gameobjects.GameCharacter;
import com.acg.outtamycircle.entitycomponent.impl.gameobjects.GameObject;
import com.acg.outtamycircle.entitycomponent.impl.gameobjects.RadialForcePowerup;
import com.acg.outtamycircle.entitycomponent.impl.gameobjects.WeightPowerup;
import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.Contact;
import com.google.fpl.liquidfun.ContactListener;
import com.google.fpl.liquidfun.Fixture;

public class ContactHandler extends ContactListener{
    private SparseArray<ContactType> map;

    public ContactHandler() {
        map = new SparseArray<>();
        CharactersContact cc = new CharactersContact();
        CharacterPowerupContact cp = new CharacterPowerupContact();

        map.put(myHashCode(GameCharacter.class, GameCharacter.class), cc);
        map.put(myHashCode(GameCharacter.class, WeightPowerup.class), cp);
        map.put(myHashCode(GameCharacter.class, RadialForcePowerup.class), cp);
    }

    @Override
    public void beginContact(Contact contact) {
        super.beginContact(contact);
        Fixture fa = contact.getFixtureA(),
                fb = contact.getFixtureB();
        Body ba = fa.getBody(), bb = fb.getBody();
        GameObject a = (GameObject)ba.getUserData(), b = (GameObject)bb.getUserData();

        ContactType contactType = map.get(myHashCode(a.getClass(), b.getClass()));

        if (contactType != null)
            contactType.handle(a, b);
    }

    public void init(GameStatus status) {
        for(int i=0; i<map.size(); i++)
            map.get(map.keyAt(i)).setStatus(status);
    }

    private static int myHashCode(Object a, Object b){
        return a.hashCode() ^ b.hashCode();
    }
}
