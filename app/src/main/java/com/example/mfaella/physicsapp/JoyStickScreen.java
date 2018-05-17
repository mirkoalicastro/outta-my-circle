package com.example.mfaella.physicsapp;

import android.graphics.Color;
import android.util.Log;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input;
import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.impl.AndroidJoyStick;

public class JoyStickScreen extends Screen {
    private final AndroidJoyStick androidJoyStick = new AndroidJoyStick(game.getInput(),300,300,100);
    public JoyStickScreen(Game game) {
        super(game);
    }

    @Override
    public void update(float deltaTime) {
        for (Input.TouchEvent event : androidJoyStick.processAndRelease()) {
            Log.d(tag, "Ho la possibilita' di gestire " + event.x + "," + event.y);
        }
    }

    @Override
    public void present(float deltaTime) {
        Graphics g = game.getGraphics();
        g.drawTile(Assets.backgroundTile, 0,0, g.getWidth(), g.getHeight());
        androidJoyStick.draw(g, Color.GREEN);
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
