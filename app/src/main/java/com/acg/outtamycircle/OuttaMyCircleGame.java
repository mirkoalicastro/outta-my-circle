package com.acg.outtamycircle;

import android.content.Intent;

import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.impl.AndroidGame;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;

public class OuttaMyCircleGame extends AndroidGame {

    //TODO unica soluzione?
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GoogleTest.RC_SIGN_IN) {
            GoogleTest.handleSignInData(data);
        }
    }

    @Override
    public Screen getStartScreen() {
        return new StartScreen(this);
    }
}
