package com.badlogic.androidgames.framework;

public abstract class Screen {
    protected final Game game;
    public final String tag;

    public Screen(Game game) {
        this.game = game;
        //TODO it's temp.
        this.tag = this.getClass().getSimpleName();
    }

    public abstract void update(float deltaTime);

    public abstract void present(float deltaTime);

    public abstract void pause();

    public abstract void resume();

    public abstract void dispose();

}
