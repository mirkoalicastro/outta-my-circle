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
    private int currentIdAttack = 0;
    private final Graphics g = game.getGraphics();
    private final Button rightSkin = new AndroidRectangularButton(790,200,74,80);
    private final Button leftSkin = new AndroidRectangularButton(490-74,200,74,80);
    private final Button rightAttack = new AndroidRectangularButton(790,400,74,80);
    private final Button leftAttack = new AndroidRectangularButton(490-74,400,74,80);

    private boolean dontUpdate;

    public CustomizeGameCharacterScreen(Game game) {
        super(game);
    }

    @Override
    public void update(float deltaTime) {
        for (Input.TouchEvent event : game.getInput().getTouchEvents()) {
            Log.d("demo", "tipo " + event.type);
            if(event.type != Input.TouchEvent.TOUCH_UP)
                continue;
            if (rightSkin.inBounds(event)) {
                if(currentIdPixmap < Assets.skins.length-1) {
                    currentIdPixmap++;
                    dontUpdate = false;
                }
            } else if(leftSkin.inBounds(event)) {
                if(currentIdPixmap > 0) {
                    currentIdPixmap--;
                    dontUpdate = false;
                }
            } else if (rightAttack.inBounds(event)) {
                if(currentIdAttack < Assets.attacks.length-1) {
                    currentIdAttack++;
                    dontUpdate = false;
                }
            } else if(leftAttack.inBounds(event)) {
                if(currentIdAttack > 0) {
                    currentIdAttack--;
                    dontUpdate = false;
                }
            }
        }
    }

    @Override
    public void present(float deltaTime) {
        if(dontUpdate)
            return;
        Log.d("info", "present");
        dontUpdate = true;
        g.drawTile(Assets.backgroundTile, 0,0, g.getWidth(), g.getHeight());
        g.drawPixmap(Assets.skins[currentIdPixmap], 590, 190, 0,0,200,200);
        rightSkin.draw(g, Assets.rightArrow);
        leftSkin.draw(g, Assets.leftArrow);
        g.drawPixmap(Assets.attacks[currentIdAttack], 590, 390, 0,0,200,200);
        rightAttack.draw(g, Assets.rightArrow);
        leftAttack.draw(g, Assets.leftArrow);
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
