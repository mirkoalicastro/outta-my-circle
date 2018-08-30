package com.acg.outtamycircle.network.googleimpl;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.badlogic.androidgames.framework.impl.AndroidGame;

public abstract class GoogleAndroidGame extends AndroidGame {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyGoogleSignIn.createInstance(this);
        GoogleRoom.createInstance(this,MyGoogleSignIn.getInstance());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("GoogleS","YOYOYOYOYOOY");
        if(requestCode == GoogleRC.RC_SIGN_IN) {
            MyGoogleSignIn.getInstance().handleActivityResult(requestCode, resultCode, data);
        } else if(requestCode == GoogleRC.RC_WAITING_ROOM) {
            GoogleRoom.getInstance().handleActivityResult(requestCode, resultCode, data);
        }
    }
}
