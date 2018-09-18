package com.acg.outtamycircle.network.googleimpl;

import android.support.annotation.NonNull;

import com.google.android.gms.games.GamesCallbackStatusCodes;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.games.multiplayer.realtime.RoomUpdateCallback;

public class MyRoomUpdateCallback extends RoomUpdateCallback {
    private final MyGoogleRoom myGoogleRoom;

    public MyRoomUpdateCallback(MyGoogleRoom MyGoogleRoom) {
        this.myGoogleRoom = MyGoogleRoom;
    }

    @Override
    public void onRoomCreated(int statusCode, Room room) {
        if (statusCode != GamesCallbackStatusCodes.OK) {
            myGoogleRoom.error();
        } else {
            myGoogleRoom.updateRoom(room);
            myGoogleRoom.showWaitingRoom(room);
        }
    }

    @Override
    public void onRoomConnected(int statusCode, Room room) {
        if (statusCode != GamesCallbackStatusCodes.OK)
            myGoogleRoom.error();
        else
            myGoogleRoom.updateRoom(room);
    }

    @Override
    public void onJoinedRoom(int statusCode, Room room) {
        if (statusCode != GamesCallbackStatusCodes.OK)
            myGoogleRoom.error();
        else {
            myGoogleRoom.updateRoom(room);
            myGoogleRoom.showWaitingRoom(room);
        }
    }

    @Override
    public void onLeftRoom(int statusCode, @NonNull String roomId) {
        myGoogleRoom.leave();
    }

}
