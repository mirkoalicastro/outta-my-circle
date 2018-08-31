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
    private int colorStart = Color.BLUE;

    private int colorMatch = Color.WHITE;

    private final Button sound = new AndroidCircularButton(androidGame.getGraphics(),null, 200,200,150,Color.GREEN,null,0,null);
    private final Button start = new AndroidRectangularButton(androidGame.getGraphics(),null,500,500,100,100, colorStart, null, 0, null);
    private final Button match = new AndroidRectangularButton(androidGame.getGraphics(),null, 500,200,100,100, colorMatch, null, 0, null);

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
        g.drawEffect(Assets.backgroundTile, 0,0, g.getWidth(), g.getHeight());
        sound.draw();
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
