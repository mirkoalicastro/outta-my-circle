package com.acg.outtamycircle;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class GoogleTest {
    private static final GoogleSignInOptions OPTIONS = GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN;
    public static final int RC_SIGN_IN = 9001;
    private static GoogleSignInClient client = null;
    private static Activity activity = null;

    private GoogleTest() {}

    public static void createClient(Activity activity) {
        client = GoogleSignIn.getClient(activity, OPTIONS);
        GoogleTest.activity = activity;
    }
    public static void signIn() {
        Intent signInIntent = client.getSignInIntent();
        activity.startActivityForResult(signInIntent,  RC_SIGN_IN);
    }
    public static void handleSignInData(Intent data) {
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            Log.d("GoogleTest", "Tutto ok");
        } catch (ApiException e) {
            Log.d("GoogleTest", "signInResult:failed code=" + e.getStatusCode() + "," + GoogleSignInStatusCodes.getStatusCodeString(e.getStatusCode()));
        }
    }

}
