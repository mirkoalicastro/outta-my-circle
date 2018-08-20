package com.acg.outtamycircle;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Screen;

public abstract class ClientServerScreen extends Screen {

    public ClientServerScreen(Game game) {
        super(game);
        setup();
    }

    @Override
    public void present(float deltaTime) {

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

    abstract void setup();
}
