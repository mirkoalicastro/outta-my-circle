package com.acg.outtamycircle.network.googleimpl;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.games.GamesCallbackStatusCodes;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.games.multiplayer.realtime.RoomUpdateCallback;

public class MyRoomUpdateCallback extends RoomUpdateCallback {
    private static final String TAG = "GoogleS";
    private final GoogleRoom googleRoom;

    public MyRoomUpdateCallback(GoogleRoom googleRoom) {
        this.googleRoom = googleRoom;
    }

    // Called when room has been created
    @Override
    public void onRoomCreated(int statusCode, Room room) {
        Log.d(TAG, "onRoomCreated(" + statusCode + ", " + room + ")");
        if (statusCode != GamesCallbackStatusCodes.OK) {
            Log.e(TAG, "*** Error: onRoomCreated, status " + statusCode);
            googleRoom.showGameError();
            return;
        }
        googleRoom.showWaitingRoom(room);
        googleRoom.updateRoom(room);
    }

    // Called when room is fully connected.
    @Override
    public void onRoomConnected(int statusCode, Room room) {
        Log.d(TAG, "onRoomConnected(" + statusCode + ", " + room + ")");
        if (statusCode != GamesCallbackStatusCodes.OK) {
            Log.e(TAG, "*** Error: onRoomConnected, status " + statusCode);
            googleRoom.showGameError();
            return;
        }
        googleRoom.updateRoom(room);
    }

    @Override
    public void onJoinedRoom(int statusCode, Room room) {
        Log.d(TAG, "onJoinedRoom(" + statusCode + ", " + room + ")");
        if (statusCode != GamesCallbackStatusCodes.OK) {
            Log.e(TAG, "*** Error: onRoomConnected, status " + statusCode);
            googleRoom.showGameError();
            return;
        }
        googleRoom.showWaitingRoom(room);
        googleRoom.updateRoom(room);
    }

    // Called when we've successfully left the room (this happens a result of voluntarily leaving
    // via a call to leaveRoom(). If we get disconnected, we get onDisconnectedFromRoom()).
    @Override
    public void onLeftRoom(int statusCode, @NonNull String roomId) {
        // we have left the room; return to main screen.
        Log.d(TAG, "onLeftRoom, code " + statusCode);
    }

}
