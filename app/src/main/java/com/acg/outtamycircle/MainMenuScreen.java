package com.acg.outtamycircle;

import android.util.Log;

import com.acg.outtamycircle.network.googleimpl.GoogleAndroidGame;
import com.badlogic.androidgames.framework.Animation;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input.TouchEvent;
import com.badlogic.androidgames.framework.impl.AndroidButton;
import com.badlogic.androidgames.framework.impl.AndroidCircularButton;
import com.badlogic.androidgames.framework.impl.AndroidGame;
import com.badlogic.androidgames.framework.impl.AndroidRectangularButton;
import com.badlogic.androidgames.framework.impl.AndroidScreen;
import com.badlogic.androidgames.framework.impl.SpinAnimation;

public class MainMenuScreen extends AndroidScreen {

    private final AndroidButton startButton = new AndroidRectangularButton(androidGame.getGraphics(), 890,550,324,124);
    private final AndroidButton soundButton = new AndroidCircularButton(androidGame.getGraphics(), 1050, 146,86);
    private boolean unchanged;

    public MainMenuScreen(AndroidGame game) {
        super(game);
        Settings.loadSettings(androidGame);
        startButton.setPixmap(Assets.start);
        if (Settings.soundEnabled)
            soundButton.setPixmap(Assets.sound);
        else
            soundButton.setPixmap(Assets.nosound);
    }

    @Override
    public void update(float deltaTime) {
        boolean goCustomizeScreen = false;
        for(TouchEvent event: androidGame.getInput().getTouchEvents()) {
            if(event.type == TouchEvent.TOUCH_UP) {
                if(soundButton.inBounds(event)) {
                    unchanged = false;
                    if(Settings.soundEnabled) {
                        soundButton.setPixmap(Assets.nosound);
                    } else {
                        Log.d("AUDIOTEST", "play");
                        Assets.click.play(Settings.volume);
                        soundButton.setPixmap(Assets.sound);
                    }
                    Settings.setSoundEnabled(androidGame, !Settings.soundEnabled);
                }
                if(startButton.inBounds(event)) {
                    if(Settings.soundEnabled)
                        Assets.click.play(Settings.volume);
                    goCustomizeScreen = true;
                }
            }
        }
        unchanged = false;
        if(goCustomizeScreen)
            androidGame.setScreen(new CustomizeGameCharacterScreen((GoogleAndroidGame)androidGame));
    }

    @Override
    public void present(float deltaTime) {
        if(unchanged)
            return;
        Graphics g = androidGame.getGraphics();
        g.drawEffect(Assets.backgroundTile, 0,0, g.getWidth(), g.getHeight());
        g.drawPixmap(Assets.logo, 406,126);

        startButton.draw();
        soundButton.draw();

        unchanged = true;
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {
        Settings.loadSettings(androidGame);
    }

    @Override
    public void dispose() {

    }

    @Override
    public void back() {
        androidGame.finish();
    }
}
