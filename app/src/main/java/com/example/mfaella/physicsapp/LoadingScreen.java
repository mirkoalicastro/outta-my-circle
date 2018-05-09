package com.example.mfaella.physicsapp;

import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.Log;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.Graphics.PixmapFormat;
import com.badlogic.androidgames.framework.impl.AndroidGame;
import com.badlogic.androidgames.framework.impl.AndroidPixmap;

import java.util.Calendar;

public class LoadingScreen extends com.badlogic.androidgames.framework.LoadingScreen {
    private final Graphics g;
    public LoadingScreen(Game game) {
        super(game);
        g = game.getGraphics();
    }

    @Override
    public void onProgress(int progress) {
        //x: 13, y: 254, width: 290, height: 19
        g.drawPixmap(Assets.loading, 10,250);
        float vals = 290.0f*(((float)progress)/100.0f);
        int val = (int)vals;
        Log.d("LoadingScreenImpl", "VALORE: " + val + " __ PROGRESS: " + progress);
        g.drawRect(13+val,254,291-val,19, Color.BLACK);
        game.display();
    }

    @Override
    public void update(float deltaTime) {
        Assets.backgroundTile = g.newShader("bgtile.png", PixmapFormat.ARGB8888);
        Assets.loading = g.newPixmap("loading.png", PixmapFormat.ARGB8888);
        g.drawTile(Assets.backgroundTile, 0,0, g.getWidth(), g.getHeight());
        setProgress(30);
        Log.d("LoadingScreenImpl", "intanto avanzo!!!!");
        setProgress(70);
        setProgress(10);
        Log.d("LoadingScreenImpl", "e finisco pure!!!!");
        setProgress(100);
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

    private void full() {
        int size = 1000;
        int ret = 0;
        for(int i=0; i<size; i++) {
            for(int j=0; j<size; j++) {
                for(int h=0; h<size; h++) {
                    ret += i+j+h;
                }
            }
        }
        Log.d("", "->" + ret + "<-");
    }

}