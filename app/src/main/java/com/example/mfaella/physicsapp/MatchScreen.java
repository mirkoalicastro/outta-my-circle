package com.example.mfaella.physicsapp;

import android.graphics.Color;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Screen;
import com.example.mfaella.physicsapp.entitycomponent.EntityFactory;
import com.example.mfaella.physicsapp.entitycomponent.impl.Arena;
import com.google.fpl.liquidfun.World;
import com.example.mfaella.physicsapp.entitycomponent.impl.Character;

import java.util.List;

public class MatchScreen extends Screen {
    private class State{
        Arena arena;
        Character[] characters;

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



        state.setArena(EntityFactory.createArena(game.getGraphics().getWidth()/2 - 1));

        Character[] characters = {EntityFactory.createDefaultCharacter(2,0,0)};

        state.setCharacters(characters);
    }

    @Override
    public void update(float deltaTime) {

    }

    @Override
    public void present(float deltaTime) {
        Graphics g = game.getGraphics(); //se lo sfondo è un gameobject si può separare graphics da screen
        g.drawTile(Assets.backgroundTile, 0,0, g.getWidth(), g.getHeight()); //inutile?

        //disegna

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
