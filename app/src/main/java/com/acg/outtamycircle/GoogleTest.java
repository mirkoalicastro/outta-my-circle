package com.acg.outtamycircle;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class GoogleTest {
    private static final GoogleSignInOptions OPTIONS = GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN;
    public static final int RC_SIGN_IN = 9_001;
    private static GoogleSignInClient client = null;
    private static Activity activity = null;

    private GoogleTest() {}

    public static void createClient(Activity activity) {
        client = GoogleSignIn.getClient(activity, OPTIONS);
        GoogleTest.activity = activity;
    }
    public static boolean isSignedIn() {
        return GoogleSignIn.getLastSignedInAccount(activity) != null;
    }

    private static GoogleSignInAccount getAccount() {
        return GoogleSignIn.getLastSignedInAccount(activity);
    }

    public static void signOut() {
        client.signOut();
    }

    public static void signIn() { //onresume
        client.silentSignIn().addOnCompleteListener(new OnCompleteListener<GoogleSignInAccount>() {
            @Override
            public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
                if (!task.isSuccessful())
                    explicitSignIn();
            }
        });
    }
    private static void explicitSignIn() {
        Intent signInIntent = client.getSignInIntent();
        activity.startActivityForResult(signInIntent,  RC_SIGN_IN);
    }
    public static void handleSignInData(Intent data) {
        GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
        if (result.isSuccess()) {
            // The signed in account is stored in the result.
            Log.d("GoogleTest", "Tutto ok");
        } else {
            String message = result.getStatus().getStatusMessage();
            Log.d("GoogleTest", "signInResult:failed code=" + result.getStatus().getStatusCode() + "," + GoogleSignInStatusCodes.getStatusCodeString(result.getStatus().getStatusCode()));
            if (message == null || message.isEmpty()) {
                message = "BOOH";
            }
            new AlertDialog.Builder(activity).setMessage(message).setNeutralButton(android.R.string.ok, null).show();
        }
    }

}
