package com.acg.outtamycircle.network.googleimpl;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.acg.outtamycircle.network.ServerClientMessageHandler;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesCallbackStatusCodes;
import com.google.android.gms.games.RealTimeMultiplayerClient;
import com.google.android.gms.games.multiplayer.Participant;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;
import com.google.android.gms.games.multiplayer.realtime.RoomStatusUpdateCallback;
import com.google.android.gms.games.multiplayer.realtime.RoomUpdateCallback;

import java.util.ArrayList;
import java.util.List;

public class GoogleRoom {
    private static String TAG = "BEBE";
    private static final int MIN_OPPONENTS = 2;
    private static final int MAX_OPPONENTS = 4;
    private RealTimeMultiplayerClient client;
    private String roomId; //should be volatile?
    private final Activity activity;
    private final GoogleSign googleSign;
    private ArrayList<Participant> participants;
    private String myId;

    public GoogleRoom(Activity activity, GoogleSign googleSign) {
        this.activity = activity;
        this.googleSign = googleSign;
    }

    private RoomUpdateCallback roomUpdateCallback = new RoomUpdateCallback() {

        // Called when room has been created
        @Override
        public void onRoomCreated(int statusCode, Room room) {
            Log.d(TAG, "onRoomCreated(" + statusCode + ", " + room + ")");
            if (statusCode != GamesCallbackStatusCodes.OK) {
                Log.e(TAG, "*** Error: onRoomCreated, status " + statusCode);
                showGameError();
                return;
            }

            // save room ID so we can leave cleanly before the game starts.
            roomId = room.getRoomId();

            // show the waiting room UI

            showWaitingRoom(room);

        }
        // Called when room is fully connected.
        @Override
        public void onRoomConnected(int statusCode, Room room) {
            Log.d(TAG, "onRoomConnected(" + statusCode + ", " + room + ")");
            if (statusCode != GamesCallbackStatusCodes.OK) {
                Log.e(TAG, "*** Error: onRoomConnected, status " + statusCode);
                showGameError();
                return;
            }
            updateRoom(room);
        }

        @Override
        public void onJoinedRoom(int statusCode, Room room) {
            Log.d(TAG, "onJoinedRoom(" + statusCode + ", " + room + ")");
            if (statusCode != GamesCallbackStatusCodes.OK) {
                Log.e(TAG, "*** Error: onRoomConnected, status " + statusCode);
                showGameError();
                return;
            }

            // show the waiting room UI
            showWaitingRoom(room);
        }

        // Called when we've successfully left the room (this happens a result of voluntarily leaving
        // via a call to leaveRoom(). If we get disconnected, we get onDisconnectedFromRoom()).
        @Override
        public void onLeftRoom(int statusCode, @NonNull String roomId) {
            // we have left the room; return to main screen.
            Log.d(TAG, "onLeftRoom, code " + statusCode);
        }
    };

    private void showWaitingRoom(Room room) {
        //TODO
    }


    private RoomStatusUpdateCallback roomStatusUpdateCallback = new RoomStatusUpdateCallback() {
        // Called when we are connected to the room. We're not ready to play yet! (maybe not everybody
        // is connected yet).
        @Override
        public void onConnectedToRoom(Room room) {
            Log.d(TAG, "onConnectedToRoom.");

            //get participants and my ID:
            participants = room.getParticipants();
            //            myId = room.getParticipantId(playerId);

            // save room ID if its not initialized in onRoomCreated() so we can leave cleanly before the game starts.
            if (roomId == null) {
                roomId = room.getRoomId();
            }

        }

        // Called when we get disconnected from the room. We return to the main screen.
        @Override
        public void onDisconnectedFromRoom(Room room) {
            roomId = null;
           // config = null;
            showGameError();
        }


        // We treat most of the room update callbacks in the same way: we update our list of
        // participants and update the display. In a real game we would also have to check if that
        // change requires some action like removing the corresponding player avatar from the screen,
        // etc.
        @Override
        public void onPeerDeclined(Room room, @NonNull List<String> arg1) {
            updateRoom(room);
        }

        @Override
        public void onPeerInvitedToRoom(Room room, @NonNull List<String> arg1) {
            updateRoom(room);
        }

        @Override
        public void onP2PDisconnected(@NonNull String participant) {
        }

        @Override
        public void onP2PConnected(@NonNull String participant) {
        }

        @Override
        public void onPeerJoined(Room room, @NonNull List<String> arg1) {
            updateRoom(room);
        }

        @Override
        public void onPeerLeft(Room room, @NonNull List<String> peersWhoLeft) {
            updateRoom(room);
        }

        @Override
        public void onRoomAutoMatching(Room room) {
            updateRoom(room);
        }

        @Override
        public void onRoomConnecting(Room room) {
            updateRoom(room);
        }

        @Override
        public void onPeersConnected(Room room, @NonNull List<String> peers) {
            updateRoom(room);
        }

        @Override
        public void onPeersDisconnected(Room room, @NonNull List<String> peers) {
            updateRoom(room);
        }
    };

    private void showGameError() {
        //TODO
    }

    private void updateRoom(Room room) {
        //TODO
    }


    public void quickGame() {
        if(!googleSign.isSignedIn())
            throw new IllegalStateException("first login bitch");
        client = Games.getRealTimeMultiplayerClient(activity, googleSign.getAccount());
        Bundle autoMatchCriteria = RoomConfig.createAutoMatchCriteria(MIN_OPPONENTS, MAX_OPPONENTS, 0);
//        switchToScreen(R.id.screen_wait);

        RoomConfig config = RoomConfig.builder(roomUpdateCallback)
                .setOnMessageReceivedListener(new ServerClientMessageHandler())
                .setRoomStatusUpdateCallback(roomStatusUpdateCallback)
                .setAutoMatchCriteria(autoMatchCriteria)
                .build();
        client.create(config);
    }
}
