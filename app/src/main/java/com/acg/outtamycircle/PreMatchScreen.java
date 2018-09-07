package com.acg.outtamycircle;

import android.graphics.Color;
import android.util.Log;

import com.acg.outtamycircle.network.GameMessage;
import com.acg.outtamycircle.network.GameMessageInterpreterImpl;
import com.acg.outtamycircle.network.ServerClientMessageHandler;
import com.acg.outtamycircle.network.googleimpl.MyGoogleRoom;
import com.badlogic.androidgames.framework.impl.AndroidGame;
import com.badlogic.androidgames.framework.impl.AndroidScreen;

public class PreMatchScreen extends AndroidScreen {

    private int readMessages = 0;
    private int time;
    private String winnerId;
    private int phase = 0;
    private final GameMessageInterpreterImpl interpreter = new GameMessageInterpreterImpl();
    private final MyGoogleRoom myGoogleRoom;
    private final int numOpponents;
    private final int frameWeight = game.getGraphics().getWidth();
    private final int frameHeight = game.getGraphics().getHeight();
    private final int arenaRadius = frameHeight/2 - 40;
    private String[] players;
    private short[] skins;
    private byte[] attacks;
    private int[][] positions;

    public PreMatchScreen(AndroidGame androidGame, MyGoogleRoom myGoogleRoom) {
        super(androidGame);
        this.winnerId = myGoogleRoom.getPlayerId();
        this.myGoogleRoom = myGoogleRoom;
        this.numOpponents = myGoogleRoom.getRoom().getParticipants().size()-1;
        players = new String[numOpponents+1];
        skins = new short[numOpponents+1];
        attacks = new byte[numOpponents+1];
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
        }
    }

    private void broadcastInit() {
        Log.d("FINE", "broadcast");
    }

    private void clientGetInit() {
        Log.d("FINE", "client");
    }

    private void serverGetInit() {
        if(readMessages < numOpponents+1) {
            if(readMessages == 0) {
                positions = distributePoints(arenaRadius -40, frameWeight/2, frameHeight /2, numOpponents+1);
                players[0] = myGoogleRoom.getPlayerId();
                skins[0] = myGoogleRoom.getCurrentIdSkin();
                attacks[0] = myGoogleRoom.getCurrentIdAttack();
                readMessages++;
            }
            for(GameMessage message: myGoogleRoom.getServerClientMessageHandler().getMessages()) {
                players[readMessages] = message.getSender();
                skins[readMessages] = interpreter.getInitClientSkinId(message);
                attacks[readMessages] = interpreter.getInitClientAttackId(message);
                readMessages++;
            }
        }
        if(readMessages >= numOpponents) {
            readMessages = 0;
            phase++;
        }
    }

    private void sendTime() {
        long time = -System.currentTimeMillis();
        naivePrimeTest(500);
        time += System.currentTimeMillis();
        this.time = (int)time;

        ServerClientMessageHandler handler = myGoogleRoom.getServerClientMessageHandler();
        GameMessage gameMessage = new GameMessage(); //TODO ogni volta new?

        interpreter.makeHostOrClientMessage(gameMessage, this.time);
        handler.putInBuffer(gameMessage);
        handler.broadcastReliable();
        phase++;
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

        GameMessage gameMessage = new GameMessage();
        interpreter.makeInitClientMessage(gameMessage, myGoogleRoom.getCurrentIdSkin(), (byte) myGoogleRoom.getCurrentIdAttack()); //TODO
        ServerClientMessageHandler handler = myGoogleRoom.getServerClientMessageHandler();

        handler.putInBuffer(gameMessage);
        handler.sendReliable(myGoogleRoom.getServerId());
        phase++;
    }

    private void choose() {
        if(readMessages == 0) {
            Log.d("AGLIO", "Io sono " + time + " con id " + winnerId + " <=> " + myGoogleRoom.getPlayerId());
        }
        if(readMessages < numOpponents) {
            for(GameMessage message: myGoogleRoom.getServerClientMessageHandler().getMessages()) {
                int itsTime = interpreter.getTimeMillis(message);
                Log.d("AGLIO", "Ricevo " + itsTime + " da  " + message.getSender() + " (rispetto a " + time + ")");
                if(itsTime < time) {
                    time = itsTime;
                    winnerId = message.getSender();
                }
                readMessages++;
            }
        }
        if(readMessages >= numOpponents) {
            if (winnerId.equals(myGoogleRoom.getPlayerId())) {
                Log.d("MAMMAMIA", "sono server");
            } else {
                Log.d("MAMMAMIA", "sono client");
            }
            myGoogleRoom.setServerId(winnerId);
            phase++;
            readMessages = 0;
        }
    }

    @Override
    public void present(float deltaTime) {
        androidGame.getGraphics().clear(Color.BLACK);
        androidGame.getGraphics().drawText("ASHPETTO " + Math.random(),100,100,30,0xFFCA1111);
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
