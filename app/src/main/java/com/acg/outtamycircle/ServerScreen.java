package com.acg.outtamycircle;

import android.graphics.Color;

import com.acg.outtamycircle.entitycomponent.Component;
import com.acg.outtamycircle.entitycomponent.DrawableComponent;
import com.acg.outtamycircle.entitycomponent.EntityFactory;
import com.acg.outtamycircle.entitycomponent.impl.CircleDrawableComponent;
import com.acg.outtamycircle.entitycomponent.impl.DynamicCircle;
import com.acg.outtamycircle.entitycomponent.impl.GameCharacter;
import com.acg.outtamycircle.entitycomponent.impl.LiquidFunPhysicsComponent;
import com.acg.outtamycircle.physicsutilities.Converter;
import com.badlogic.androidgames.framework.impl.AndroidGame;
import com.google.fpl.liquidfun.World;

public class ServerScreen extends ClientServerScreen {
    private final World world;

    private static final float TIME_STEP = 1 / 60f;   //60 fps
    private static final int VELOCITY_ITERATIONS = 8;
    private static final int POSITION_ITERATIONS = 3;

    private int[][] spawnPositions;

    public ServerScreen(AndroidGame game, long []ids) {
        super(game, ids);

        world = new World(0, 0);
        EntityFactory.setWorld(world);

        /*Inizializzazione Giocatori*/
        spawnPositions = distributePoints(arenaRadius -40, frameWeight/2, frameHeight /2, 4);

        GameCharacter[] characters = {
                EntityFactory.createServerDefaultCharacter(40, spawnPositions[0][0], spawnPositions[0][1], Color.GREEN),
                EntityFactory.createServerDefaultCharacter(40, spawnPositions[1][0], spawnPositions[1][1], Color.WHITE),
                EntityFactory.createServerDefaultCharacter(40, spawnPositions[2][0], spawnPositions[2][1], Color.YELLOW),
                EntityFactory.createServerDefaultCharacter(40, spawnPositions[3][0], spawnPositions[3][1], Color.RED),
        };
        status.setCharacters(characters);

        //TODO comunica posizioni etc.
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        LiquidFunPhysicsComponent comp = (LiquidFunPhysicsComponent)status.characters[0].getComponent(Component.Type.Physics);

        comp.move((float)androidJoystick.getNormX(), (float)androidJoystick.getNormY());

        //TODO deltaTime?
        world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS, 0);

        for(int i=0; i<status.characters.length; i++) {
            comp = (LiquidFunPhysicsComponent)status.characters[i].getComponent(Component.Type.Physics);

            DrawableComponent shape = (DrawableComponent)status.characters[i].getComponent(Component.Type.Drawable);

            shape.setX((int) Converter.physicsToFrameX(comp.getX()))
                    .setY((int) Converter.physicsToFrameY(comp.getY()));
        }

        checkStatus();

        //TODO invia posizione
    }

    @Override
    public void setup(){
        Converter.setScale(frameWeight, frameHeight);
    }

    /*TODO Non serve*/
    @Override
    public void present(float deltaTime){
        super.present(deltaTime);
        //world.setContactListener(new ContactHandler());
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

    private void checkStatus(){
        for(int i=0 ; i<spawnPositions.length ; i++)
            if(isOut(status.characters[i]))
                status.alives[i] = false;
    }

    private boolean isOut(GameCharacter ch1){
        CircleDrawableComponent circle = (CircleDrawableComponent)ch1.getComponent(Component.Type.Physics);
        float chX = circle.getX();
        float chY = circle.getY();

        DrawableComponent arenaDrawable = (DrawableComponent)status.arena.getComponent(Component.Type.Drawable);
        float arenaX = Converter.frameToPhysicsX(arenaDrawable.getX());
        float arenaY = Converter.frameToPhysicsY(arenaDrawable.getY());

        float deltaX = (chX - arenaX)*(chX - arenaX);
        float deltaY = (chY - arenaY)*(chY- arenaY);

        float delta = (float)Math.sqrt(deltaX + deltaY);
        return delta > arenaRadius + circle.getRadius();
    }
}