package com.acg.outtamycircle;

import android.graphics.Color;

import com.badlogic.androidgames.framework.Button;
import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input.TouchEvent;
import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.impl.AndroidCircularButton;
import com.badlogic.androidgames.framework.impl.AndroidGame;
import com.badlogic.androidgames.framework.impl.AndroidRectangularButton;
import com.badlogic.androidgames.framework.impl.AndroidScreen;

public class MainMenuScreen extends AndroidScreen {
    private final Button sound = new AndroidCircularButton(androidGame.getGraphics(),200,200,150);
    private final Button start = new AndroidRectangularButton(androidGame.getGraphics(),500,500,100,100);

    private final Button match = new AndroidRectangularButton(androidGame.getGraphics(), 500,200,100,100);

    private int colorStart = Color.BLUE;

    private int colorMatch = Color.WHITE;

    public MainMenuScreen(AndroidGame game) {
        super(game);
    }

    @Override
    public void update(float deltaTime) {

        for(TouchEvent event: androidGame.getInput().getTouchEvents()) {
            if(event.type == TouchEvent.TOUCH_UP) {
                if(sound.inBounds(event)) {
                    Settings.soundEnabled = !Settings.soundEnabled;
//                    if(Settings.soundEnabled)
  //                      Assets.click.play(1);
                }
                if(start.inBounds(event)) {
                    if(colorStart == Color.BLUE)
                        colorStart = Color.MAGENTA;
                    else {
                        colorStart = Color.BLUE;
                        androidGame.setScreen(new JoystickScreen(androidGame));
                    }
    //                if(Settings.soundEnabled)
      //                  Assets.click.play(1);
                    //Start lobby
                }
                if(match.inBounds(event)){
                    androidGame.setScreen(new ServerScreen(androidGame, new long[]{0, 1, 2, 3})); //TODO generalizzare
                }
            }
        }
    }

    @Override
    public void present(float deltaTime) {
        Graphics g = androidGame.getGraphics();
        g.drawTile(Assets.backgroundTile, 0,0, g.getWidth(), g.getHeight());
        if(Settings.soundEnabled)
            sound.draw(Color.GREEN);
        else
            sound.draw(Color.RED);
        start.draw(colorStart);

        match.draw(colorMatch);
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
