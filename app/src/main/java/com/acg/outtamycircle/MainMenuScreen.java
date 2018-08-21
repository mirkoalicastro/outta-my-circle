package com.acg.outtamycircle;

import android.graphics.Color;

import com.badlogic.androidgames.framework.Button;
import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input.TouchEvent;
import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.impl.AndroidCircularButton;
import com.badlogic.androidgames.framework.impl.AndroidRectangularButton;

public class MainMenuScreen extends Screen {
    private final Button sound = new AndroidCircularButton(200,200,150);
    private final Button start = new AndroidRectangularButton(500,500,100,100);

    private final Button match = new AndroidRectangularButton(500,200,100,100);

    private int colorStart = android.graphics.Color.BLUE;

    private int colorMatch = Color.WHITE;

    public MainMenuScreen(Game game) {
        super(game);
        game.getInput().getTouchEvents(); //clear
    }

    @Override
    public void update(float deltaTime) {
        Graphics g = game.getGraphics();

        game.getInput().getKeyEvents(); //is it necessary?

        for(TouchEvent event: game.getInput().getTouchEvents()) {
            if(event.type == TouchEvent.TOUCH_UP) {
                if(sound.inBounds(event)) {
                    Settings.soundEnabled = !Settings.soundEnabled;
//                    if(Settings.soundEnabled)
  //                      Assets.click.play(1);
                }
                if(start.inBounds(event)) {
                    if(colorStart == android.graphics.Color.BLUE)
                        colorStart = android.graphics.Color.MAGENTA;
                    else {
                        colorStart = android.graphics.Color.BLUE;
                        game.setScreen(new JoystickScreen(game));
                    }
    //                if(Settings.soundEnabled)
      //                  Assets.click.play(1);
                    //Start lobby
                }
                if(match.inBounds(event)){
                    game.setScreen(new MatchScreen(game));
                }
            }
        }
    }

    @Override
    public void present(float deltaTime) {
        Graphics g = game.getGraphics();
        g.drawTile(Assets.backgroundTile, 0,0, g.getWidth(), g.getHeight());
        if(Settings.soundEnabled)
            sound.draw(g, android.graphics.Color.GREEN);
        else
            sound.draw(g, android.graphics.Color.RED);
        start.draw(g, colorStart);

        match.draw(g, colorMatch);
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
