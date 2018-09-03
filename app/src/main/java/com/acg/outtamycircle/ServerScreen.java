package com.acg.outtamycircle;

import android.graphics.Color;
import android.util.Log;

import com.acg.outtamycircle.entitycomponent.Component;
import com.acg.outtamycircle.entitycomponent.DrawableComponent;
import com.acg.outtamycircle.entitycomponent.EntityFactory;
import com.acg.outtamycircle.entitycomponent.impl.BoundsTest;
import com.acg.outtamycircle.entitycomponent.impl.GameCharacter;
import com.acg.outtamycircle.entitycomponent.impl.LiquidFunPhysicsComponent;
import com.acg.outtamycircle.physicsutilities.Converter;
import com.badlogic.androidgames.framework.impl.AndroidGame;
import com.google.fpl.liquidfun.World;

import java.util.Calendar;

public class ServerScreen extends ClientServerScreen {
    private final World world;

    private BoundsTest bt;

    private static final float TIME_STEP = 1 / 60f;   //60 fps
    private static final int VELOCITY_ITERATIONS = 8;
    private static final int POSITION_ITERATIONS = 3;

    public ServerScreen(AndroidGame game, long []ids) {
        super(game, ids);
        setup();

        world = new World(0, 0);
        EntityFactory.setWorld(world);

        /*Testcode*/
        //bt = new BoundsTest(graphics, world, r+ 10, w/2, h/2);

        /*Inizializzazione Giocatori*/
        GameCharacter[] characters = {
                EntityFactory.createServerDefaultCharacter(40, spawnPositions[0][0], spawnPositions[0][1], Color.GREEN),
                EntityFactory.createServerDefaultCharacter(40, spawnPositions[1][0], spawnPositions[1][1], Color.WHITE),
                EntityFactory.createServerDefaultCharacter(40, spawnPositions[2][0], spawnPositions[2][1], Color.YELLOW),
                EntityFactory.createServerDefaultCharacter(40, spawnPositions[3][0], spawnPositions[3][1], Color.RED),
        };
        status.setCharacters(characters);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        LiquidFunPhysicsComponent comp = (LiquidFunPhysicsComponent)status.characters[0].getComponent(Component.Type.Physics);

        comp.move((float)androidJoystick.getNormX(), (float)androidJoystick.getNormY());

        //TODO deltaTime
        world.step(deltaTime, VELOCITY_ITERATIONS, POSITION_ITERATIONS, 0);

        for(int i=0; i<status.characters.length; i++) {
            comp = (LiquidFunPhysicsComponent)status.characters[i].getComponent(Component.Type.Physics);

            DrawableComponent shape = (DrawableComponent)status.characters[i].getComponent(Component.Type.Drawable);

            shape.setX((int) Converter.physicsToFrameX(comp.getX()))
                    .setY((int) Converter.physicsToFrameY(comp.getY()));
        }

        //TODO invia posizione
    }

    @Override
    public void setup(){
        Converter.setScale(w, h);
    }

  //  int num = 0;
  //  double sum = 0;
    /*TODO Non serve*/
    @Override
    public void present(float deltaTime){
  //      long start = Calendar.getInstance().getTimeInMillis();
        super.present(deltaTime);
  /*      sum += Calendar.getInstance().getTimeInMillis()-start;
        num++;
        Log.d("ServerScreen","vale " + (sum/num) + " (" + num + ")");*/

        // world.setContactListener(new ContactHandler());
    }
}