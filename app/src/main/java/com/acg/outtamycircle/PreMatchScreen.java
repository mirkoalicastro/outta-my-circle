package com.acg.outtamycircle;

import android.graphics.Color;
import android.util.Log;

import com.acg.outtamycircle.network.GameMessage;
import com.acg.outtamycircle.network.GameMessageInterpreterImpl;
import com.acg.outtamycircle.network.NetworkMessageHandler;
import com.acg.outtamycircle.network.googleimpl.ClientMessageReceiver;
import com.acg.outtamycircle.network.googleimpl.MyGoogleRoom;
import com.acg.outtamycircle.network.googleimpl.ServerMessageReceiver;
import com.badlogic.androidgames.framework.impl.AndroidGame;
import com.badlogic.androidgames.framework.impl.AndroidScreen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PreMatchScreen extends AndroidScreen {

    private final Map<String, Short> orderedPlayers = new HashMap<>();

    private int readMessages = 0;
    private int phase = 0;

    private int time;
    private String winnerId;

    private final GameMessageInterpreterImpl interpreter = new GameMessageInterpreterImpl();
    private final MyGoogleRoom myGoogleRoom;
    private final int numOpponents;

    private byte[] skins;
    private byte[] attacks;
    private int[][] spawnPositions;
    private String[] players;

    private ClientServerScreen nextScreen;
    private boolean start;

    public PreMatchScreen(AndroidGame androidGame, MyGoogleRoom myGoogleRoom) {
        super(androidGame);
        this.winnerId = myGoogleRoom.getPlayerId();
        this.myGoogleRoom = myGoogleRoom;
        this.numOpponents = myGoogleRoom.getRoom().getParticipants().size()-1;
        players = new String[numOpponents+1];
        skins = new byte[numOpponents+1];
        attacks = new byte[numOpponents+1];
        ArrayList<String> ids = myGoogleRoom.getRoom().getParticipantIds();
        Collections.sort(ids);
        for(String s: ids)
            orderedPlayers.put(s,(short)orderedPlayers.size());
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
        NetworkMessageHandler handler = myGoogleRoom.getNetworkMessageHandlerImpl();
        GameMessage message = GameMessage.createInstance(); //TODO ogni volta new?
        interpreter.makeStartMessage(message); //TODO make start message
        handler.putInBuffer(message);
        handler.broadcastReliable();
        GameMessage.deleteInstance(message);
        //TODO devo aspettare?
        androidGame.setScreen(nextScreen);
    }

    private void receiveStart() {
        for (GameMessage message : myGoogleRoom.getNetworkMessageHandlerImpl().getMessages()) {
            if(message.getType() == GameMessage.Type.START) {
                start = true;
                break;
            }
        }
        if(start)
            androidGame.setScreen(nextScreen);
    }

    private void createMatchScreen() {
        if(myGoogleRoom.isServer()) {
            myGoogleRoom.getNetworkMessageHandlerImpl().setReceivers(new ServerMessageReceiver(interpreter, numOpponents+1), new ServerMessageReceiver(interpreter, numOpponents+1));
            nextScreen = new ServerScreen(androidGame, myGoogleRoom, players, skins, spawnPositions, attacks);
        } else {
            myGoogleRoom.getNetworkMessageHandlerImpl().setReceivers(new ClientMessageReceiver(), new ClientMessageReceiver()); //TODO inutile?
            nextScreen = new ClientScreen(androidGame, myGoogleRoom, players, skins, spawnPositions);
        }
        nextPhase();
    }

    private void broadcastInit() {
        NetworkMessageHandler handler = myGoogleRoom.getNetworkMessageHandlerImpl();
        for(short i=0; i<numOpponents+1; i++) {
            GameMessage message = GameMessage.createInstance(); //TODO new??
            interpreter.makeCreateMessage(message, i, spawnPositions[i][0], spawnPositions[i][1], skins[i]);
            handler.putInBuffer(message);
            handler.broadcastReliable(); //TODO carico tutto e poi faccio un broadcast?
            GameMessage.deleteInstance(message);
        }
        nextPhase();
    }

    private void clientGetInit() {
        if(spawnPositions==null){
            spawnPositions = new int[numOpponents+1][2];
        }
        if(readMessages < numOpponents+1) {
            for(GameMessage message: myGoogleRoom.getNetworkMessageHandlerImpl().getMessages()) {
                Log.d("JUANNINO", message.getType().toString());
                if(message.getType() == GameMessage.Type.START) {
                    start = true;
                    continue;
                }
                int offset = interpreter.getObjectId(message);
                spawnPositions[offset][0] = (int)interpreter.getPosX(message);
                spawnPositions[offset][1] = (int)interpreter.getPosY(message);
                skins[offset] = interpreter.getSkinId(message);
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
            for(GameMessage message: myGoogleRoom.getNetworkMessageHandlerImpl().getMessages()) {
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

        NetworkMessageHandler handler = myGoogleRoom.getNetworkMessageHandlerImpl();
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

        GameMessage message = GameMessage.createInstance();
        interpreter.makeInitClientMessage(message, myGoogleRoom.getCurrentIdSkin(), (byte) myGoogleRoom.getCurrentIdAttack()); //TODO
        Log.d("HUAN","should be " + myGoogleRoom.getCurrentIdSkin() + " , " + myGoogleRoom.getCurrentIdAttack());
        Log.d("HUAN", Arrays.toString(message.buffer));
        NetworkMessageHandler handler = myGoogleRoom.getNetworkMessageHandlerImpl();
        handler.putInBuffer(message);
        handler.sendReliable(myGoogleRoom.getServerId());
        GameMessage.deleteInstance(message);

        nextPhase();
    }

    private void choose() {
        if(readMessages == 0) {
            Log.d("AGLIO", "Io sono " + time + " con id " + winnerId + " <=> " + myGoogleRoom.getPlayerId());
        }
        if(readMessages < numOpponents) {
            for(GameMessage message: myGoogleRoom.getNetworkMessageHandlerImpl().getMessages()) {
                Log.d("JUANNINO", message.getType().toString());
                if(message.getType() == GameMessage.Type.START) {
                    start = true;
                    continue;
                }
                int itsTime = interpreter.getTimeMillis(message);
                if(itsTime < time || (itsTime == time && message.getSender().compareTo(winnerId) < 0)) {
                    time = itsTime;
                    winnerId = message.getSender();
                }
                readMessages++;
            }
        }
        if(readMessages >= numOpponents) {
            if (winnerId.equals(myGoogleRoom.getPlayerId()))
                Log.d("MAMMAMIA", "sono server");
            else
                Log.d("MAMMAMIA", "sono client");
            myGoogleRoom.setServerId(winnerId);
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
        double x, y;
        double p = (Math.PI*2)/n;
        double theta = Math.PI/2;

        for(int i=0 ; i<n ; i++){
            x = Math.cos(theta)*r;
            y = Math.sin(theta)*r;


            points[i][0] = (int)x + w;
            points[i][1] = (int)y + h;

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
