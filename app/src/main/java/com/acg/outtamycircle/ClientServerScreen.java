package com.acg.outtamycircle;

import android.graphics.Color;
import android.util.Log;

import com.acg.outtamycircle.entitycomponent.Component;
import com.acg.outtamycircle.entitycomponent.DrawableComponent;
import com.acg.outtamycircle.entitycomponent.EntityFactory;
import com.acg.outtamycircle.entitycomponent.impl.DynamicCircle;
import com.acg.outtamycircle.entitycomponent.impl.GameCharacter;
import com.acg.outtamycircle.entitycomponent.impl.LiquidFunPhysicsComponent;
import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input;
import com.badlogic.androidgames.framework.Joystick;
import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.impl.AndroidGame;
import com.badlogic.androidgames.framework.impl.AndroidJoystick;
import com.badlogic.androidgames.framework.impl.AndroidScreen;
import com.badlogic.androidgames.framework.impl.TimedCircularButton;
import com.google.fpl.liquidfun.World;

import java.util.List;

public abstract class ClientServerScreen extends AndroidScreen {
    protected GameStatus status;
    protected int h, w, r; //height, width, radius

    private final Graphics graphics = game.getGraphics();
    private final TimedCircularButton timedCircularButton = new TimedCircularButton(2000,1080,580,100);

    protected int[][] spawnPositions;

    /*La cattura degli eventi è equivalente in client e server,
     ma va processata in maniera differente*/
    protected List<Input.TouchEvent> events;
    protected final Joystick androidJoystick = new AndroidJoystick(game.getInput(),200,580,100);

    public ClientServerScreen(AndroidGame game, long[] ids) {
        super(game);
        setup();
        status = new GameStatus();

        EntityFactory.setGraphics(game.getGraphics());

        h = graphics.getHeight();
        w = graphics.getWidth();
        r = h/2 - 40;

        status.setArena(EntityFactory.createArena(r, w/2, h/2));


        spawnPositions = getSpawnPositions(r-40, w/2, h/2, 4);
    }

    @Override
    public void present(float deltaTime) {
        //TODO
        Graphics g = game.getGraphics(); //se lo sfondo è un gameobject si può separare graphics da screen
        g.drawTile(Assets.backgroundTile, 0,0, g.getWidth(), g.getHeight()); //inutile?

        DrawableComponent arenaDrawable = (DrawableComponent) status.arena.getComponent(Component.Type.Drawable);
        arenaDrawable.draw();

        drawCharacters();

        androidJoystick.draw(graphics, Color.GREEN);
        timedCircularButton.draw(g, Color.GREEN);
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
                if (timedCircularButton.isActive()) {
                    Log.d("TCB", "WOW ATTACKK WHOAAAA");
                }
            }
        }
    }

    private void drawCharacters(){
        for(int i=0 ; i<status.characters.length ; i++)
            ((DrawableComponent)status.characters[i].getComponent(Component.Type.Drawable)).draw();
    }

    /**
     * Calcolo delle posizioni di spawn dei giocatori
     *
     * @param r raggio
     * @param w fattore di shift sull'asse x
     * @param h fattore di shift sull'asse y
     * @param n numero di giocatori
     * @return
     */
    private int[][] getSpawnPositions(int r, int w, int h, int n){
        int[][] spwns = new int[n][2];
        double x, y;
        double p = (Math.PI*2)/n;
        double theta = Math.PI/2;

        System.out.println("TAG: "+" W:"+w+" H: "+h);
        for(int i=0 ; i<n ; i++){
            x = Math.cos(theta)*r;
            y = Math.sin(theta)*r;


            spwns[i][0] = (int)x + w;
            spwns[i][1] = (int)y + h;

            theta += p;
        }



        return spwns;
    }
}
