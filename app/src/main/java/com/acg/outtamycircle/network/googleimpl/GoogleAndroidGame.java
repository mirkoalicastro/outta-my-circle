package com.acg.outtamycircle.network.googleimpl;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.acg.outtamycircle.ClientScreen;
import com.acg.outtamycircle.ClientServerScreen;
import com.acg.outtamycircle.ServerScreen;
import com.acg.outtamycircle.network.GameMessage;
import com.acg.outtamycircle.network.GameMessageInterpreterImpl;
import com.acg.outtamycircle.network.ServerClientMessageHandler;
import com.badlogic.androidgames.framework.impl.AndroidGame;
import com.google.android.gms.games.RealTimeMultiplayerClient;

import java.util.Calendar;

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

    public static final String TAG = "ABCDARIO";

    private ClientServerScreen getClientServerScreen(GoogleRoom googleRoom) {
        Log.d(TAG, "inizio");
        String playerId = MyGoogleSignIn.getInstance().getPlayerId();
        long time = -System.currentTimeMillis();
        naivePrimeTest(500);
        time += System.currentTimeMillis();

        int bestTime = (int)time;
        String winnerId = playerId;

        Log.d(TAG,"continuo ok?");

        ServerClientMessageHandler client = googleRoom.getServerClientMessageHandler();
        GameMessageInterpreterImpl interpreter = new GameMessageInterpreterImpl();
        GameMessage gameMessage = new GameMessage();
        interpreter.makeHostOrClientMessage(gameMessage,bestTime);
        client.putInBuffer(gameMessage);
        Log.d(TAG,"colpa del broadcast?");
        client.broadcastUnreliable();

        Log.d(TAG, "sono "+ playerId + " e ho calcolato in " + bestTime);

        int count = 1;
        int sizeParticipants = googleRoom.getRoom().getParticipants().size();
        String sender = null;
        while(count < sizeParticipants) {
            Log.d(TAG,"sto ancora qui");
            for(GameMessage tmp: client.getMessages()) {
                int tmpTime = interpreter.getTimeMillis(tmp);
                if(tmpTime < bestTime || (tmpTime == bestTime && playerId.compareTo((sender=tmp.getSender())) > 0)) {
                    bestTime = tmpTime;
                    playerId = sender;
                }
                Log.d(TAG, "sono "+ playerId + " e ho ricevuto " + tmpTime + " da " + sender);
                count++;
            }
            if(count < sizeParticipants) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {

                }
            }
        }
        if(winnerId.equals(playerId)) {
            client.setReceivers(new ServerMessageReceiver(interpreter,sizeParticipants), new ServerMessageReceiver(interpreter,sizeParticipants));
            return new ServerScreen(this, null); //TODO ids?!
        } else {
            client.setReceivers(new ClientMessageReceiver(), new ClientMessageReceiver());
            return new ClientScreen(this, null); //TODO ids!?
        }
    }
    public void startGame() {
        GoogleRoom googleRoom = GoogleRoom.getInstance();
        googleRoom.getServerClientMessageHandler().setRoom(googleRoom.getRoom());
        ClientServerScreen clientServerScreen = getClientServerScreen(googleRoom);
        setScreen(clientServerScreen);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyGoogleSignIn.createInstance(this);
        GoogleRoom.createInstance(this,MyGoogleSignIn.getInstance());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("GoogleS","YOYOYOYOYOOY");
        if(requestCode == GoogleRC.RC_SIGN_IN) {
            MyGoogleSignIn.getInstance().handleActivityResult(requestCode, resultCode, data);
        } else if(requestCode == GoogleRC.RC_WAITING_ROOM) {
            GoogleRoom.getInstance().handleActivityResult(requestCode, resultCode, data);
        }
    }
}
