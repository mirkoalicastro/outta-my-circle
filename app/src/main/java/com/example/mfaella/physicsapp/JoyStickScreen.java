package com.example.mfaella.physicsapp;

import android.graphics.Color;
import android.util.Log;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input;
import com.badlogic.androidgames.framework.Screen;

public class JoyStickScreen extends Screen {
    private final JoyStick joyStick = new JoyStick(game.getInput(), game.getGraphics(),300,300,100);
    public JoyStickScreen(Game game) {
        super(game);
    }

    @Override
    public void update(float deltaTime) {
        for (Input.TouchEvent event : joyStick.processAndRelease()) {
            Log.d(tag, "Ho la possibilita' di gestire " + event.x + "," + event.y);
        }
    }

    @Override
    public void present(float deltaTime) {
        Graphics g = game.getGraphics();
        g.drawTile(Assets.backgroundTile, 0,0, g.getWidth(), g.getHeight());
        joyStick.draw(g, Color.GREEN);
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
