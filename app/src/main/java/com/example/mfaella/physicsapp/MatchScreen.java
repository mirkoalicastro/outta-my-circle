package com.example.mfaella.physicsapp;

import android.graphics.Color;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.JoyStick;
import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.impl.AndroidJoyStick;
import com.example.mfaella.physicsapp.entitycomponent.Component;
import com.example.mfaella.physicsapp.entitycomponent.DrawableComponent;
import com.example.mfaella.physicsapp.entitycomponent.EntityFactory;
import com.example.mfaella.physicsapp.entitycomponent.impl.Arena;
import com.google.fpl.liquidfun.World;
import com.example.mfaella.physicsapp.entitycomponent.impl.Character;

import java.util.List;

public class MatchScreen extends Screen {

    private final JoyStick androidJoyStick = new AndroidJoyStick(game.getInput(),300,300,100);

    private class State{
        public Arena arena;
        public Character[] characters;

        public void setArena(Arena arena){
            this.arena = arena;
        }

        public void setCharacters(Character[] characters) {
            this.characters = characters;
        }
    }

    private State state;

    public MatchScreen(Game game){
        super(game);
        state = new State();
        //chiedi a room lo stato (i giocatori)
        EntityFactory.setWorld(new World(0, 0));
        EntityFactory.setGraphics(game.getGraphics());

        int h = game.getGraphics().getHeight();
        int w = game.getGraphics().getWidth();

        state.setArena(EntityFactory.createArena(h/2 -5, w/2, h/2));

        /*Character[] characters = {EntityFactory.createDefaultCharacter(25, w/2, h/2)};
            pare non funzioni la componente fisica

        state.setCharacters(characters);*/
    }

    @Override
    public void update(float deltaTime) {
        //world.step(); ??
    }

    @Override
    public void present(float deltaTime) {
        Graphics g = game.getGraphics(); //se lo sfondo è un gameobject si può separare graphics da screen
        g.drawTile(Assets.backgroundTile, 0,0, g.getWidth(), g.getHeight()); //inutile?

        DrawableComponent arenaDrawable = (DrawableComponent) state.arena.getComponent(Component.Type.Drawable);
        arenaDrawable.draw();

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
