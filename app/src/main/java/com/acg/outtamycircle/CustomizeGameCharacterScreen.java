package com.acg.outtamycircle;

import android.graphics.Color;

import com.acg.outtamycircle.network.googleimpl.GoogleAndroidGame;
import com.acg.outtamycircle.network.googleimpl.MyGoogleRoom;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input;
import com.badlogic.androidgames.framework.impl.AndroidButton;
import com.badlogic.androidgames.framework.impl.AndroidRectangularButton;
import com.badlogic.androidgames.framework.impl.AndroidScreen;
import com.badlogic.androidgames.framework.impl.NumberPickerButton;

public class CustomizeGameCharacterScreen extends AndroidScreen {
    private byte currentIdSkin = 0;
    private byte currentIdAttack = 0;
    private boolean showingGoogle;

    private final AndroidButton leftSkin = new AndroidRectangularButton(androidGame.getGraphics(),140-74,200,74,80);
    private final AndroidButton leftAttack = new AndroidRectangularButton(androidGame.getGraphics(),140-74,400,74,80);
    private final AndroidButton rightSkin = new AndroidRectangularButton(androidGame.getGraphics(),440,200,74,80);
    private final AndroidButton rightAttack = new AndroidRectangularButton(androidGame.getGraphics(),440,400,74,80);

    private final NumberPickerButton minPlayersPickerButton = new NumberPickerButton(androidGame.getGraphics(), 240+558, 204, 2,8);
    private final NumberPickerButton maxPlayersPickerButton = new NumberPickerButton(androidGame.getGraphics(), 240+558, 404, 2,8);

    private final AndroidButton backButton = new AndroidRectangularButton(androidGame.getGraphics(),66,550,324,124);
    private final AndroidButton quickGameButton = new AndroidRectangularButton(androidGame.getGraphics(), 890,550,324,124);

    private final MyGoogleRoom myGoogleRoom;
    private final MyGoogleRoom.ResetCallback myResetCallback = new MyGoogleRoom.ResetCallback() {
        @Override
        public void reset() {
            enableButtons(true);
        }
    };
    private boolean unchanged;

    public CustomizeGameCharacterScreen(GoogleAndroidGame googleAndroidGame) {
        super(googleAndroidGame);
        leftSkin.setPixmap(Assets.leftArrow);
        rightSkin.setPixmap(Assets.rightArrow);
        leftAttack.setPixmap(Assets.leftArrow);
        rightAttack.setPixmap(Assets.rightArrow);
        backButton.setPixmap(Assets.back);
        quickGameButton.setPixmap(Assets.quickGame);
        minPlayersPickerButton.setMinusPixmap(Assets.leftArrow);
        minPlayersPickerButton.setPlusPixmap(Assets.rightArrow);
        maxPlayersPickerButton.setMinusPixmap(Assets.leftArrow);
        maxPlayersPickerButton.setPlusPixmap(Assets.rightArrow);
        myGoogleRoom = new MyGoogleRoom(googleAndroidGame, googleAndroidGame.getMyGoogleSignIn());
        googleAndroidGame.setMyGoogleRoom(myGoogleRoom);
    }

