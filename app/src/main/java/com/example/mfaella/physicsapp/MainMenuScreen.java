package com.example.mfaella.physicsapp;

import android.util.Log;

import com.badlogic.androidgames.framework.Button;
import com.badlogic.androidgames.framework.Color;
import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input.TouchEvent;
import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.impl.AndroidCircularButton;
import com.badlogic.androidgames.framework.impl.AndroidRectangularButton;

public class MainMenuScreen extends Screen {
    private final Button sound = new AndroidCircularButton(200,200,150);
    private final Button start = new AndroidRectangularButton(500,500,100,100);
    public MainMenuScreen(Game game) {
        super(game);
    }

    @Override
    public void update(float deltaTime) {
        Graphics g = game.getGraphics();

        game.getInput().getKeyEvents();

        for(TouchEvent event: game.getInput().getTouchEvents()) {
            if(event.type == TouchEvent.TOUCH_UP) {
                //TODO why event.x and event.y are always 0? (Check AndroidFastRenderView is on screen)
//                event.x = (int) (Math.random()*200f);
  //              event.y = (int) (Math.random()*200f);
                Log.d("AGLIO", "Individuo evento: " + event.x + "," + event.y);
                if(sound.inBounds(event))
                    Log.d("AGLIO", "O mostr");
                if(inBounds(event, 0, g.getHeight() - 64, 64, 64)) {
        //            Settings.soundEnabled = !Settings.soundEnabled;
          //          if(Settings.soundEnabled)
            //            Assets.click.play(1);
                }
                if(inBounds(event, 64, 220, 192, 42) ) {
       //             game.setScreen(new GameScreen(game));
              //      if(Settings.soundEnabled)
                //        Assets.click.play(1);
                    return;
                }
                if(inBounds(event, 64, 220 + 42, 192, 42) ) {
                  //  game.setScreen(new HighscoreScreen(game));
                    //if(Settings.soundEnabled)
                      //  Assets.click.play(1);
                    return;
                }
                if(inBounds(event, 64, 220 + 84, 192, 42) ) {
                //    game.setScreen(new HelpScreen(game));
                  //  if(Settings.soundEnabled)
                    //    Assets.click.play(1);
                    return;
                }
            }
        }
    }

    private boolean inBounds(TouchEvent event, int x, int y, int width, int height) {
        return (event.x > x && event.x < x + width - 1 &&
                event.y > y && event.y < y + height - 1);
    }

    @Override
    public void present(float deltaTime) {
        Graphics g = game.getGraphics();
        g.drawTile(Assets.backgroundTile, 0,0, g.getWidth(), g.getHeight());
        //TODO forse dovrebbe disegnare graphics direttamente, o forse no.
        sound.draw(g, android.graphics.Color.RED);
        start.draw(g, android.graphics.Color.BLUE);
//        g.drawPixmap(Assets.background, 0, 0);
  //      g.drawPixmap(Assets.logo, 32, 20);
    //    g.drawPixmap(Assets.mainMenu, 64, 220);
      //  if(Settings.soundEnabled)
        //    g.drawPixmap(Assets.buttons, 0, 416, 0, 0, 64, 64);
        //else
          //  g.drawPixmap(Assets.buttons, 0, 416, 64, 0, 64, 64);
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
