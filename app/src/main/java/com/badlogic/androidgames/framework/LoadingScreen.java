package com.badlogic.androidgames.framework;


public abstract class LoadingScreen extends Screen {

    public LoadingScreen(Game game) {
        super(game);
    }

    public abstract void setProgress(int progress);

    public abstract int getProgress();

    public abstract void onProgress(int progress);
}
