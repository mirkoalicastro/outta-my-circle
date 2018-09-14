package com.acg.outtamycircle.network.googleimpl;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.games.multiplayer.realtime.RoomStatusUpdateCallback;

import java.util.List;

public class MyRoomStatusUpdateCallback extends RoomStatusUpdateCallback {
    private static final String TAG = "GoogleS";
    private final MyGoogleRoom myGoogleRoom;
    public MyRoomStatusUpdateCallback(MyGoogleRoom myGoogleRoom) {
        this.myGoogleRoom = myGoogleRoom;
    }

    // Called when we are connected to the room. We're not ready to play yet! (maybe not everybody
    // is connected yet).
    @Override
    public void onConnectedToRoom(Room room) {
        myGoogleRoom.updateRoom(room);
    }

    // Called when we get disconnected from the room. We return to the main screen.
    @Override
    public void onDisconnectedFromRoom(Room room) {
        myGoogleRoom.leave();
    }


    // We treat most of the room update callbacks in the same way: we update our list of
    // participants and update the display. In a real game we would also have to check if that
    // change requires some action like removing the corresponding player avatar from the screen,
    // etc.
    @Override
    public void onPeerDeclined(Room room, @NonNull List<String> arg1) {
        myGoogleRoom.updateRoom(room);
    }

    @Override
    public void onPeerInvitedToRoom(Room room, @NonNull List<String> arg1) {
        myGoogleRoom.updateRoom(room);
    }

    @Override
    public void onP2PDisconnected(@NonNull String participant) {
    }

    @Override
    public void onP2PConnected(@NonNull String participant) {
    }

    @Override
    public void onPeerJoined(Room room, @NonNull List<String> arg1) {
        myGoogleRoom.updateRoom(room);
    }

    @Override
    public void onPeerLeft(Room room, @NonNull List<String> peersWhoLeft) {
        myGoogleRoom.updateRoom(room);
    }

    @Override
    public void onRoomAutoMatching(Room room) {
        myGoogleRoom.updateRoom(room);
    }

    @Override
    public void onRoomConnecting(Room room) {
        myGoogleRoom.updateRoom(room);
    }

    @Override
    public void onPeersConnected(Room room, @NonNull List<String> peers) {
        myGoogleRoom.updateRoom(room);
    }

    @Override
    public void onPeersDisconnected(Room room, @NonNull List<String> peers) {
        myGoogleRoom.updateRoom(room);
    }
}

