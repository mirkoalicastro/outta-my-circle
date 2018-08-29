package com.acg.outtamycircle;

import android.util.Log;

import com.acg.outtamycircle.network.googleimpl.MyGoogleSignIn;
import com.badlogic.androidgames.framework.Button;
import com.badlogic.androidgames.framework.Color;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input;
import com.badlogic.androidgames.framework.impl.AndroidCircularButton;
import com.badlogic.androidgames.framework.impl.AndroidGame;
import com.badlogic.androidgames.framework.impl.AndroidRectangularButton;
import com.badlogic.androidgames.framework.impl.AndroidScreen;

public class CustomizeGameCharacterScreen extends AndroidScreen {
    private int currentIdSkin = 0;
    private int currentIdAttack = 0;
    private final Graphics g = androidGame.getGraphics();
    private final Button rightSkin = new AndroidRectangularButton(790,200,74,80);
    private final Button leftSkin = new AndroidRectangularButton(490-74,200,74,80);
    private final Button rightAttack = new AndroidRectangularButton(790,400,74,80);
    private final Button leftAttack = new AndroidRectangularButton(490-74,400,74,80);
    private final Button quickGame = new AndroidCircularButton(50,50,50);

    private boolean dontUpdate;

    private MyGoogleSignIn myGoogleSignIn = MyGoogleSignIn.getInstance();

    public CustomizeGameCharacterScreen(AndroidGame androidGame) {
        super(androidGame);
        myGoogleSignIn.createClient(androidGame);
        myGoogleSignIn.signIn();
    }

    @Override
    public void update(float deltaTime) {
        for (Input.TouchEvent event : androidGame.getInput().getTouchEvents()) {
            if(event.type != Input.TouchEvent.TOUCH_UP)
                continue;
            if (rightSkin.inBounds(event)) {
                if(currentIdSkin < Assets.skins.length-1) {
                    currentIdSkin++;
                    dontUpdate = false;
                }
            } else if(leftSkin.inBounds(event)) {
                if(currentIdSkin > 0) {
                    currentIdSkin--;
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
        Log.d("BEBE", myGoogleSignIn.getPlayerId() == null ? "null" : myGoogleSignIn.getPlayerId());
        dontUpdate = true;
        quickGame.draw(g, Color.convert(50,20,10,100));
        g.drawTile(Assets.backgroundTile, 0,0, g.getWidth(), g.getHeight());
        g.drawText(androidGame.getString(R.string.select_player),520,150,40, android.graphics.Color.RED);
        g.drawText(androidGame.getString(R.string.select_attack),500,350,40, android.graphics.Color.RED);
        g.drawPixmap(Assets.skins[currentIdSkin], 590, 190, 0,0,200,200);
        if(currentIdSkin != Assets.skins.length-1)
            rightSkin.draw(g, Assets.rightArrow);
        if(currentIdSkin != 0)
            leftSkin.draw(g, Assets.leftArrow);
        g.drawPixmap(Assets.attacks[currentIdAttack], 590, 390, 0,0,200,200);
        if(currentIdAttack != Assets.attacks.length-1)
            rightAttack.draw(g, Assets.rightArrow);
        if(currentIdAttack != 0)
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
