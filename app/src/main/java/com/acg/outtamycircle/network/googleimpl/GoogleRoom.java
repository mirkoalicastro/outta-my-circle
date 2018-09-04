package com.acg.outtamycircle.network.googleimpl;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.acg.outtamycircle.R;
import com.acg.outtamycircle.network.ServerClientMessageHandler;
import com.badlogic.androidgames.framework.impl.AndroidGame;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesActivityResultCodes;
import com.google.android.gms.games.RealTimeMultiplayerClient;
import com.google.android.gms.games.multiplayer.Participant;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;
import com.google.android.gms.games.multiplayer.realtime.RoomStatusUpdateCallback;
import com.google.android.gms.games.multiplayer.realtime.RoomUpdateCallback;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;


public class GoogleRoom {
    public static final int MIN_PLAYERS = 2;
    public static final int MAX_PLAYERS = 4;
    private static GoogleRoom instance;

    private final MyGoogleSignIn myGoogleSignIn;
    private final GoogleAndroidGame googleAndroidGame;
    private final RoomUpdateCallback myRoomUpdatedCallback = new MyRoomUpdateCallback(this);
    private final RoomStatusUpdateCallback myRoomStatusUpdatedCallback = new MyRoomStatusUpdateCallback(this);
    private final ServerClientMessageHandler serverClientMessageHandler = new ServerClientMessageHandler();
    private final MessageReceiver defaultFirstReceiver = new ClientMessageReceiver(GoogleRoom.MAX_PLAYERS);
    private final MessageReceiver defaultSecondReceiver = new ClientMessageReceiver(GoogleRoom.MAX_PLAYERS);

    private volatile Room room;
    private RealTimeMultiplayerClient realTimeMultiplayerClient;
    private RoomConfig config;

    private static String TAG = "GoogleS";

    public ServerClientMessageHandler getServerClientMessageHandler() {
        return serverClientMessageHandler;
    }

    public static void createInstance(GoogleAndroidGame googleAndroidGame, MyGoogleSignIn myGoogleSignIn) {
        instance = new GoogleRoom(googleAndroidGame, myGoogleSignIn);
    }

    private void reset() {
        room = null;
        config = null;
        realTimeMultiplayerClient = null;
        serverClientMessageHandler.setReceivers(defaultFirstReceiver, defaultSecondReceiver);
        serverClientMessageHandler.setClient(null);
    }

    public static GoogleRoom getInstance() {
        if(instance == null)
            throw new IllegalStateException("first create");
        return instance;
    }

    private GoogleRoom(GoogleAndroidGame googleAndroidGame, MyGoogleSignIn myGoogleSignIn) {
        this.googleAndroidGame = googleAndroidGame;
        this.myGoogleSignIn = myGoogleSignIn;
        if(myGoogleSignIn.isSignedIn())
            realTimeMultiplayerClient = Games.getRealTimeMultiplayerClient(googleAndroidGame, myGoogleSignIn.getAccount());
    }

    public void handleActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GoogleRC.RC_WAITING_ROOM) {
            if(resultCode == Activity.RESULT_OK) {
                Log.d(TAG,"OK");
                googleAndroidGame.startGame();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.d(TAG,"CANCELLED");
                realTimeMultiplayerClient.leave(config, room.getRoomId());
            } else if (resultCode == GamesActivityResultCodes.RESULT_LEFT_ROOM) {
                Log.d(TAG,"LEFT");
                realTimeMultiplayerClient.leave(config, room.getRoomId());
            }
        }
    }

    void showGameError() {
        Log.d(TAG, "GAME ERROR WTF");
        new AlertDialog.Builder(googleAndroidGame)
                .setMessage(googleAndroidGame.getString(R.string.google_error))
                .setNeutralButton(android.R.string.ok, null).create().show();
    }

    public static volatile String mMyId;
    public static volatile String mRoomId;
    public static volatile ArrayList<Participant> mParticipants;

    void updateRoom(Room room) {
        Log.d("PEPPE","Aggiorno la stanza");
        mRoomId = room.getRoomId();
        mParticipants = room.getParticipants();
        //TODO
    }

    void showWaitingRoom(Room room) {
        Log.d(TAG, "SHOW WAITING ROOM");
        Games.getRealTimeMultiplayerClient(googleAndroidGame, myGoogleSignIn.getAccount())
                .getWaitingRoomIntent(room, MAX_PLAYERS-1)
                .addOnSuccessListener(new OnSuccessListener<Intent>() {
                    @Override
                    public void onSuccess(Intent intent) {
                        googleAndroidGame.startActivityForResult(intent, GoogleRC.RC_WAITING_ROOM);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showGameError();
                    }
                });
    }

    public void quickGame(int min_players, int max_players) {
        if(!myGoogleSignIn.isSignedIn())
            throw new IllegalStateException("first login bitch"); //TODO non eccezione
        if(min_players < MIN_PLAYERS)
            throw new IllegalArgumentException("Min players must be at least " + MIN_PLAYERS);
        if(max_players > MAX_PLAYERS)
            throw new IllegalArgumentException("Max players must be at most " + MAX_PLAYERS);
        reset();
        realTimeMultiplayerClient = Games.getRealTimeMultiplayerClient(googleAndroidGame, myGoogleSignIn.getAccount());
        serverClientMessageHandler.setClient(realTimeMultiplayerClient);
        Bundle autoMatchCriteria = RoomConfig.createAutoMatchCriteria(min_players-1, max_players-1, 0);

        config = RoomConfig.builder(myRoomUpdatedCallback)
                .setOnMessageReceivedListener(serverClientMessageHandler)
                .setRoomStatusUpdateCallback(myRoomStatusUpdatedCallback)
                .setAutoMatchCriteria(autoMatchCriteria)
                .build();
        realTimeMultiplayerClient.create(config);
    }

    public RealTimeMultiplayerClient getRealTimeMultiplayerClient() {
        realTimeMultiplayerClient = Games.getRealTimeMultiplayerClient(googleAndroidGame, myGoogleSignIn.getAccount());
        return realTimeMultiplayerClient;
    }

    public Room getRoom() {
        return room;
    }

    void setRoom(Room room) {
        this.room = room;
    }

}
