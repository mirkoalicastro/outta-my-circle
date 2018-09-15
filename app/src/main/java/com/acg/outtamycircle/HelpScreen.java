package com.acg.outtamycircle;

import android.graphics.Color;

import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input;
import com.badlogic.androidgames.framework.impl.AndroidButton;
import com.badlogic.androidgames.framework.impl.AndroidGame;
import com.badlogic.androidgames.framework.impl.AndroidRectangularButton;
import com.badlogic.androidgames.framework.impl.AndroidScreen;

public class HelpScreen extends AndroidScreen {
    private final AndroidButton backButton = new AndroidRectangularButton(androidGame.getGraphics(),66,550,324,124);

    public HelpScreen(AndroidGame androidGame) {
        super(androidGame);
        backButton.setPixmap(Assets.back);
    }

    @Override
    public void update(float deltaTime) {
        boolean goMainMenuScreen = false;
        for(Input.TouchEvent event: androidGame.getInput().getTouchEvents()) {
            if(backButton.inBounds(event) && event.type == Input.TouchEvent.TOUCH_UP) {
                if(Settings.soundEnabled)
                    Assets.click.play(Settings.volume);
                goMainMenuScreen = true;
                break;
            }
        }
        if(goMainMenuScreen)
            androidGame.setScreen(new MainMenuScreen(androidGame));
    }

    @Override
    public void present(float deltaTime) {
        Graphics graphics = androidGame.getGraphics();
        graphics.drawEffect(Assets.backgroundTile, 0,0,graphics.getWidth(),graphics.getHeight());
        graphics.drawText(androidGame.getString(R.string.help_first_line),66, 66, 35, Color.BLACK);
        graphics.drawText(androidGame.getString(R.string.help_second_line),66, 66+45, 35, Color.BLACK);
        graphics.drawText(androidGame.getString(R.string.help_third_line),66, 66+45*2, 35, Color.BLACK);
        graphics.drawText(androidGame.getString(R.string.help_fourth_line),66, 66+45*3, 35, Color.BLACK);
        graphics.drawText(androidGame.getString(R.string.help_fifth_line),66, 66+45*4, 35, Color.BLACK);
        graphics.drawText(androidGame.getString(R.string.help_sixth_line),66, 66+45*5, 35, Color.BLACK);
        graphics.drawText(androidGame.getString(R.string.help_seventh_line),66, 66+45*6, 35, Color.BLACK);
        //blank line
        graphics.drawText(androidGame.getString(R.string.help_eighth_line),66, 66+45*8, 35, Color.BLACK);
        backButton.draw();
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
}
