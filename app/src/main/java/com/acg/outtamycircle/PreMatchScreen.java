package com.acg.outtamycircle;

import com.acg.outtamycircle.network.GameMessage;
import com.acg.outtamycircle.network.GameMessageInterpreterImpl;
import com.acg.outtamycircle.network.NetworkMessageHandler;
import com.acg.outtamycircle.network.NetworkMessageHandlerImpl;
import com.acg.outtamycircle.network.googleimpl.ClientMessageReceiver;
import com.acg.outtamycircle.network.googleimpl.MyGoogleRoom;
import com.acg.outtamycircle.network.googleimpl.ServerMessageReceiver;
import com.badlogic.androidgames.framework.Animation;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.impl.AndroidGame;
import com.badlogic.androidgames.framework.impl.AndroidScreen;
import com.badlogic.androidgames.framework.impl.SpinAnimation;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PreMatchScreen extends AndroidScreen {

    private static final long MAX_TIME = 7000;
    private final Map<String, Integer> orderedPlayers = new HashMap<>();

    private int readMessages = 0;
    private int phase = 0;
    private boolean repeatSendInit = true;
    private boolean repeatBroadcastInit = true;
    private final int[] countBroadcastInit = new int[]{0};

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
    private final Animation loadingAnimation;
    private final long startAt;

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
            orderedPlayers.put(s, orderedPlayers.size());
        }
        Graphics graphics = game.getGraphics();
        loadingAnimation = new SpinAnimation(graphics, 15, 50)
                .setX(graphics.getWidth()/2 - Assets.wait.getWidth()/2)
                .setY(graphics.getHeight()/2 - Assets.wait.getHeight()/2)
                .setPixmap(Assets.wait);
        startAt = System.currentTimeMillis();
    }

    @Override
    public void update(float deltaTime) {
        if(tooMuchTime())
            privateBack();

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

    private void privateBack() {
        myGoogleRoom.error();
    }

    private boolean tooMuchTime() {
        return System.currentTimeMillis() - startAt > MAX_TIME;
    }

    private void sendStart() {
        NetworkMessageHandler handler = myGoogleRoom.getNetworkMessageHandler();
        GameMessage message = GameMessage.createInstance();
        interpreter.makeStartMessage(message);
        handler.putInBuffer(message);
        handler.broadcastReliable();
        GameMessage.deleteInstance(message);

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
            myGoogleRoom.getNetworkMessageHandler().setReceivers(new ClientMessageReceiver(), new ClientMessageReceiver());
        }
    }

    private void createMatchScreen() {
        if(myGoogleRoom.isServer())
            nextScreen = new ServerScreen(androidGame, myGoogleRoom, players, skins, spawnPositions, attacks, playerOffset);
        else
            nextScreen = new ClientScreen(androidGame, myGoogleRoom, players, skins, spawnPositions, playerOffset);
        nextPhase();
    }

    private void broadcastInit() {
        if(!repeatBroadcastInit)
            return;
        repeatBroadcastInit = false;
        NetworkMessageHandlerImpl handler = myGoogleRoom.getNetworkMessageHandler();
        GameMessage message = GameMessage.createInstance();
        for(int i=0; i<numOpponents+1; i++) {
            interpreter.makeCreateMessage(message, i, spawnPositions[i][0], spawnPositions[i][1], skins[i]);
            handler.putInBuffer(message);
        }
        handler.broadcastReliable(new NetworkMessageHandlerImpl.OnComplete(){
            @Override
            public void work(Task<Integer> task) {
                boolean nextPhase = false;
                synchronized (countBroadcastInit) {
                    if (task.isSuccessful()) {
                        countBroadcastInit[0]++;
                        if (countBroadcastInit[0] == numOpponents)
                            nextPhase = true;
                    } else {
                        repeatBroadcastInit = true;
                    }
                }
                if (nextPhase)
                    nextPhase();
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
                if(message.getType() == GameMessage.Type.START) {
                    start = true;
                    continue;
                } else if(message.getType() != GameMessage.Type.CREATE)
                    continue;
                int offset = interpreter.getObjectId(message);
                spawnPositions[offset][0] = interpreter.getPosX(message);
                spawnPositions[offset][1] = interpreter.getPosY(message);
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
            for(GameMessage message: myGoogleRoom.getNetworkMessageHandler().getMessages()) {
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
        GameMessage message = GameMessage.createInstance();
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
        interpreter.makeInitClientMessage(message, myGoogleRoom.getCurrentIdSkin(), myGoogleRoom.getCurrentIdAttack());
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
        if(readMessages >= numOpponents)
            nextPhase();
    }

    @Override
    public void present(float deltaTime) {
        Graphics graphics = androidGame.getGraphics();
        graphics.drawEffect(Assets.backgroundTile, 0,0, graphics.getWidth(), graphics.getHeight());
        loadingAnimation.draw();
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
