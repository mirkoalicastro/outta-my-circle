package com.acg.outtamycircle;

import android.graphics.Color;
import android.util.Log;

import com.acg.outtamycircle.network.GameMessage;
import com.acg.outtamycircle.network.GameMessageInterpreterImpl;
import com.acg.outtamycircle.network.ServerClientMessageHandler;
import com.badlogic.androidgames.framework.impl.AndroidGame;
import com.badlogic.androidgames.framework.impl.AndroidScreen;

public class ChooseClientOrServerScreen extends AndroidScreen {

    private int readMessages = 0;
    private int time;
    private final int numOpponents;
    private final String playerId;
    private String winnerId;
    private final ServerClientMessageHandler handler;

    public ChooseClientOrServerScreen(AndroidGame androidGame, ServerClientMessageHandler handler, int time, String playerId, int numOpponents) {
        super(androidGame);
        this.time = time;
        this.numOpponents = numOpponents;
        this.playerId = playerId;
        this.winnerId = playerId;
        this.handler = handler;
        androidGame.getGraphics().clear(Color.BLACK);
    }

    @Override
    public void update(float deltaTime) {
        if(readMessages < numOpponents) {
            Log.d("JUAN", "aspetto");
            for(GameMessage message: handler.getMessages()) {
                int itsTime = new GameMessageInterpreterImpl().getTimeMillis(message);
                Log.d("JUAN", "Leggo " + itsTime + " da " + message.getSender());
                if(itsTime < time) {
                    time = itsTime;
                    winnerId = message.getSender();
                }
                readMessages++;
            }
        }
        if(readMessages >= numOpponents) {
            if (winnerId.equals(playerId)) {
                Log.d("MAMMAMIA", "sono server");
                androidGame.setScreen(new ServerScreen(androidGame, new long[]{0}));
            } else {
                Log.d("MAMMAMIA", "sono client");
                androidGame.setScreen(new ServerScreen(androidGame, new long[]{0}));
            }
        }
    }

    @Override
    public void present(float deltaTime) {
        androidGame.getGraphics().drawText("ASHPETTO " + Math.random(),100,100,30,0xFFCA1111);
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
