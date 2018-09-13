package com.acg.outtamycircle;

import android.graphics.Color;
import android.util.Log;

import com.acg.outtamycircle.network.GameMessage;
import com.acg.outtamycircle.network.GameMessageInterpreterImpl;
import com.acg.outtamycircle.network.NetworkMessageHandler;
import com.acg.outtamycircle.network.NetworkMessageHandlerImpl;
import com.acg.outtamycircle.network.googleimpl.ClientMessageReceiver;
import com.acg.outtamycircle.network.googleimpl.MyGoogleRoom;
import com.acg.outtamycircle.network.googleimpl.ServerMessageReceiver;
import com.badlogic.androidgames.framework.impl.AndroidGame;
import com.badlogic.androidgames.framework.impl.AndroidScreen;
import com.google.android.gms.games.multiplayer.Participant;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PreMatchScreen extends AndroidScreen {

    private final Map<String, Short> orderedPlayers = new HashMap<>();

    private int readMessages = 0;
    private int phase = 0;
    private boolean repeatSendInit = true;
    private boolean repeatBroadcastInit = true;
    private volatile int countBroadcastInit = 0;

    private int time;

    private final GameMessageInterpreterImpl interpreter = new GameMessageInterpreterImpl();
    private final MyGoogleRoom myGoogleRoom;
    private final int numOpponents;

    private int[] skins;
    private int[] attacks;
    private int[][] spawnPositions;
    private String[] players;

    private ClientServerScreen nextScreen;
    private boolean start;
    private int playerOffset;

    public PreMatchScreen(AndroidGame androidGame, MyGoogleRoom myGoogleRoom) {
        super(androidGame);
        myGoogleRoom.setServerId(myGoogleRoom.getPlayerId());
        this.myGoogleRoom = myGoogleRoom;
        this.numOpponents = myGoogleRoom.getParticipants().size()-1;
        players = new String[numOpponents+1];
        skins = new int[numOpponents+1];
        attacks = new int[numOpponents+1];
        ArrayList<String> ids = myGoogleRoom.getParticipantIds();
        Collections.sort(ids);
        for(String s: ids) {
            if(s.equals(myGoogleRoom.getPlayerId()))
                playerOffset = orderedPlayers.size();
            orderedPlayers.put(s, (short) orderedPlayers.size());
        }
        androidGame.getGraphics().clear(Color.BLACK);
    }

    @Override
    public void update(float deltaTime) {
        switch (phase) {
            case 0:
                sendTime();
                break;
            case 1:
                choose();
                break;
            case 2:
                if(myGoogleRoom.isServer())
                    serverGetInit();
                else
                    sendInit();
                break;
            case 3:
                if(myGoogleRoom.isServer())
                    broadcastInit();
                else
                    clientGetInit();
                break;
            case 4:
                createMatchScreen();
                break;
            case 5:
                if(myGoogleRoom.isServer())
                    sendStart();
                else
                    receiveStart();
        }
    }

    private void sendStart() {
        if(nextScreen == null)
            return;
        NetworkMessageHandler handler = myGoogleRoom.getNetworkMessageHandler();
        GameMessage message = GameMessage.createInstance(); //TODO ogni volta new?
        interpreter.makeStartMessage(message); //TODO make start message
        handler.putInBuffer(message);
        handler.broadcastReliable();
        GameMessage.deleteInstance(message);
        //TODO devo aspettare?
        myGoogleRoom.getNetworkMessageHandler().setReceivers(new ServerMessageReceiver(interpreter, numOpponents+1), new ServerMessageReceiver(interpreter, numOpponents+1));
        androidGame.setScreen(nextScreen);
    }

    private void receiveStart() {
        for (GameMessage message : myGoogleRoom.getNetworkMessageHandler().getMessages()) {
            if(message.getType() == GameMessage.Type.START) {
                start = true;
                break;
            }
        }
        if(start) {
            androidGame.setScreen(nextScreen);
            myGoogleRoom.getNetworkMessageHandler().setReceivers(new ClientMessageReceiver(), new ClientMessageReceiver()); //TODO inutile
        }
    }

    private void createMatchScreen() {
        if(myGoogleRoom.isServer()) {
            nextScreen = new ServerScreen(androidGame, myGoogleRoom, players, skins, spawnPositions, attacks, playerOffset);
            Log.d("IOSOSERVER", "ISTANZIO nextScreen");
        } else {
            nextScreen = new ClientScreen(androidGame, myGoogleRoom, players, skins, spawnPositions, playerOffset);
            Log.d("IOSOCLIENT", "ISTANZIO nextScreen");
        }
        nextPhase();
    }

    private void broadcastInit() {
        if(!repeatBroadcastInit)
            return;
        repeatBroadcastInit = false;
        NetworkMessageHandlerImpl handler = myGoogleRoom.getNetworkMessageHandler();
        GameMessage message = GameMessage.createInstance();
        Log.d("HEYHEYHEY", Arrays.toString(skins));
        for(short i=0; i<numOpponents+1; i++) {
            interpreter.makeCreateMessage(message, i, spawnPositions[i][0], spawnPositions[i][1], skins[i]);
            handler.putInBuffer(message);
        }
        handler.broadcastReliable(new NetworkMessageHandlerImpl.OnComplete(){
            @Override
            public void work(Task<Integer> task) {
                if(task.isSuccessful()) {
                    countBroadcastInit++;
                    if(countBroadcastInit == numOpponents)
                        nextPhase();
                } else {
                    repeatBroadcastInit = true;
                }
            }
        });
        GameMessage.deleteInstance(message);
    }

    private void clientGetInit() {
        if(spawnPositions==null){
            spawnPositions = new int[numOpponents+1][2];
        }
        if(readMessages < numOpponents+1) {
            for(GameMessage message: myGoogleRoom.getNetworkMessageHandler().getMessages()) {
                Log.d("JUANNINO", message.getType().toString());
                if(message.getType() == GameMessage.Type.START) {
                    start = true;
                    continue;
                } else if(message.getType() != GameMessage.Type.CREATE) {
                    Log.d("ATTENZIONEATT", "devo saltare causa " + message.getType().toString());
                    continue;
                }
                int offset = interpreter.getObjectId(message);
                spawnPositions[offset][0] = (int)interpreter.getPosX(message);
                spawnPositions[offset][1] = (int)interpreter.getPosY(message);
                skins[offset] = interpreter.getSkinId(message);
                Log.d("HEYHEYHEY", "ho ricevuto " + offset + " = " + skins[offset]);
                players[offset] = message.getSender();
                readMessages++;
            }
        }
        if(readMessages > numOpponents)
            nextPhase();
    }

    private void serverGetInit() {
        if(readMessages == 0) {
            int offset = orderedPlayers.get(myGoogleRoom.getPlayerId());
            spawnPositions = distributePoints(game.getGraphics().getHeight()/2 - 80, game.getGraphics().getWidth()/2, game.getGraphics().getHeight() /2, numOpponents+1);
            players[offset] = myGoogleRoom.getPlayerId();
            skins[offset] = myGoogleRoom.getCurrentIdSkin();
            attacks[offset] = myGoogleRoom.getCurrentIdAttack();
            readMessages++;
        }
        if(readMessages < numOpponents+1) {
            for(GameMessage message: myGoogleRoom.getNetworkMessageHandler().getMessages()) {
                Log.d("HUAN", Arrays.toString(message.buffer));
                Log.d("JUANNINO", message.getType().toString());
                int offset = orderedPlayers.get(message.getSender());
                players[offset] = message.getSender();
                skins[offset] = interpreter.getInitClientSkinId(message);
                attacks[offset] = interpreter.getInitClientAttackId(message);
                readMessages++;
            }
        }
        if(readMessages > numOpponents)
            nextPhase();
    }

    private void nextPhase() {
        readMessages = 0;
        phase++;
    }

    private void sendTime() {
        long time = -System.currentTimeMillis();
        naivePrimeTest(500);
        time += System.currentTimeMillis();
        this.time = (int)time;

        NetworkMessageHandler handler = myGoogleRoom.getNetworkMessageHandler();
        GameMessage message = GameMessage.createInstance(); //TODO ogni volta new?
        interpreter.makeHostOrClientMessage(message, this.time);
        handler.putInBuffer(message);
        handler.broadcastReliable();
        GameMessage.deleteInstance(message);
        nextPhase();
    }

    private void naivePrimeTest(int k) {
        for(int i=2,j,n=1;n<k;i++) {
            for (j = 2; j < i; j++)
                if (i % j == 0)
                    break;
            if(i == j)
                n++;
        }
    }

    private void sendInit() {
        if(!repeatSendInit)
            return;
        repeatSendInit = false;
        GameMessage message = GameMessage.createInstance();
        interpreter.makeInitClientMessage(message, myGoogleRoom.getCurrentIdSkin(), (byte) myGoogleRoom.getCurrentIdAttack()); //TODO
        Log.d("HUAN","should be " + myGoogleRoom.getCurrentIdSkin() + " , " + myGoogleRoom.getCurrentIdAttack());
        Log.d("HUAN", Arrays.toString(message.buffer));
        NetworkMessageHandlerImpl handler = myGoogleRoom.getNetworkMessageHandler();
        handler.putInBuffer(message);
        handler.sendReliable(myGoogleRoom.getServerId(), new NetworkMessageHandlerImpl.OnComplete() {
            @Override
            public void work(Task<Integer> task) {
                if(task.isSuccessful())
                    nextPhase();
                else
                    repeatSendInit = true;
            }
        });
        GameMessage.deleteInstance(message);
    }

    private void choose() {
        if(readMessages < numOpponents) {
            for(GameMessage message: myGoogleRoom.getNetworkMessageHandler().getMessages()) {
                if(message.getType() == GameMessage.Type.START) {
                    start = true;
                    continue;
                }
                int itsTime = interpreter.getTimeMillis(message);
                if(itsTime < time || (itsTime == time && message.getSender().compareTo(myGoogleRoom.getServerId()) < 0)) {
                    time = itsTime;
                    myGoogleRoom.setServerId(message.getSender());
                }
                readMessages++;
            }
        }
        if(readMessages >= numOpponents) {
            if (myGoogleRoom.getPlayerId().equals(myGoogleRoom.getServerId()))
                Log.d("MAMMAMIA", "sono server");
            else
                Log.d("MAMMAMIA", "sono client");
            nextPhase();
        }
    }

    @Override
    public void present(float deltaTime) {
        androidGame.getGraphics().clear(Color.BLACK);
        androidGame.getGraphics().drawText("ASHPETTO " + "phase: "+phase+" readMessages: "+readMessages /*Math.random()*/,100,100,30,0xFFCA1111);
    }

    /**
     * Distribuzione di n cordinate equidistanti su di una circonferenza
     *
     * @param r raggio
     * @param w fattore di shift sull'asse x
     * @param h fattore di shift sull'asse y
     * @param n numero di giocatori
     * @return
     */
    private int[][] distributePoints(int r, int w, int h, int n){
        int[][] points = new int[n][2];
        double p = Math.PI*2/n;
        double theta = Math.PI/2;

        for(int i=0; i<n; i++){
            int x = (int)(Math.cos(theta)*r);
            int y = (int)(Math.sin(theta)*r);
            points[i][0] = x + w;
            points[i][1] = y + h;
            theta += p;
        }

        return points;
    }


    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public void back() {

    }
}