    @Override
    public void update(float deltaTime) {
        boolean goBack = false;
        boolean goForward = false;
        for (Input.TouchEvent event : androidGame.getInput().getTouchEvents()) {
            if(event.type != Input.TouchEvent.TOUCH_UP)
                continue;
            if (minPlayersPickerButton.inBounds(event) && minPlayersPickerButton.isEnabled()) {
                if (minPlayersPickerButton.update(event)) {
                    if (Settings.soundEnabled)
                        Assets.click.play(Settings.volume);
                    if(maxPlayersPickerButton.getValue() < minPlayersPickerButton.getValue())
                        maxPlayersPickerButton.setValue(minPlayersPickerButton.getValue());
                    unchanged = false;
                }
            } else if (maxPlayersPickerButton.inBounds(event) && maxPlayersPickerButton.isEnabled()) {
                if (maxPlayersPickerButton.update(event)) {
                    if (Settings.soundEnabled)
                        Assets.click.play(Settings.volume);
                    if(minPlayersPickerButton.getValue() > maxPlayersPickerButton.getValue())
                        minPlayersPickerButton.setValue(maxPlayersPickerButton.getValue());
                    unchanged = false;
                }
            } else if(quickGameButton.inBounds(event) && quickGameButton.isEnabled()) {
                goForward = true;
                if(Settings.soundEnabled)
                    Assets.click.play(Settings.volume);
                break;
            } else if(backButton.inBounds(event) && backButton.isEnabled()) {
                goBack = true;
                if(Settings.soundEnabled)
                    Assets.click.play(Settings.volume);
                break;
            } else if(rightSkin.inBounds(event) && rightSkin.isEnabled()) {
                if(currentIdSkin < Assets.skins.length) {
                    currentIdSkin++;
                    unchanged = false;
                    if(Settings.soundEnabled)
                        Assets.click.play(Settings.volume);
                }
            } else if(leftSkin.inBounds(event) && leftSkin.isEnabled()) {
                if(currentIdSkin > 0) {
                    currentIdSkin--;
                    unchanged = false;
                    if(Settings.soundEnabled)
                        Assets.click.play(Settings.volume);
                }
            } else if (rightAttack.inBounds(event) && rightAttack.isEnabled()) {
                if(currentIdAttack < Assets.attacks.length) {
                    currentIdAttack++;
                    unchanged = false;
                    if(Settings.soundEnabled)
                        Assets.click.play(Settings.volume);
                }
            } else if(leftAttack.inBounds(event) && leftAttack.isEnabled()) {
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
            byte definitiveIdSkin = currentIdSkin;
            byte definitiveIdAttack = currentIdAttack;
            if(definitiveIdSkin == Assets.skins.length)
                definitiveIdSkin = (byte) (Math.random() * Assets.skins.length);
            if(definitiveIdAttack == Assets.attacks.length)
                definitiveIdAttack = (byte) (Math.random() * Assets.attacks.length);
            enableButtons(
                    !myGoogleRoom.quickGame(myResetCallback,minPlayersPickerButton.getValue(), maxPlayersPickerButton.getValue(), definitiveIdSkin, definitiveIdAttack)
            );
        }
    }

    private void enableButtons(boolean enable) {
        quickGameButton.enable(enable);
        backButton.enable(enable);
        minPlayersPickerButton.enable(enable);
        maxPlayersPickerButton.enable(enable);
        leftAttack.enable(enable);
        rightAttack.enable(enable);
        leftSkin.enable(enable);
        rightSkin.enable(enable);
        showingGoogle = !enable;
    }

    @Override
    public void present(float deltaTime) {
        if(unchanged)
            return;
        final Graphics graphics = androidGame.getGraphics();
        unchanged = true;
        graphics.drawEffect(Assets.backgroundTile, 0,0, graphics.getWidth(), graphics.getHeight());
        maxPlayersPickerButton.draw();
        minPlayersPickerButton.draw();
        quickGameButton.draw();
        backButton.draw();
        graphics.drawText(androidGame.getString(R.string.select_player),170,150,40, Color.BLACK);
        graphics.drawText(androidGame.getString(R.string.select_attack),150,350,40, Color.BLACK);
        graphics.drawText(androidGame.getString(R.string.select_min_players),170+732,150,40, Color.BLACK);
        graphics.drawText(androidGame.getString(R.string.select_max_players),165+732,350,40, Color.BLACK);
        if(currentIdSkin == Assets.skins.length)
            graphics.drawPixmap(Assets.random, 240, 190);
        else
            graphics.drawPixmap(Assets.skins[currentIdSkin], 240, 190);
        if(currentIdSkin != Assets.skins.length)
            rightSkin.draw();
        if(currentIdSkin != 0)
            leftSkin.draw();
        if(currentIdAttack == Assets.attacks.length)
            graphics.drawPixmap(Assets.random, 240, 390);
        else
            graphics.drawPixmap(Assets.attacks[currentIdAttack], 240, 390);
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
        if(showingGoogle)
            return;
        if(Settings.soundEnabled)
            Assets.click.play(Settings.volume);
        androidGame.setScreen(new MainMenuScreen(androidGame));
    }

}
