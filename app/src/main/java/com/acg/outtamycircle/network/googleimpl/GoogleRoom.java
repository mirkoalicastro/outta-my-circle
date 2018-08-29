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
import com.google.android.gms.games.RealTimeMultiplayerClient;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;
import com.google.android.gms.games.multiplayer.realtime.RoomStatusUpdateCallback;
import com.google.android.gms.games.multiplayer.realtime.RoomUpdateCallback;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;


public class GoogleRoom {
    public static final Room EMPTY = null;
    public static final int MIN_PLAYERS = 2;
    public static final int MAX_PLAYERS = 4;

    private final MyGoogleSignIn myGoogleSignIn;
    private final Activity activity;
    private final RoomUpdateCallback myRoomUpdatedCallback = new MyRoomUpdateCallback(this);
    private final RoomStatusUpdateCallback myRoomStatusUpdatedCallback = new MyRoomStatusUpdateCallback(this);
    private final ServerClientMessageHandler serverClientMessageHandler = new ServerClientMessageHandler();

    private volatile Room room;
    private RealTimeMultiplayerClient realTimeMultiplayerClient;

    private static String TAG = "GoogleS";

    public GoogleRoom(Activity activity, MyGoogleSignIn myGoogleSignIn) {
        this.activity = activity;
        this.myGoogleSignIn = myGoogleSignIn;
        realTimeMultiplayerClient = Games.getRealTimeMultiplayerClient(activity, myGoogleSignIn.getAccount());
    }

    protected void showGameError() {
        Log.d(TAG, "GAME ERROR WTF");
        new AlertDialog.Builder(activity)
                .setMessage(activity.getString(R.string.google_error))
                .setNeutralButton(android.R.string.ok, null).show();
        //switchToMainScreen();
    }

    protected void updateRoom(Room room) {
        Log.d(TAG, "UPDATE ROOM");
    }

    protected void showWaitingRoom(Room room) {
        showGameError();

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

    protected void setRoom(Room room) {
        this.room = room;
    }

}
