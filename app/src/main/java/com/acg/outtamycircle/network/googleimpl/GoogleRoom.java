package com.acg.outtamycircle.network.googleimpl;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.acg.outtamycircle.R;
import com.acg.outtamycircle.network.ServerClientMessageHandler;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesActivityResultCodes;
import com.google.android.gms.games.RealTimeMultiplayerClient;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;
import com.google.android.gms.games.multiplayer.realtime.RoomStatusUpdateCallback;
import com.google.android.gms.games.multiplayer.realtime.RoomUpdateCallback;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;


public class GoogleRoom {
    public static final int MIN_PLAYERS = 2;
    public static final int MAX_PLAYERS = 4;
    private static GoogleRoom instance;

    private final MyGoogleSignIn myGoogleSignIn;
    private final Activity activity;
    private final RoomUpdateCallback myRoomUpdatedCallback = new MyRoomUpdateCallback(this);
    private final RoomStatusUpdateCallback myRoomStatusUpdatedCallback = new MyRoomStatusUpdateCallback(this);
    private final ServerClientMessageHandler serverClientMessageHandler = new ServerClientMessageHandler();

    private volatile Room room;
    private RealTimeMultiplayerClient realTimeMultiplayerClient;

    private static String TAG = "GoogleS";

    public static void createInstance(Activity activity, MyGoogleSignIn myGoogleSignIn) {
        instance = new GoogleRoom(activity, myGoogleSignIn);
    }

    public static GoogleRoom getInstance() {
        if(instance == null)
            throw new IllegalStateException("first create");
        return instance;
    }

    private GoogleRoom(Activity activity, MyGoogleSignIn myGoogleSignIn) {
        this.activity = activity;
        this.myGoogleSignIn = myGoogleSignIn;
        realTimeMultiplayerClient = Games.getRealTimeMultiplayerClient(activity, myGoogleSignIn.getAccount());
    }

    public void handleActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GoogleRC.RC_SIGN_IN) {
            if (resultCode == Activity.RESULT_OK) {
                // (start game)
                // startGame(mParticipants.size());
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // Waiting room was dismissed with the back button. The meaning of this
                // action is up to the game. You may choose to leave the room and cancel the
                // match, or do something else like minimize the waiting room and
                // continue to connect in the background.

                // in this example, we take the simple approach and just leave the room:
                // Games.RealTimeMultiplayer.leave(getApiClient(), this, mRoomId);
                // getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            } else if (resultCode == GamesActivityResultCodes.RESULT_LEFT_ROOM) {
                // player wants to leave the room.
                // Games.RealTimeMultiplayer.leave(getApiClient(), this, mRoomId);
                // getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }
        }
    }


    void showGameError() {
        Log.d(TAG, "GAME ERROR WTF");
        new AlertDialog.Builder(activity)
                .setMessage(activity.getString(R.string.google_error))
                .setNeutralButton(android.R.string.ok, null).create().show();
        //switchToMainScreen();
    }

    void updateRoom(Room room) {

    }

    void showWaitingRoom(Room room) {
        Log.d(TAG, "SHOW WAITING ROOM");
        Games.getRealTimeMultiplayerClient(activity, myGoogleSignIn.getAccount())
                .getWaitingRoomIntent(room, MAX_PLAYERS-1)
                .addOnSuccessListener(new OnSuccessListener<Intent>() {
                    @Override
                    public void onSuccess(Intent intent) {
                        activity.startActivityForResult(intent, GoogleRC.RC_WAITING_ROOM);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showGameError();
                    }
                });
    }


    public void quickGame() {
        if(!myGoogleSignIn.isSignedIn())
            throw new IllegalStateException("first login bitch");
        realTimeMultiplayerClient = Games.getRealTimeMultiplayerClient(activity, myGoogleSignIn.getAccount());
        Bundle autoMatchCriteria = RoomConfig.createAutoMatchCriteria(MIN_PLAYERS-1, MAX_PLAYERS-1, 0);

        RoomConfig config = RoomConfig.builder(myRoomUpdatedCallback)
                .setOnMessageReceivedListener(serverClientMessageHandler)
                .setRoomStatusUpdateCallback(myRoomStatusUpdatedCallback)
                .setAutoMatchCriteria(autoMatchCriteria)
                .build();
        realTimeMultiplayerClient.create(config);
    }

    public RealTimeMultiplayerClient getRealTimeMultiplayerClient() {
        return realTimeMultiplayerClient;
    }

    public Room getRoom() {
        return room;
    }

    void setRoom(Room room) {
        this.room = room;
    }

}
