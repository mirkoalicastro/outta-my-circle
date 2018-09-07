package com.acg.outtamycircle.network.googleimpl;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.games.GamesCallbackStatusCodes;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.games.multiplayer.realtime.RoomUpdateCallback;

public class MyRoomUpdateCallback extends RoomUpdateCallback {
    private static final String TAG = "GoogleS";
    private final MyGoogleRoom myGoogleRoom;

    public MyRoomUpdateCallback(MyGoogleRoom MyGoogleRoom) {
        this.myGoogleRoom = MyGoogleRoom;
    }

    // Called when room has been created
    @Override
    public void onRoomCreated(int statusCode, Room room) {
        Log.d(TAG, "onRoomCreated(" + statusCode + ", " + room + ")");
        if (statusCode != GamesCallbackStatusCodes.OK) {
            Log.e(TAG, "*** Error: onRoomCreated, status " + statusCode);
            myGoogleRoom.showGameError();
        } else {
            myGoogleRoom.updateRoom(room);
            myGoogleRoom.showWaitingRoom(room);
        }
    }

    // Called when room is fully connected.
    @Override
    public void onRoomConnected(int statusCode, Room room) {
        Log.d(TAG, "onRoomConnected(" + statusCode + ", " + room + ")");
        if (statusCode != GamesCallbackStatusCodes.OK) {
            Log.e(TAG, "*** Error: onRoomConnected, status " + statusCode);
            myGoogleRoom.showGameError();
        } else {
            myGoogleRoom.updateRoom(room);
        }
    }

    @Override
    public void onJoinedRoom(int statusCode, Room room) {
        Log.d(TAG, "onJoinedRoom(" + statusCode + ", " + room + ")");
        if (statusCode != GamesCallbackStatusCodes.OK) {
            Log.e(TAG, "*** Error: onRoomConnected, status " + statusCode);
            myGoogleRoom.showGameError();
        } else {
            myGoogleRoom.updateRoom(room);
            myGoogleRoom.showWaitingRoom(room);
        }
    }

    // Called when we've successfully left the room (this happens a result of voluntarily leaving
    // via a call to leaveRoom(). If we get disconnected, we get onDisconnectedFromRoom()).
    @Override
    public void onLeftRoom(int statusCode, @NonNull String roomId) {
        // we have left the room; return to main screen.
        Log.d(TAG, "onLeftRoom, code " + statusCode);
        myGoogleRoom.leave();
    }

}
