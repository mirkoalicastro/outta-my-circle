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

public class LoadingScreen extends Screen {
    Game game;
    public LoadingScreen(Game game) {
        super(game);
        this.game = game;
    }

    private void startScreen() {

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

    @Override
    public void update(float deltaTime) {
        Graphics g = game.getGraphics();
        Assets.backgroundTile = g.newShader("bgtile.png", PixmapFormat.ARGB8888);
        Assets.loading = g.newPixmap("loading.png", PixmapFormat.ARGB8888);
        startScreen();
        g.drawTile(Assets.backgroundTile, 0,0, g.getWidth(), g.getHeight());
        g.drawPixmap(Assets.loading, 10,250);
        g.drawRect(150,254,153,19, Color.BLACK);
        game.display();
        full();
        for(int i=1;i<=40;i+=1) {
            g.drawPixmap(Assets.loading, 10,250);
            g.drawRect(150+i,254,153-i,19, Color.BLACK);
            game.display();
            long to = Calendar.getInstance().getTime().getTime()+50;
            while(Calendar.getInstance().getTime().getTime() < to) {

            }

        }
        full();
        g.drawPixmap(Assets.loading, 10,250);
        g.drawRect(230,254,73,19, Color.BLACK);
        game.display();
        //        Assets.click = game.getAudio().newSound("click.ogg");
//        Settings.load(game.getFileIO());
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

    @Override
    public void dispose() {

    }
}