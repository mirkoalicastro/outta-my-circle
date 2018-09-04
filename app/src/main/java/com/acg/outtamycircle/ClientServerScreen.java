package com.acg.outtamycircle;

import android.graphics.Color;
import android.graphics.Shader;
import android.util.Log;

import com.acg.outtamycircle.entitycomponent.Component;
import com.acg.outtamycircle.entitycomponent.DrawableComponent;
import com.acg.outtamycircle.entitycomponent.EntityFactory;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input;
import com.badlogic.androidgames.framework.impl.AndroidGame;
import com.badlogic.androidgames.framework.impl.AndroidJoystick;
import com.badlogic.androidgames.framework.impl.AndroidScreen;
import com.badlogic.androidgames.framework.impl.RadialGradientEffect;
import com.badlogic.androidgames.framework.impl.TimedCircularButton;

import java.util.List;

public abstract class ClientServerScreen extends AndroidScreen {
    protected GameStatus status;
    protected int frameHeight, frameWeight, arenaRadius; //height, width, radius

    private final TimedCircularButton timedCircularButton = new TimedCircularButton(androidGame.getGraphics(),1080,520,100,2000);

    protected int[][] spawnPositions;

    /*La cattura degli eventi è equivalente in client e server,
     ma va processata in maniera differente*/
    protected List<Input.TouchEvent> events;

    protected final AndroidJoystick androidJoystick = new AndroidJoystick(androidGame.getGraphics(),200,520,100);

    public ClientServerScreen(AndroidGame game, long[] ids) {
        super(game);
        androidJoystick.setSecondaryColor(Settings.WHITE50ALFA)
                .setEffect(new RadialGradientEffect(androidJoystick.getX(),androidJoystick.getY(),androidJoystick.getRadius(),
                        new int[]{Settings.INTERNAL_GRADIENT, Settings.EXTERNAL_GRADIENT},
                        new float[]{0f,1f}, Shader.TileMode.CLAMP))
                .setColor(Settings.DKGRAY).setStroke(15,Color.BLACK);

        timedCircularButton.setSecondaryColor(Settings.DKRED)
                .setSecondaryPixmap(Assets.swordsWhite)
                .setPixmap(Assets.swordsBlack)
                .setColor(Settings.DKGREEN)
                .setStroke(15, Color.BLACK);

        frameHeight = game.getGraphics().getHeight();
        frameWeight = game.getGraphics().getWidth();
        arenaRadius = frameHeight /2 - 40;

        setup();
        status = new GameStatus();

        EntityFactory.setGraphics(game.getGraphics());

        status.setArena(EntityFactory.createArena(arenaRadius, frameWeight/2, frameHeight /2));

        spawnPositions = getSpawnPositions(arenaRadius-40, frameWeight/2, frameHeight /2, 4);
    }

    @Override
    public void present(float deltaTime) {
        //TODO
        Graphics g = game.getGraphics(); //se lo sfondo è un gameobject si può separare graphics da screen
        g.drawEffect(Assets.backgroundTile, 0,0, g.getWidth(), g.getHeight()); //inutile?

        DrawableComponent arenaDrawable = (DrawableComponent) status.arena.getComponent(Component.Type.Drawable);
        arenaDrawable.draw();

        drawCharacters();

        androidJoystick.draw();
        timedCircularButton.draw();
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

    public abstract void setup();

    public void update(float deltaTime) {
        events = androidJoystick.processAndRelease(game.getInput().getTouchEvents());

        for (Input.TouchEvent event : events) {
            if (timedCircularButton.inBounds(event)) {
                Log.d("TCB", "PRESSED");
                if (timedCircularButton.isEnabled()) {
                    Log.d("TCB", "WOW ATTACKK WHOAAAA");

                    timedCircularButton.resetTime();
                }
            }
        }
    }

    //TODO così il personaggio sparisce improvvisamente, inoltre continua ad esserci il corpo fisico
    private void drawCharacters(){
        for(int i=0 ; i<status.characters.length ; i++)
            if(status.alives[i])
                ((DrawableComponent)status.characters[i].getComponent(Component.Type.Drawable)).draw();
    }

    /**
     * Calcolo delle posizioni di spawn dei giocatori
     *
     * @param arenaRadius raggio
     * @param frameWeight fattore di shift sull'asse x
     * @param h fattore di shift sull'asse y
     * @param n numero di giocatori
     * @return
     */
    private int[][] getSpawnPositions(int arenaRadius, int frameWeight, int h, int n){
        int[][] spwns = new int[n][2];
        double x, y;
        double p = (Math.PI*2)/n;
        double theta = Math.PI/2;
        Log.d("TAG","frameWeight:" + frameWeight + " H: " + h);
        for(int i=0 ; i<n ; i++){
            x = Math.cos(theta)*arenaRadius;
            y = Math.sin(theta)*arenaRadius;
            spwns[i][0] = (int)x + frameWeight;
            spwns[i][1] = (int)y + h;
            theta += p;
        }
        return spwns;
    }
}
