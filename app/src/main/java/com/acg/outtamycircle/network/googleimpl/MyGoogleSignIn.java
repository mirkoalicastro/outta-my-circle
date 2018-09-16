package com.acg.outtamycircle.network.googleimpl;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.acg.outtamycircle.MainMenuScreen;
import com.acg.outtamycircle.R;
import com.badlogic.androidgames.framework.impl.AndroidGame;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.Player;
import com.google.android.gms.games.PlayersClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class MyGoogleSignIn {
    public static final int RC_SIGN_IN = 9_001;
    private static final GoogleSignInOptions OPTIONS = GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN;
    private GoogleSignInClient client;
    private final AndroidGame androidGame;
    private String playerId;

    public String getPlayerId() {
        return playerId;
    }

    public MyGoogleSignIn(AndroidGame androidGame) {
        this.androidGame = androidGame;
        this.client = GoogleSignIn.getClient(androidGame, OPTIONS);
    }

    public boolean isSignedIn() {
        return GoogleSignIn.getLastSignedInAccount(androidGame) != null;
    }

    public GoogleSignInAccount getAccount() {
        return GoogleSignIn.getLastSignedInAccount(androidGame);
    }

    public void signOut() {
        client.signOut();
    }

    private void onConnected(GoogleSignInAccount account) {

        PlayersClient playersClient = Games.getPlayersClient(androidGame, account);
        playersClient.getCurrentPlayer()
                .addOnSuccessListener(new OnSuccessListener<Player>() {
                    @Override
                    public void onSuccess(Player player) {
                        playerId = player.getPlayerId();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        playerId = null;
                    }
                });
    }

    private void onDisconnected() {
        signOut();
    }

    public void signIn() {
        client.silentSignIn().addOnCompleteListener(new OnCompleteListener<GoogleSignInAccount>() {
            @Override
            public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
                if (!task.isSuccessful() || !isSignedIn())
                    explicitSignIn();
                else
                    onConnected(task.getResult());
            }
        });
    }
    private void explicitSignIn() {
        Intent signInIntent = client.getSignInIntent();
        androidGame.startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    public void handleActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                onConnected(task.getResult(ApiException.class));
            } catch (final ApiException apiException) {
                onDisconnected();
                androidGame.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new AlertDialog.Builder(androidGame)
                                .setMessage(androidGame.getString(R.string.google_error_signin) + " (" + apiException.getStatusCode() + ")")
                                .setNeutralButton(android.R.string.ok, null)
                                .show();
                        androidGame.setScreen(new MainMenuScreen(androidGame));
                    }
                });
            }
        }
    }
}
