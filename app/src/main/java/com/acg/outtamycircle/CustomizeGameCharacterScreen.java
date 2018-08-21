package com.acg.outtamycircle;

import android.util.Log;

import com.badlogic.androidgames.framework.Button;
import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input;
import com.badlogic.androidgames.framework.Pixmap;
import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.impl.AndroidGraphics;
import com.badlogic.androidgames.framework.impl.AndroidRectangularButton;

import java.util.List;

public class CustomizeGameCharacterScreen extends Screen {
    private int currentIdPixmap = 0;
    private final Graphics g = game.getGraphics();
    private final Button rightArrow = new AndroidRectangularButton(790,200,74,80);
    private final Button leftArrow = new AndroidRectangularButton(490-74,200,74,80);
    public CustomizeGameCharacterScreen(Game game) {
        super(game);
    }

    @Override
    public void update(float deltaTime) {
        for (Input.TouchEvent event : game.getInput().getTouchEvents()) {
            if (rightArrow.inBounds(event)) {
                if(currentIdPixmap < Assets.skins.length-1)
                    currentIdPixmap++;
            } else if(leftArrow.inBounds(event)) {
                if(currentIdPixmap > 0)
                    currentIdPixmap--;
            }
        }
    }

    @Override
    public void present(float deltaTime) {
        g.drawTile(Assets.backgroundTile, 0,0, g.getWidth(), g.getHeight());
        g.drawPixmap(Assets.skins[currentIdPixmap], 590, 200, 0,0,100,100);
        rightArrow.draw(g, Assets.rightArrow);
        leftArrow.draw(g, Assets.rightArrow);
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
