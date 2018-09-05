package com.acg.outtamycircle;

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
        final Graphics graphics = androidGame.getGraphics();
        Assets.background = graphics.newPixmap("bgtile.png", PixmapFormat.ARGB8888);
        Assets.backgroundTile = new RectangularTileAndroidEffect((AndroidPixmap)Assets.background);
        Assets.loading = graphics.newPixmap("loading.png", PixmapFormat.ARGB8888);
        setProgress(30);
        graphics.drawEffect(Assets.backgroundTile, 0,0, graphics.getWidth(), graphics.getHeight());
        Assets.joystick = graphics.newPixmap("joystick.png", PixmapFormat.ARGB8888);
        Assets.arena = graphics.newPixmap("arenatile.png", PixmapFormat.ARGB8888);
        Assets.arenaTile = new CircularTileAndroidEffect((AndroidPixmap)Assets.arena);
        Assets.logo = graphics.newPixmap("logo.png", PixmapFormat.ARGB8888);
        Assets.skins = graphics.newPixmapsFromFolder("skins",PixmapFormat.ARGB8888);
        Assets.attacks = graphics.newPixmapsFromFolder("attacks",PixmapFormat.ARGB8888);
        Assets.start = graphics.newPixmap("start.png", PixmapFormat.ARGB8888);
        Assets.back = graphics.newPixmap("back.png", PixmapFormat.ARGB8888);
        Assets.quickGame = graphics.newPixmap("quickgame.png", PixmapFormat.ARGB8888);
        setProgress(50);
        Assets.sound = graphics.newPixmap("sound.png", PixmapFormat.ARGB8888);
        Assets.nosound = graphics.newPixmap("nosound.png", PixmapFormat.ARGB8888);
        Assets.rightArrow = graphics.newPixmap("r_arrow.png", PixmapFormat.ARGB8888);
        Assets.leftArrow = graphics.newPixmap("l_arrow.png", PixmapFormat.ARGB8888);
        Assets.swordsBlack = graphics.newPixmap("swords_black.png", PixmapFormat.ARGB8888);
        Assets.swordsWhite = graphics.newPixmap("swords_white.png", PixmapFormat.ARGB8888);
        setProgress(70);
        Assets.click = androidGame.getAudio().newSound("click.wav");
        Assets.powerupCollision = androidGame.getAudio().newSound("powerupcollision.wav");
        Assets.newPowerup = androidGame.getAudio().newSound("powerupcollision.wav");
        Assets.gameCharacterCollision = androidGame.getAudio().newSound("gamecharcollision.wav");
        setProgress(100);
        androidGame.setScreen(new MainMenuScreen(androidGame));
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

}