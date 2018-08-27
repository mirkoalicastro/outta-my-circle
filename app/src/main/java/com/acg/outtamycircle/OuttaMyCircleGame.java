package com.acg.outtamycircle;

import android.content.Intent;

import com.acg.outtamycircle.network.googleimpl.GoogleSign;
import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.impl.AndroidGame;

public class OuttaMyCircleGame extends AndroidGame {

    //TODO unica soluzione?
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GoogleSign.RC_SIGN_IN) {
            GoogleSign.handleSignInData(data);
        }
    }

    @Override
    public Screen getStartScreen() {
        return new StartScreen(this);
    }
}
