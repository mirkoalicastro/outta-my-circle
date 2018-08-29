package com.acg.outtamycircle;

import android.content.Intent;
import android.os.Bundle;

import com.acg.outtamycircle.network.googleimpl.GoogleRC;
import com.acg.outtamycircle.network.googleimpl.MyGoogleSignIn;
import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.impl.AndroidGame;

public class OuttaMyCircleGame extends AndroidGame {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyGoogleSignIn.createInstance(this);

    }

    //TODO unica soluzione?
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GoogleRC.RC_SIGN_IN) {
            MyGoogleSignIn.getInstance().handleSignData(requestCode, resultCode, data);
        }
    }

    @Override
    public Screen getStartScreen() {
        return new StartScreen(this);
    }
}
