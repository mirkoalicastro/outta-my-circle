package com.acg.outtamycircle.network.googleimpl;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.acg.outtamycircle.ChooseClientOrServerScreen;
import com.acg.outtamycircle.ClientScreen;
import com.acg.outtamycircle.ClientServerScreen;
import com.acg.outtamycircle.ServerScreen;
import com.acg.outtamycircle.network.GameMessage;
import com.acg.outtamycircle.network.GameMessageInterpreter;
import com.acg.outtamycircle.network.GameMessageInterpreterImpl;
import com.acg.outtamycircle.network.ServerClientMessageHandler;
import com.badlogic.androidgames.framework.impl.AndroidGame;
import com.google.android.gms.games.RealTimeMultiplayerClient;
import com.google.android.gms.games.multiplayer.Participant;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public abstract class GoogleAndroidGame extends AndroidGame {
    private void naivePrimeTest(int k) {
        for(int i=2,j,n=1;n<k;i++) {
            for (j = 2; j < i; j++)
                if (i % j == 0)
                    break;
            if(i == j)
                n++;
        }
    }

    public static final String TAG = "PEPPE";

    private ChooseClientOrServerScreen getClientServerScreen(MyGoogleRoom myGoogleRoom) {
        Log.d(TAG, "inizio");
        String playerId = myGoogleSignIn.getPlayerId();
        long time = -System.currentTimeMillis();
        naivePrimeTest(500);
        time += System.currentTimeMillis();

        int bestTime = (int)time;

        ServerClientMessageHandler handler = myGoogleRoom.getServerClientMessageHandler();
        GameMessage gm = new GameMessage();
        GameMessageInterpreterImpl interpreter = new GameMessageInterpreterImpl();
        interpreter.makeHostOrClientMessage(gm, (int)time);
        handler.putInBuffer(gm);
        handler.broadcastReliable();

        return new ChooseClientOrServerScreen(this,handler,bestTime,playerId, myGoogleRoom.getRoom().getParticipants().size()-1);
    }
    public void startGame() {
        ChooseClientOrServerScreen clientServerScreen = getClientServerScreen(myGoogleRoom);
        setScreen(clientServerScreen);
    }

    private MyGoogleSignIn myGoogleSignIn;
    private MyGoogleRoom myGoogleRoom;

    public MyGoogleRoom getMyGoogleRoom() {
        return myGoogleRoom;
    }

    public MyGoogleSignIn getMyGoogleSignIn() {
        return myGoogleSignIn;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myGoogleSignIn = new MyGoogleSignIn(this);
        myGoogleRoom = new MyGoogleRoom(this,myGoogleSignIn);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("GoogleS","YOYOYOYOYOOY");
        if(requestCode == GoogleRC.RC_SIGN_IN) {
            myGoogleSignIn.handleActivityResult(requestCode, resultCode, data);
        } else if(requestCode == GoogleRC.RC_WAITING_ROOM) {
            myGoogleRoom.handleActivityResult(requestCode, resultCode, data);
        }
    }
}
