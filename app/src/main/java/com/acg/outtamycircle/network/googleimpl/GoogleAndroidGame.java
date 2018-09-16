package com.acg.outtamycircle.network.googleimpl;

import android.content.Intent;
import android.os.Bundle;

import com.acg.outtamycircle.PreMatchScreen;
import com.badlogic.androidgames.framework.impl.AndroidGame;

public abstract class GoogleAndroidGame extends AndroidGame {

    public void startGame() {
        this.setScreen(new PreMatchScreen(this,myGoogleRoom));
    }

    private MyGoogleSignIn myGoogleSignIn;
    private MyGoogleRoom myGoogleRoom;

    public MyGoogleRoom getMyGoogleRoom() {
        return myGoogleRoom;
    }

    public MyGoogleSignIn getMyGoogleSignIn() {
        return myGoogleSignIn;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myGoogleSignIn = new MyGoogleSignIn(this);
        myGoogleSignIn.signIn();
    }

    public void setMyGoogleRoom(MyGoogleRoom myGoogleRoom) {
        this.myGoogleRoom = myGoogleRoom;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == MyGoogleSignIn.RC_SIGN_IN) {
            myGoogleSignIn.handleActivityResult(requestCode, resultCode, data);
        } else if(requestCode == MyGoogleRoom.RC_WAITING_ROOM) {
            if(myGoogleRoom != null)
                myGoogleRoom.handleActivityResult(requestCode, resultCode, data);
        }
    }
}
