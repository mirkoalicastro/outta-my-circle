package com.example.mfaella.physicsapp;

import android.graphics.Color;
import android.util.Log;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Graphics.PixmapFormat;

public class LoadingScreen extends com.badlogic.androidgames.framework.LoadingScreen {
    private final Graphics g;
    public LoadingScreen(Game game) {
        super(game);
        g = game.getGraphics();
    }

    @Override
    public void onProgress(int progress) {
        int x = (g.getWidth()-Assets.loading.getWidth())/2;
        int y = (g.getHeight()-Assets.loading.getHeight())/2;
        //x: 13, y: 254, width: 290, height: 19
        g.drawPixmap(Assets.loading, x,y);
        int val = 475;
        float vals = (float)val*(((float)progress)/100.0f);
        g.drawRect((int)vals+11+x,y+12,val-(int)vals,31, Color.BLACK);
        game.display();
    }

    @Override
    public void update(float deltaTime) {
        Assets.backgroundTile = g.newTile("bgtile.png", PixmapFormat.ARGB8888);
        Assets.loading = g.newPixmap("loading.png", PixmapFormat.ARGB8888);
        g.drawTile(Assets.backgroundTile, 0,0, g.getWidth(), g.getHeight());
        setProgress(30);
        // long to = Calendar.getInstance().getTime().getTime()+50;
        // while(Calendar.getInstance().getTime().getTime() < to);
        // Assets.click = game.getAudio().newSound("click.ogg");
        // Settings.load(game.getFileIO());
        game.setScreen(new MainMenuScreen(game));
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

}