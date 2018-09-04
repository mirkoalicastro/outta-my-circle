package com.acg.outtamycircle;

import android.graphics.Color;

import com.badlogic.androidgames.framework.Button;
import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input.TouchEvent;
import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.impl.AndroidButton;
import com.badlogic.androidgames.framework.impl.AndroidCircularButton;
import com.badlogic.androidgames.framework.impl.AndroidGame;
import com.badlogic.androidgames.framework.impl.AndroidRectangularButton;
import com.badlogic.androidgames.framework.impl.AndroidScreen;

public class MainMenuScreen extends AndroidScreen {
    private final AndroidButton start = new AndroidRectangularButton(androidGame.getGraphics(),200,500,100,100);
    private final AndroidButton match = new AndroidRectangularButton(androidGame.getGraphics(), 900,500,100,100);

    public MainMenuScreen(AndroidGame game) {
        super(game);
        start.setColor(Color.BLUE);
        match.setColor(Color.WHITE);
    }

    @Override
    public void update(float deltaTime) {

        for(TouchEvent event: androidGame.getInput().getTouchEvents()) {
            if(event.type == TouchEvent.TOUCH_UP) {
                if(start.inBounds(event)) {
                    if(start.getColor().equals(Color.BLUE))
                        start.setColor(Color.MAGENTA);
                    else {
                        start.setColor(Color.BLUE);
                        androidGame.setScreen(new JoystickScreen(androidGame));
                    }
    //                if(Settings.soundEnabled)
      //                  Assets.click.play(1);
                } else if(match.inBounds(event)){
                    androidGame.setScreen(new ServerScreen(androidGame, new long[]{0, 1, 2, 3})); //TODO generalizzare
                }
            }
        }
    }

    @Override
    public void present(float deltaTime) {
        Graphics g = androidGame.getGraphics();
        g.drawEffect(Assets.backgroundTile, 0,0, g.getWidth(), g.getHeight());
        g.drawPixmap(Assets.logo, 406,126);

        start.draw();
        match.draw();
    }

    @Override
    public void pause() {
       // Settings.save(game.getFileIO());
    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
