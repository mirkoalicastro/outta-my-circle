package com.acg.outtamycircle;

import android.graphics.Color;

import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Graphics.PixmapFormat;
import com.badlogic.androidgames.framework.impl.AndroidGame;
import com.badlogic.androidgames.framework.impl.AndroidLoadingScreen;
import com.badlogic.androidgames.framework.impl.AndroidPixmap;
import com.badlogic.androidgames.framework.impl.CircularAndroidTileEffect;
import com.badlogic.androidgames.framework.impl.RectangularAndroidTileEffect;

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
        Assets.backgroundTile = new RectangularAndroidTileEffect((AndroidPixmap)Assets.background);
        Assets.loading = graphics.newPixmap("etc/loading.png", PixmapFormat.ARGB8888);
        graphics.drawEffect(Assets.backgroundTile, 0,0, graphics.getWidth(), graphics.getHeight());
        setProgress(15);
        Assets.arena = graphics.newPixmap("tiles/arenatile.png", PixmapFormat.ARGB8888);
        Assets.arenaTile = new CircularAndroidTileEffect((AndroidPixmap)Assets.arena);
        setProgress(30);
        Assets.help = graphics.newPixmap("buttons/help.png", PixmapFormat.ARGB8888);
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
        Assets.powerups = graphics.newPixmapsFromFolder("powerups",PixmapFormat.ARGB8888);
        setProgress(65);
        Assets.wait = graphics.newPixmap("etc/wait.png", PixmapFormat.ARGB8888);
        Assets.unknownSkin = graphics.newPixmap("etc/unknown.png", PixmapFormat.ARGB8888);
        Assets.logo = graphics.newPixmap("etc/logo.png", PixmapFormat.ARGB8888);
        Assets.random = graphics.newPixmap("etc/random.png", PixmapFormat.ARGB8888);
        Assets.swordsBlack = graphics.newPixmap("etc/swords_black.png", PixmapFormat.ARGB8888);
        Assets.swordsWhite = graphics.newPixmap("etc/swords_white.png", PixmapFormat.ARGB8888);
        Assets.sad = graphics.newPixmap("etc/sad.png", PixmapFormat.ARGB8888);
        Assets.happy = graphics.newPixmap("etc/happy.png", PixmapFormat.ARGB8888);
        Assets.neutral = graphics.newPixmap("etc/neutral.png", PixmapFormat.ARGB8888);
        Assets.wait = graphics.newPixmap("etc/wait.png", PixmapFormat.ARGB8888);
        setProgress(80);
        Assets.click = androidGame.getAudio().newSound("audio/click.mp3");
        Assets.powerupCollision = androidGame.getAudio().newSound("audio/powerupcollision.mp3");
        Assets.newPowerup = androidGame.getAudio().newSound("audio/newpowerup.mp3");
        Assets.gameCharacterCollision = androidGame.getAudio().newSound("audio/gamecharcollision.mp3");
        Assets.attackEnabled = androidGame.getAudio().newSound("audio/attackenabled.mp3");
        Assets.attackDisabled = androidGame.getAudio().newSound("audio/attackdisabled.mp3");
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