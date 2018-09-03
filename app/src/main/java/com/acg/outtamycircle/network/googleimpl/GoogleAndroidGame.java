package com.acg.outtamycircle.network.googleimpl;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.acg.outtamycircle.ClientServerScreen;
import com.badlogic.androidgames.framework.impl.AndroidGame;

import java.util.Calendar;

public abstract class GoogleAndroidGame extends AndroidGame {
    private void naivePrimeTest(int k) {
        for(int i=2,j,n=1;n<k;i++) {
            for (j = 2; j < i; j++)
                if (i % j == 0)
                    break;
            if(i == j)
                n++;
        }
    }
    private ClientServerScreen getClientServerScreen() {
        long time = -System.currentTimeMillis();
        naivePrimeTest(1500);
        time += System.currentTimeMillis();
        //send time
        //
        return null;
    }
    public void startGame() {
        getClientServerScreen();
       // setScreen(getClientServerScreen());
    }
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
