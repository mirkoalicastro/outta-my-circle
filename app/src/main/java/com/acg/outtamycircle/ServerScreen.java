package com.acg.outtamycircle;

import android.graphics.Color;
import android.util.Log;

import com.acg.outtamycircle.entitycomponent.Component;
import com.acg.outtamycircle.entitycomponent.EntityFactory;
import com.acg.outtamycircle.entitycomponent.PositionComponent;
import com.acg.outtamycircle.entitycomponent.impl.GameCharacter;
import com.acg.outtamycircle.entitycomponent.impl.LiquidFunPhysicsComponent;
import com.acg.outtamycircle.physicsutilities.Converter;
import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.impl.AndroidGame;
import com.google.fpl.liquidfun.World;

public class ServerScreen extends ClientServerScreen {
    private final World world;

    private static final float TIME_STEP = 1 / 60f;   //60 fps
    private static final int VELOCITY_ITERATIONS = 8;
    private static final int POSITION_ITERATIONS = 3;

    public ServerScreen(AndroidGame game, long []ids) {
        super(game, ids);
        setup();

        world = new World(0, 0);
        EntityFactory.setWorld(world);

        /*Inizializzazione Giocatori*/
        GameCharacter[] characters = {
                EntityFactory.createServerDefaultCharacter(40, w/2, r/2 - 90, Color.GREEN),
                EntityFactory.createServerDefaultCharacter(40, w/2, r + r/2 + 90, Color.WHITE),
                EntityFactory.createServerDefaultCharacter(40, w/2 + r/2 + 90, h/2, Color.YELLOW),
                EntityFactory.createServerDefaultCharacter(40, w/2 - r/2 - 90, h/2, Color.RED),
        };
        status.setCharacters(characters);
    }


    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        LiquidFunPhysicsComponent comp = (LiquidFunPhysicsComponent)status.characters[0].getComponent(Component.Type.Physics);

        comp.move((float)androidJoystick.getNormX(), (float)androidJoystick.getNormY());

        world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS, 0);

        PositionComponent pos = (PositionComponent)status.characters[0].getComponent(Component.Type.Position);

        pos.x = (int)Converter.physicsToFrameX(comp.getX());
        pos.y = (int)Converter.physicsToFrameY(comp.getY());

        //TODO invia posizione
    }

    @Override
    public void setup(){

    }
}
