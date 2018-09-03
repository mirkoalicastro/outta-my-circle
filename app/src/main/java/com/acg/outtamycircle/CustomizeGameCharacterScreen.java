package com.acg.outtamycircle;

import android.graphics.Color;
import android.util.Log;

import com.acg.outtamycircle.network.googleimpl.GoogleRoom;
import com.acg.outtamycircle.network.googleimpl.MyGoogleSignIn;
import com.badlogic.androidgames.framework.Button;
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

    private final Button leftSkin = new AndroidRectangularButton(androidGame.getGraphics(),null,490-74,200,74,80,null,Assets.leftArrow,0,null);
    private final Button leftAttack = new AndroidRectangularButton(androidGame.getGraphics(),null,490-74,400,74,80,null,Assets.leftArrow,0,null);
    private final Button rightSkin = new AndroidRectangularButton(androidGame.getGraphics(),null,790,200,74,80,null,Assets.rightArrow,0,null);
    private final Button rightAttack = new AndroidRectangularButton(androidGame.getGraphics(),null,790,400,74,80,null,Assets.rightArrow,0,null);
    private final Button quickGame = new AndroidCircularButton(androidGame.getGraphics(),null,150,150,50,Color.RED,null,0,null);

    private boolean dontUpdate;

    private MyGoogleSignIn myGoogleSignIn = MyGoogleSignIn.getInstance();

    public CustomizeGameCharacterScreen(AndroidGame androidGame) {
        super(androidGame);
        myGoogleSignIn.createClient(androidGame);
        myGoogleSignIn.signIn();
    }

    @Override
    public void update(float deltaTime) {
        boolean rom = false;
        for (Input.TouchEvent event : androidGame.getInput().getTouchEvents()) {
            if(event.type != Input.TouchEvent.TOUCH_UP)
                continue;
            if(quickGame.inBounds(event) && quickGame.isEnabled()) {
                rom = true;
            } else if(rightSkin.inBounds(event)) {
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
        if(rom) {
            quickGame.enable(false);
            GoogleRoom.getInstance().quickGame(2,2);
        }
    }

    @Override
    public void present(float deltaTime) {
        if(dontUpdate)
            return;
        Log.d("BEBE", myGoogleSignIn.getPlayerId() == null ? "null" : myGoogleSignIn.getPlayerId());
        dontUpdate = true;
        g.drawEffect(Assets.backgroundTile, 0,0, g.getWidth(), g.getHeight());
        quickGame.draw();
        g.drawText(androidGame.getString(R.string.select_player),520,150,40, android.graphics.Color.RED);
        g.drawText(androidGame.getString(R.string.select_attack),500,350,40, android.graphics.Color.RED);
        g.drawPixmap(Assets.skins[currentIdSkin], 590, 190);
        if(currentIdSkin != Assets.skins.length-1)
            rightSkin.draw();
        if(currentIdSkin != 0)
            leftSkin.draw();
        g.drawPixmap(Assets.attacks[currentIdAttack], 590, 390);
        if(currentIdAttack != Assets.attacks.length-1)
            rightAttack.draw();
        if(currentIdAttack != 0)
            leftAttack.draw();
    }

    @Override
    public void pause() {
        Log.d("GoogleS", "pause");
        quickGame.enable(false);
    }

    @Override
    public void resume() {
        Log.d("GoogleS", "resume");
        quickGame.enable(true);
    }

    @Override
    public void dispose() {

    }
}
