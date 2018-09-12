package com.acg.outtamycircle;

import android.graphics.Color;
import android.util.Log;

import com.acg.outtamycircle.network.googleimpl.GoogleAndroidGame;
import com.acg.outtamycircle.network.googleimpl.MyGoogleRoom;
import com.acg.outtamycircle.network.googleimpl.MyGoogleSignIn;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input;
import com.badlogic.androidgames.framework.impl.AndroidButton;
import com.badlogic.androidgames.framework.impl.AndroidCircularButton;
import com.badlogic.androidgames.framework.impl.AndroidGame;
import com.badlogic.androidgames.framework.impl.AndroidRectangularButton;
import com.badlogic.androidgames.framework.impl.AndroidScreen;

public class CustomizeGameCharacterScreen extends AndroidScreen {
    private byte currentIdSkin = 0;
    private byte currentIdAttack = 0;

    private final AndroidButton leftSkin = new AndroidRectangularButton(androidGame.getGraphics(),490-74,200,74,80);
    private final AndroidButton leftAttack = new AndroidRectangularButton(androidGame.getGraphics(),490-74,400,74,80);
    private final AndroidButton rightSkin = new AndroidRectangularButton(androidGame.getGraphics(),790,200,74,80);
    private final AndroidButton rightAttack = new AndroidRectangularButton(androidGame.getGraphics(),790,400,74,80);
    private final AndroidButton fakeButton = new AndroidCircularButton(androidGame.getGraphics(),150,150,100);

    private final AndroidButton backButton = new AndroidRectangularButton(androidGame.getGraphics(),66,550,324,124);
    private final AndroidButton quickGameButton = new AndroidRectangularButton(androidGame.getGraphics(), 890,550,324,124);

    private boolean unchanged;

    public CustomizeGameCharacterScreen(GoogleAndroidGame googleAndroidGame) {
        super(googleAndroidGame);
        leftSkin.setPixmap(Assets.leftArrow);
        rightSkin.setPixmap(Assets.rightArrow);
        leftAttack.setPixmap(Assets.leftArrow);
        rightAttack.setPixmap(Assets.rightArrow);
        fakeButton.setColor(Color.RED);
        backButton.setPixmap(Assets.back);
        quickGameButton.setPixmap(Assets.quickGame);
        googleAndroidGame.getMyGoogleSignIn().signIn();
    }

    @Override
    public void update(float deltaTime) {
        boolean rom = false;

        boolean goBack = false;
        boolean goForward = false;
        for (Input.TouchEvent event : androidGame.getInput().getTouchEvents()) {
            if(event.type != Input.TouchEvent.TOUCH_UP)
                continue;
            if(fakeButton.inBounds(event) && fakeButton.isEnabled()) {
                Log.d("GoogleS", "click");
                rom = true;
                break;
            } else if(backButton.inBounds(event)) {
                goBack = true;
                if(Settings.soundEnabled)
                    Assets.click.play(Settings.volume);
                break;
            } else if(quickGameButton.inBounds(event)) {
                goForward = true;
                if(Settings.soundEnabled)
                    Assets.click.play(Settings.volume);
                break;
            } else if(rightSkin.inBounds(event)) {
                if(currentIdSkin < Assets.skins.length) {
                    currentIdSkin++;
                    unchanged = false;
                    if(Settings.soundEnabled)
                        Assets.click.play(Settings.volume);
                }
            } else if(leftSkin.inBounds(event)) {
                if(currentIdSkin > 0) {
                    currentIdSkin--;
                    unchanged = false;
                    if(Settings.soundEnabled)
                        Assets.click.play(Settings.volume);
                }
            } else if (rightAttack.inBounds(event)) {
                if(currentIdAttack < Assets.attacks.length) {
                    currentIdAttack++;
                    unchanged = false;
                    if(Settings.soundEnabled)
                        Assets.click.play(Settings.volume);
                }
            } else if(leftAttack.inBounds(event)) {
                if(currentIdAttack > 0) {
                    currentIdAttack--;
                    unchanged = false;
                    if(Settings.soundEnabled)
                        Assets.click.play(Settings.volume);
                }
            }
        }
        if(goBack) {
            androidGame.setScreen(new MainMenuScreen(androidGame));
            return;
        }
        if(goForward) {
            int[][] spawnPositions = distributePoints(game.getGraphics().getHeight()/2 - 80, game.getGraphics().getWidth()/2, game.getGraphics().getHeight() /2, 2);
            androidGame.setScreen(new ServerScreen(androidGame,((GoogleAndroidGame)androidGame).getMyGoogleRoom(),new String[]{"",""},new byte[]{currentIdSkin,1},spawnPositions,new byte[]{0,1}, 0));
        }
        if(rom) {
            byte definitiveIdSkin = currentIdSkin;
            byte definitiveIdAttack = currentIdAttack;
            if(definitiveIdSkin == Assets.skins.length)
                definitiveIdSkin = (byte) (Math.random() * Assets.skins.length);
            if(definitiveIdAttack == Assets.attacks.length)
                definitiveIdAttack = (byte) (Math.random() * Assets.attacks.length);
            fakeButton.enable(
                    !((GoogleAndroidGame)androidGame).getMyGoogleRoom().quickGame(2,2,definitiveIdSkin, definitiveIdAttack)
            );
        }
    }

    @Override
    public void present(float deltaTime) {
        if(unchanged)
            return;
        final Graphics graphics = androidGame.getGraphics();
        unchanged = true;
        graphics.drawEffect(Assets.backgroundTile, 0,0, graphics.getWidth(), graphics.getHeight());
        fakeButton.draw();
        quickGameButton.draw();
        backButton.draw();
        graphics.drawText(androidGame.getString(R.string.select_player),520,150,40, Color.BLACK);
        graphics.drawText(androidGame.getString(R.string.select_attack),500,350,40, Color.BLACK);
        if(currentIdSkin == Assets.skins.length)
            graphics.drawPixmap(Assets.random, 590, 190);
        else
            graphics.drawPixmap(Assets.skins[currentIdSkin], 590, 190);
        if(currentIdSkin != Assets.skins.length)
            rightSkin.draw();
        if(currentIdSkin != 0)
            leftSkin.draw();
        if(currentIdAttack == Assets.attacks.length)
            graphics.drawPixmap(Assets.random, 590, 390);
        else
            graphics.drawPixmap(Assets.attacks[currentIdAttack], 590, 390);
        if(currentIdAttack != Assets.attacks.length)
            rightAttack.draw();
        if(currentIdAttack != 0)
            leftAttack.draw();
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

    @Override
    public void back() {
        if(Settings.soundEnabled)
            Assets.click.play(Settings.volume);
        androidGame.setScreen(new MainMenuScreen(androidGame));
    }

    private int[][] distributePoints(int r, int w, int h, int n){
        int[][] points = new int[n][2];
        double x, y;
        double p = (Math.PI*2)/n;
        double theta = Math.PI/2;

        for(int i=0 ; i<n ; i++){
            x = Math.cos(theta)*r;
            y = Math.sin(theta)*r;


            points[i][0] = (int)x + w;
            points[i][1] = (int)y + h;

            theta += p;
        }
        return points;
    }

}
