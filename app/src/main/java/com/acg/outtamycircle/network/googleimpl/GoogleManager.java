package com.acg.outtamycircle.network.googleimpl;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

public class GoogleManager {
    public static void init(Activity activity) {
        MyGoogleSignIn.createInstance(activity);
        GoogleRoom.createInstance(activity,MyGoogleSignIn.getInstance());
    }
    public static void handleResult(int requestCode, int resultCode, Intent data) {
        Log.d("GoogleS","YOYOYOYOYOOY");
        if(requestCode == GoogleRC.RC_SIGN_IN) {
            MyGoogleSignIn.getInstance().handleActivityResult(requestCode, resultCode, data);
        } else if(requestCode == GoogleRC.RC_WAITING_ROOM) {
            GoogleRoom.getInstance().handleActivityResult(requestCode, resultCode, data);
        }
    }
}
