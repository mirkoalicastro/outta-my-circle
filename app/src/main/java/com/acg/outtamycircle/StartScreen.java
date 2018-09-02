package com.acg.outtamycircle;

import android.graphics.Color;

import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Graphics.PixmapFormat;
import com.badlogic.androidgames.framework.Pixmap;
import com.badlogic.androidgames.framework.impl.AndroidGame;
import com.badlogic.androidgames.framework.impl.AndroidPixmap;
import com.badlogic.androidgames.framework.impl.AndroidSplashScreen;
import com.badlogic.androidgames.framework.impl.CircularTileAndroidEffect;
import com.badlogic.androidgames.framework.impl.RectangularTileAndroidEffect;

import java.io.IOException;
import java.util.Arrays;

public class StartScreen extends AndroidSplashScreen {

    public StartScreen(AndroidGame game) {
        super(game);
    }

    @Override
    public void onProgress(int progress) {
        int x = (androidGame.getGraphics().getWidth()-Assets.loading.getWidth())/2;
        int y = (androidGame.getGraphics().getHeight()-Assets.loading.getHeight())/2;
        //x: 13, y: 254, width: 290, height: 19
        androidGame.getGraphics().drawPixmap(Assets.loading, x,y);
        int val = 475;
        float vals = (float)val*(((float)progress)/100.0f);
        androidGame.getGraphics().drawRect((int)vals+11+x,y+12,val-(int)vals,31, Color.BLACK);
        androidGame.display();
    }

    @Override
    public void update(float deltaTime) {
        final Graphics graphics = androidGame.getGraphics();
        Assets.joystick = graphics.newPixmap("joystick.png", PixmapFormat.ARGB8888);
        Assets.background = graphics.newPixmap("bgtile.png", PixmapFormat.ARGB8888);
        Assets.backgroundTile = new RectangularTileAndroidEffect((AndroidPixmap)Assets.background);
        graphics.drawEffect(Assets.backgroundTile, 0,0, graphics.getWidth(), graphics.getHeight());
        Assets.loading = graphics.newPixmap("loading.png", PixmapFormat.ARGB8888);
        Assets.arena = graphics.newPixmap("arenatile.png", PixmapFormat.ARGB8888);
        Assets.arenaTile = new CircularTileAndroidEffect((AndroidPixmap)Assets.arena);
        Assets.arena = graphics.newPixmap("arena.png", PixmapFormat.ARGB8888);
        try {
            String[] skins = androidGame.getAssets().list("skins");
            if(skins.length == 0)
                throw new RuntimeException("Error no skin founded");
            Arrays.sort(skins);
            Assets.skins = new Pixmap[skins.length];
            for(int i=0; i<skins.length; i++)
                Assets.skins[i] = graphics.newPixmap("skins/"+skins[i], PixmapFormat.ARGB8888);

            String[] attacks = androidGame.getAssets().list("attacks");
            if(attacks.length == 0)
                throw new RuntimeException("Error no attack founded");
            Arrays.sort(attacks);
            Assets.attacks = new Pixmap[attacks.length];
            for(int i=0; i<attacks.length; i++)
                Assets.attacks[i] = graphics.newPixmap("attacks/"+attacks[i], PixmapFormat.ARGB8888);
        } catch (IOException e) {
            throw new RuntimeException("Error opening assets");
        }
        Assets.rightArrow = graphics.newPixmap("r_arrow.png", PixmapFormat.ARGB8888);
        Assets.leftArrow = graphics.newPixmap("l_arrow.png", PixmapFormat.ARGB8888);
        Assets.swords_black = graphics.newPixmap("swords_black.png", PixmapFormat.ARGB8888);
        Assets.swords_white = graphics.newPixmap("swords_white.png", PixmapFormat.ARGB8888);
        setProgress(70);
        Assets.click = androidGame.getAudio().newSound("urto1.wav");
        // Settings.load(game.getFileIO());
        setProgress(100);
        androidGame.setScreen(new MainMenuScreen(androidGame));
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