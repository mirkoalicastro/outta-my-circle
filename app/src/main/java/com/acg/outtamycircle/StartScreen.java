package com.acg.outtamycircle;

import android.content.res.AssetManager;
import android.graphics.Color;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics.PixmapFormat;
import com.badlogic.androidgames.framework.Pixmap;
import com.badlogic.androidgames.framework.impl.AndroidGraphics;
import com.badlogic.androidgames.framework.impl.AndroidLoadingScreen;

import java.io.IOException;
import java.util.Arrays;

public class StartScreen extends AndroidLoadingScreen {
    final AssetManager assetManager = ((AndroidGraphics) game.getGraphics()).getAssetManager();

    public StartScreen(Game game) {
        super(game);
    }

    @Override
    public void onProgress(int progress) {
        int x = (game.getGraphics().getWidth()-Assets.loading.getWidth())/2;
        int y = (game.getGraphics().getHeight()-Assets.loading.getHeight())/2;
        //x: 13, y: 254, width: 290, height: 19
        game.getGraphics().drawPixmap(Assets.loading, x,y);
        int val = 475;
        float vals = (float)val*(((float)progress)/100.0f);
        game.getGraphics().drawRect((int)vals+11+x,y+12,val-(int)vals,31, Color.BLACK);
        game.display();
    }

    @Override
    public void update(float deltaTime) {
        Assets.backgroundTile = game.getGraphics().newTile("bgtile.png", PixmapFormat.ARGB8888);
        Assets.loading = game.getGraphics().newPixmap("loading.png", PixmapFormat.ARGB8888);
        try {
            String[] skins = assetManager.list("skins");
            if(skins.length == 0)
                throw new RuntimeException("Error no skin founded");
            Arrays.sort(skins);
            Assets.skins = new Pixmap[skins.length];
            for(int i=0; i<skins.length; i++)
                Assets.skins[i] = game.getGraphics().newPixmap("skins/"+skins[i], PixmapFormat.ARGB8888);

            String[] attacks = assetManager.list("attacks");
            if(attacks.length == 0)
                throw new RuntimeException("Error no attack founded");
            Arrays.sort(attacks);
            Assets.attacks = new Pixmap[attacks.length];
            for(int i=0; i<attacks.length; i++)
                Assets.attacks[i] = game.getGraphics().newPixmap("attacks/"+attacks[i], PixmapFormat.ARGB8888);
        } catch (IOException e) {
            throw new RuntimeException("Error opening assets");
        }
        Assets.rightArrow = game.getGraphics().newPixmap("r_arrow.png", PixmapFormat.ARGB8888);
        Assets.leftArrow = game.getGraphics().newPixmap("l_arrow.png", PixmapFormat.ARGB8888);
        game.getGraphics().drawTile(Assets.backgroundTile, 0,0, game.getGraphics().getWidth(), game.getGraphics().getHeight());
        setProgress(70);
        Assets.click = game.getAudio().newSound("urto1.wav");
        // Settings.load(game.getFileIO());
        setProgress(100);
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