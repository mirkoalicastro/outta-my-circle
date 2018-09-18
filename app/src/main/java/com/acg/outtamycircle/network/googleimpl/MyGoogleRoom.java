package com.acg.outtamycircle.network.googleimpl;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.acg.outtamycircle.MainMenuScreen;
import com.acg.outtamycircle.R;
import com.acg.outtamycircle.network.NetworkMessageHandlerImpl;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesActivityResultCodes;
import com.google.android.gms.games.RealTimeMultiplayerClient;
import com.google.android.gms.games.multiplayer.Participant;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;
import com.google.android.gms.games.multiplayer.realtime.RoomStatusUpdateCallback;
import com.google.android.gms.games.multiplayer.realtime.RoomUpdateCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

public class MyGoogleRoom {
    public static final int RC_WAITING_ROOM = 9_002;

    private ArrayList<Participant> mParticipants;
    private ArrayList<String> mParticipantIds;
    private String mMyId;
    private String mRoomId;
    private byte currentIdSkin;
    private byte currentIdAttack;

    private ResetCallback resetCallback;
    private static final int MIN_PLAYERS = 2;
    private static final int MAX_PLAYERS = 8;
    private final MyGoogleSignIn myGoogleSignIn;
    private final GoogleAndroidGame googleAndroidGame;
    private final RoomUpdateCallback myRoomUpdatedCallback = new MyRoomUpdateCallback(this);
    private final RoomStatusUpdateCallback myRoomStatusUpdatedCallback = new MyRoomStatusUpdateCallback(this);
    private final NetworkMessageHandlerImpl networkMessageHandlerImpl = new NetworkMessageHandlerImpl(this);

    private Room room;
    private RealTimeMultiplayerClient realTimeMultiplayerClient;
    private RoomConfig config;

    private String serverId;

    public boolean isServer() {
        if(getPlayerId() == null)
            return false;
        else
            return getPlayerId().equals(serverId);
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getServerId() {
        return serverId;
    }

    public String getRoomId() {
        return mRoomId;
    }

    public String getPlayerId() {
        return mMyId;
    }

    public byte getCurrentIdSkin() {
        return currentIdSkin;
    }

    public byte getCurrentIdAttack() {
        return currentIdAttack;
    }

    private volatile boolean locked = false;

    void leave() {
        locked = true;
        if(mRoomId != null && realTimeMultiplayerClient != null) {
            realTimeMultiplayerClient.leave(config, mRoomId)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            reset();
                        }
                    });
        }
    }

    public NetworkMessageHandlerImpl getNetworkMessageHandler() {
        return networkMessageHandlerImpl;
    }

    public ArrayList<String> getParticipantIds() {
        return mParticipantIds;
    }

    public ArrayList<Participant> getParticipants() {
        return mParticipants;
    }

    private void reset() {
        room = null;
        config = null;
        realTimeMultiplayerClient = null;
        mRoomId = null;
        mMyId = null;
        mParticipants = null;
        mParticipantIds = null;
        networkMessageHandlerImpl.setReceivers(new ClientMessageReceiver(MyGoogleRoom.MAX_PLAYERS), new ClientMessageReceiver(MyGoogleRoom.MAX_PLAYERS));
        serverId = null;
        locked = false;
        if(resetCallback != null) {
            resetCallback.reset();
            resetCallback = null;
        }
    }

    public MyGoogleRoom(GoogleAndroidGame googleAndroidGame, MyGoogleSignIn myGoogleSignIn) {
        this.googleAndroidGame = googleAndroidGame;
        this.myGoogleSignIn = myGoogleSignIn;
        if(myGoogleSignIn.isSignedIn())
            realTimeMultiplayerClient = Games.getRealTimeMultiplayerClient(googleAndroidGame, myGoogleSignIn.getAccount());
    }

    public void handleActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_WAITING_ROOM) {
            if(resultCode == Activity.RESULT_OK) {
                googleAndroidGame.startGame();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                realTimeMultiplayerClient.leave(config, getRoomId());
            } else if (resultCode == GamesActivityResultCodes.RESULT_LEFT_ROOM) {
                realTimeMultiplayerClient.leave(config, getRoomId());
            }
        }
    }

    public void error() {
        googleAndroidGame.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(googleAndroidGame)
                        .setMessage(googleAndroidGame.getString(R.string.google_error))
                        .setNeutralButton(android.R.string.ok, null).create().show();
                reset();
                googleAndroidGame.setScreen(new MainMenuScreen(googleAndroidGame));
            }
        });
    }

    void updateRoom(Room room) {
        setRoom(room);
        mRoomId = room.getRoomId();
        mMyId = room.getParticipantId(myGoogleSignIn.getPlayerId());
        mParticipants = room.getParticipants();
        mParticipantIds = room.getParticipantIds();
    }

    void showWaitingRoom(Room room) {
        Games.getRealTimeMultiplayerClient(googleAndroidGame, myGoogleSignIn.getAccount())
                .getWaitingRoomIntent(room, MAX_PLAYERS-1)
                .addOnSuccessListener(new OnSuccessListener<Intent>() {
                    @Override
                    public void onSuccess(Intent intent) {
                        googleAndroidGame.startActivityForResult(intent, RC_WAITING_ROOM);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        error();
                    }
                });
    }

    public boolean quickGame(ResetCallback resetCallback, int min_players, int max_players, byte currentIdSkin, byte currentIdAttack) {
        if(min_players < MIN_PLAYERS)
            throw new IllegalArgumentException("Min players must be at least " + MIN_PLAYERS);
        if(max_players > MAX_PLAYERS)
            throw new IllegalArgumentException("Max players must be at most " + MAX_PLAYERS);
        if(min_players > max_players)
            throw new IllegalArgumentException("Min players must be at least the number of max players");
        if(locked)
            return false;
        if(!myGoogleSignIn.isSignedIn()) {
            myGoogleSignIn.signIn();
            return false;
        }
        this.resetCallback = null;
        reset();
        this.resetCallback = resetCallback;
        this.currentIdSkin = currentIdSkin;
        this.currentIdAttack = currentIdAttack;
        realTimeMultiplayerClient = Games.getRealTimeMultiplayerClient(googleAndroidGame, myGoogleSignIn.getAccount());
        Bundle autoMatchCriteria = RoomConfig.createAutoMatchCriteria(min_players-1, max_players-1, (long)Math.pow(2,currentIdSkin));

        config = RoomConfig.builder(myRoomUpdatedCallback)
                .setOnMessageReceivedListener(networkMessageHandlerImpl)
                .setRoomStatusUpdateCallback(myRoomStatusUpdatedCallback)
                .setAutoMatchCriteria(autoMatchCriteria)
                .build();

        realTimeMultiplayerClient.create(config);

        return true;
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

    public interface ResetCallback {
        void reset();
    }
}
