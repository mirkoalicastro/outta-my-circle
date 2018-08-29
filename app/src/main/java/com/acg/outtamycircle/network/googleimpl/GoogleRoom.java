package com.acg.outtamycircle.network.googleimpl;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.acg.outtamycircle.network.ServerClientMessageHandler;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.RealTimeMultiplayerClient;
import com.google.android.gms.games.multiplayer.Participant;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;
import com.google.android.gms.games.multiplayer.realtime.RoomStatusUpdateCallback;
import com.google.android.gms.games.multiplayer.realtime.RoomUpdateCallback;

import java.util.ArrayList;

public class GoogleRoom {
    public static final Room EMPTY = null;
    public static final int MIN_OPPONENTS = 2;
    public static final int MAX_OPPONENTS = 4;

    private final Activity activity;
    private final RoomUpdateCallback myRoomUpdatedCallback = new MyRoomUpdateCallback(this);
    private final RoomStatusUpdateCallback myRoomStatusUpdatedCallback = new MyRoomStatusUpdateCallback(this);
    private final ServerClientMessageHandler serverClientMessageHandler = new ServerClientMessageHandler();

    private volatile Room room;
    private RealTimeMultiplayerClient realTimeMultiplayerClient;

    private static String TAG = "GoogleS";

    public GoogleRoom(Activity activity) {
        this.activity = activity;
    }

    protected void showGameError() {
        Log.e(TAG, "GAME ERROR WTF");
    }

    protected void updateRoom(Room room) {
        Log.d(TAG, "UPDATE ROOM");
    }

    protected void showWaitingRoom(Room room) {
        Log.d(TAG, "SHOW WAITING ROOM");
    }


    public void quickGame(MyGoogleSignIn myGoogleSignIn) {
        if(!myGoogleSignIn.isSignedIn())
            throw new IllegalStateException("first login bitch");
        realTimeMultiplayerClient = Games.getRealTimeMultiplayerClient(activity, myGoogleSignIn.getAccount());
        Bundle autoMatchCriteria = RoomConfig.createAutoMatchCriteria(MIN_OPPONENTS, MAX_OPPONENTS, 0);
//        switchToScreen(R.id.screen_wait);

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

    public void setRoom(Room room) {
        this.room = room;
    }

}
