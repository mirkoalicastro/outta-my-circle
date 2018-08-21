package com.acg.outtamycircle;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Pixmap;
import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.impl.AndroidGraphics;

public class CustomizeGameCharacterScreen extends Screen {
    private Pixmap currentPixmap = Assets.skins[0];
    private final Graphics g = game.getGraphics();
    public CustomizeGameCharacterScreen(Game game) {
        super(game);
    }

    @Override
    public void update(float deltaTime) {

    }

    @Override
    public void present(float deltaTime) {
        g.drawTile(Assets.backgroundTile, 0,0, g.getWidth(), g.getHeight());
        g.drawPixmap(currentPixmap, 590, 200, 0,0,100,100);
//        g.drawPixmap();
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
