package com.example.mfaella.physicsapp;

import android.graphics.Color;
import android.util.Log;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input;
import com.badlogic.androidgames.framework.JoyStick;
import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.impl.AndroidJoyStick;

import java.util.List;

public class JoyStickScreen extends Screen {
    private final JoyStick androidJoyStick = new AndroidJoyStick(game.getInput(),300,300,100);
    private final JoyStick anotherJoyStick = new AndroidJoyStick(game.getInput(),800,300,100);
    public JoyStickScreen(Game game) {
        super(game);
    }

    @Override
    public void update(float deltaTime) {
        List<Input.TouchEvent> events = androidJoyStick.processAndRelease();
        events = anotherJoyStick.processAndRelease(events); //TODO so why not remove the first method and don't pass Input (to the constructor) anymore?
        for (Input.TouchEvent event : events) {
            Log.d(tag, "Ho la possibilita' di gestire " + event.x + "," + event.y);
        }
    }

    @Override
    public void present(float deltaTime) {
        Graphics g = game.getGraphics();
        g.drawTile(Assets.backgroundTile, 0,0, g.getWidth(), g.getHeight());
        androidJoyStick.draw(g, Color.GREEN);
        anotherJoyStick.draw(g, Color.BLUE);
        g.drawRect(200, 550, 700,50, Color.RED);
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
