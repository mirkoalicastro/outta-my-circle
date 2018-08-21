package com.acg.outtamycircle;

import android.graphics.Color;

import com.acg.outtamycircle.entitycomponent.Component;
import com.acg.outtamycircle.entitycomponent.EntityFactory;
import com.acg.outtamycircle.entitycomponent.impl.GameCharacter;
import com.acg.outtamycircle.entitycomponent.impl.LiquidFunPhysicsComponent;
import com.badlogic.androidgames.framework.Game;
import com.google.fpl.liquidfun.World;

public class ServerScreen extends ClientServerScreen {
    private final World world;

    public ServerScreen(Game game, long []ids) {
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

        //world.step(deltaTime?, 8, 3);

        //chiedi a body la posizione
        //aggiorna position
        //invia position

    }

    @Override
    public void setup(){

    }
}
