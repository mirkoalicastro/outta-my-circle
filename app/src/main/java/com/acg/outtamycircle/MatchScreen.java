package com.acg.outtamycircle;

import android.graphics.Color;
import android.util.Log;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input;
import com.badlogic.androidgames.framework.JoyStick;
import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.impl.AndroidJoyStick;
import com.acg.outtamycircle.entitycomponent.Component;
import com.acg.outtamycircle.entitycomponent.DrawableComponent;
import com.acg.outtamycircle.entitycomponent.EntityFactory;
import com.acg.outtamycircle.entitycomponent.impl.Arena;
import com.google.fpl.liquidfun.World;
import com.acg.outtamycircle.entitycomponent.impl.Character;

import java.util.List;

public class MatchScreen extends Screen {
    private final Graphics graphics = game.getGraphics(); //TODO lo vogliamo mettere in Screen?
    /*120 - 600*/
    private final JoyStick androidJoyStick = new AndroidJoyStick(game.getInput(),150,550,50);
    private final State state;

    private class State { //TODO gamestatus
        Arena arena;
        Character[] characters;

        void setArena(Arena arena){
            this.arena = arena;
        }

        void setCharacters(Character[] characters) {
            this.characters = characters;
        }

        void drawCharacters(){
            for(int i=0 ; i<characters.length ; i++)
                ((DrawableComponent)characters[i].getComponent(Component.Type.Drawable)).draw();
        }
    }

    public MatchScreen(Game game){
        super(game);
        state = new State();
        //chiedi a room lo stato (i giocatori)
        EntityFactory.setWorld(new World(0, 0));
        EntityFactory.setGraphics(game.getGraphics());

        int h = graphics.getHeight();
        int w = graphics.getWidth();
        int r = h/2 -10;

        /*Inizializzazione Arena*/
        state.setArena(EntityFactory.createArena(r, w/2, h/2));

        /*Inizializzazione Giocatori*/
        Character[] characters = {
                EntityFactory.createDefaultCharacter(40, w/2, r/2 - 90, Color.GREEN),
                EntityFactory.createDefaultCharacter(40, w/2, r + r/2 + 90, Color.WHITE),
                EntityFactory.createDefaultCharacter(40, w/2 + r/2 + 90, h/2, Color.YELLOW),
                EntityFactory.createDefaultCharacter(40, w/2 - r/2 - 90, h/2, Color.RED),
        };
        state.setCharacters(characters);
    }

    @Override
    public void update(float deltaTime) {

        //world.step(); ??
        List<Input.TouchEvent> events;
        events = androidJoyStick.processAndRelease(game.getInput().getTouchEvents());

        for (Input.TouchEvent event : events) {
            Log.d("CICCIO", event.x +"  --  "+event.y);
        }
    }

    @Override
    public void present(float deltaTime) {
        Graphics g = game.getGraphics(); //se lo sfondo è un gameobject si può separare graphics da screen
        g.drawTile(Assets.backgroundTile, 0,0, g.getWidth(), g.getHeight()); //inutile?

        DrawableComponent arenaDrawable = (DrawableComponent) state.arena.getComponent(Component.Type.Drawable);
        arenaDrawable.draw();

        state.drawCharacters();

        androidJoyStick.draw(graphics, Color.GREEN);
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
}
