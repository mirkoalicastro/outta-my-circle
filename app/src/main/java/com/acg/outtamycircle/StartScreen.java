package com.acg.outtamycircle;

import android.app.Activity;
import android.graphics.Color;

import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Graphics.PixmapFormat;
import com.badlogic.androidgames.framework.impl.AndroidGame;
import com.badlogic.androidgames.framework.impl.AndroidPixmap;
import com.badlogic.androidgames.framework.impl.AndroidLoadingScreen;
import com.badlogic.androidgames.framework.impl.CircularTileAndroidEffect;
import com.badlogic.androidgames.framework.impl.RectangularTileAndroidEffect;

public class StartScreen extends AndroidLoadingScreen {

    public StartScreen(AndroidGame game) {
        super(game);
    }

    @Override
    public void onProgress(int progress) {
        final Graphics graphics = androidGame.getGraphics();

        int x = (graphics.getWidth()-Assets.loading.getWidth())/2;
        int y = (graphics.getHeight()-Assets.loading.getHeight())/2;
        //x: 13, y: 254, width: 290, height: 19
        graphics.drawPixmap(Assets.loading, x,y);
        int val = 473;
        float vals = (float)progress*(float)val/100f;
        graphics.drawRect((int)vals+12+x,y+12,val-(int)vals,31, Color.BLACK);
        androidGame.display(); //force the canvas rendering :'(
    }

    @Override
    public void doJob() {
        Graphics graphics = androidGame.getGraphics();
        Assets.background = graphics.newPixmap("tiles/bgtile.png", PixmapFormat.ARGB8888);
        Assets.backgroundTile = new RectangularTileAndroidEffect((AndroidPixmap)Assets.background);
        Assets.loading = graphics.newPixmap("etc/loading.png", PixmapFormat.ARGB8888);
        graphics.drawEffect(Assets.backgroundTile, 0,0, graphics.getWidth(), graphics.getHeight());
        setProgress(15);
        Assets.arena = graphics.newPixmap("tiles/arenatile.png", PixmapFormat.ARGB8888);
        Assets.arenaTile = new CircularTileAndroidEffect((AndroidPixmap)Assets.arena);
        setProgress(30);
        Assets.start = graphics.newPixmap("buttons/start.png", PixmapFormat.ARGB8888);
        Assets.back = graphics.newPixmap("buttons/back.png", PixmapFormat.ARGB8888);
        Assets.quickGame = graphics.newPixmap("buttons/quickgame.png", PixmapFormat.ARGB8888);
        Assets.sound = graphics.newPixmap("buttons/sound.png", PixmapFormat.ARGB8888);
        Assets.nosound = graphics.newPixmap("buttons/nosound.png", PixmapFormat.ARGB8888);
        Assets.rightArrow = graphics.newPixmap("buttons/r_arrow.png", PixmapFormat.ARGB8888);
        Assets.leftArrow = graphics.newPixmap("buttons/l_arrow.png", PixmapFormat.ARGB8888);
        setProgress(45);
        Assets.skins = graphics.newPixmapsFromFolder("skins",PixmapFormat.ARGB8888);
        setProgress(50);
        Assets.attacks = graphics.newPixmapsFromFolder("attacks",PixmapFormat.ARGB8888);
        setProgress(65);
        Assets.logo = graphics.newPixmap("etc/logo.png", PixmapFormat.ARGB8888);
        Assets.random = graphics.newPixmap("etc/random.png", PixmapFormat.ARGB8888);
        Assets.swordsBlack = graphics.newPixmap("etc/swords_black.png", PixmapFormat.ARGB8888);
        Assets.swordsWhite = graphics.newPixmap("etc/swords_white.png", PixmapFormat.ARGB8888);
        setProgress(80);
        Assets.click = androidGame.getAudio().newSound("audio/click.wav"); //TODO
        Assets.powerupCollision = androidGame.getAudio().newSound("audio/powerupcollision.wav");
        Assets.newPowerup = androidGame.getAudio().newSound("audio/powerupcollision.wav");
        Assets.gameCharacterCollision = androidGame.getAudio().newSound("audio/gamecharcollision.wav");
        Assets.attackEnabled = androidGame.getAudio().newSound("audio/attackenabled.wav");
        Assets.attackDisabled = androidGame.getAudio().newSound("audio/attackdisabled.wav");
        setProgress(100);
        androidGame.setScreen(new MainMenuScreen(androidGame));
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void back() {
        androidGame.finish();
    }

}