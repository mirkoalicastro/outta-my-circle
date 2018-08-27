package com.acg.outtamycircle;

import android.app.Activity;
import android.util.Log;

import com.badlogic.androidgames.framework.Button;
import com.badlogic.androidgames.framework.Color;
import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input;
import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.impl.AndroidGame;
import com.badlogic.androidgames.framework.impl.AndroidRectangularButton;
import com.badlogic.androidgames.framework.impl.AndroidScreen;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.List;

public class CustomizeGameCharacterScreen extends AndroidScreen {
    private int currentIdSkin = 0;
    private int currentIdAttack = 0;
    private final Graphics g = androidGame.getGraphics();
    private final Button rightSkin = new AndroidRectangularButton(790,200,74,80);
    private final Button leftSkin = new AndroidRectangularButton(490-74,200,74,80);
    private final Button rightAttack = new AndroidRectangularButton(790,400,74,80);
    private final Button leftAttack = new AndroidRectangularButton(490-74,400,74,80);

    private boolean dontUpdate;

    public CustomizeGameCharacterScreen(AndroidGame androidGame) {
        super(androidGame);
        GoogleTest.createClient(androidGame);
        GoogleTest.signIn();
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
        dontUpdate = true;
        g.drawTile(Assets.backgroundTile, 0,0, g.getWidth(), g.getHeight());
        g.drawText(androidGame.getString(R.string.select_player),490,100,30, android.graphics.Color.RED);
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
