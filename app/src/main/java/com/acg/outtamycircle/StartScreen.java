package com.acg.outtamycircle;

import android.graphics.Color;

import com.badlogic.androidgames.framework.Graphics.PixmapFormat;
import com.badlogic.androidgames.framework.Pixmap;
import com.badlogic.androidgames.framework.impl.AndroidGame;
import com.badlogic.androidgames.framework.impl.AndroidSplashScreen;

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
        Assets.backgroundTile = game.getGraphics().newTile("bgtile.png", PixmapFormat.ARGB8888);
        androidGame.getGraphics().drawTile(Assets.backgroundTile, 0,0, game.getGraphics().getWidth(), game.getGraphics().getHeight());
        Assets.loading = game.getGraphics().newPixmap("loading.png", PixmapFormat.ARGB8888);
        try {
            String[] skins = androidGame.getAssets().list("skins");
            if(skins.length == 0)
                throw new RuntimeException("Error no skin founded");
            Arrays.sort(skins);
            Assets.skins = new Pixmap[skins.length];
            for(int i=0; i<skins.length; i++)
                Assets.skins[i] = androidGame.getGraphics().newPixmap("skins/"+skins[i], PixmapFormat.ARGB8888);

            String[] attacks = androidGame.getAssets().list("attacks");
            if(attacks.length == 0)
                throw new RuntimeException("Error no attack founded");
            Arrays.sort(attacks);
            Assets.attacks = new Pixmap[attacks.length];
            for(int i=0; i<attacks.length; i++)
                Assets.attacks[i] = androidGame.getGraphics().newPixmap("attacks/"+attacks[i], PixmapFormat.ARGB8888);
        } catch (IOException e) {
            throw new RuntimeException("Error opening assets");
        }
        Assets.rightArrow = androidGame.getGraphics().newPixmap("r_arrow.png", PixmapFormat.ARGB8888);
        Assets.leftArrow = androidGame.getGraphics().newPixmap("l_arrow.png", PixmapFormat.ARGB8888);
        Assets.swords_black = androidGame.getGraphics().newPixmap("swords_black.png", PixmapFormat.ARGB8888);
        Assets.swords_white = androidGame.getGraphics().newPixmap("swords_white.png", PixmapFormat.ARGB8888);
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