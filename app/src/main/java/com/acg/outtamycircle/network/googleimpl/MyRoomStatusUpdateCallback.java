package com.acg.outtamycircle.network.googleimpl;

import android.support.annotation.NonNull;

import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.games.multiplayer.realtime.RoomStatusUpdateCallback;

import java.util.List;

public class MyRoomStatusUpdateCallback extends RoomStatusUpdateCallback {
    private final MyGoogleRoom myGoogleRoom;
    public MyRoomStatusUpdateCallback(MyGoogleRoom myGoogleRoom) {
        this.myGoogleRoom = myGoogleRoom;
    }

    @Override
    public void onConnectedToRoom(Room room) {
        myGoogleRoom.updateRoom(room);
    }

    @Override
    public void onDisconnectedFromRoom(Room room) {
        myGoogleRoom.leave();
    }

    @Override
    public void onPeerDeclined(Room room, @NonNull List<String> arg1) {
        myGoogleRoom.updateRoom(room);
    }

    @Override
    public void onPeerInvitedToRoom(Room room, @NonNull List<String> arg1) {
        myGoogleRoom.updateRoom(room);
    }

    @Override
    public void onP2PDisconnected(@NonNull String participant) { }

    @Override
    public void onP2PConnected(@NonNull String participant) { }

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

