package com.example.mfaella.physicsapp;

import android.util.Log;

import com.badlogic.androidgames.framework.Sound;
import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.Contact;
import com.google.fpl.liquidfun.ContactListener;
import com.google.fpl.liquidfun.Fixture;

/**
 * Created by mfaella on 01/03/16.
 */
public class MyContactListener extends ContactListener {
    public void beginContact(Contact contact) {
        //Log.d("MyContactListener", "Begin contact");
        Fixture fa = contact.getFixtureA(),
                fb = contact.getFixtureB();
        Body ba = fa.getBody(), bb = fb.getBody();
        Object userdataA = ba.getUserData(), userdataB = bb.getUserData();
        GameObject a = (GameObject)userdataA,
                   b = (GameObject)userdataB;

        Sound sound = CollisionSounds.getSound(a.getClass(), b.getClass());
        if (sound!=null)
            sound.play(0.7f);
        // Log.d("MyContactListener", "contact bwt " + a.name + " and " + b.name);
    }
}
