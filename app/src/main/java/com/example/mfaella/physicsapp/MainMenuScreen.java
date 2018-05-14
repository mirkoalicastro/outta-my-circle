package com.example.mfaella.physicsapp;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input.TouchEvent;
import com.badlogic.androidgames.framework.Screen;

public class MainMenuScreen extends Screen {

    public MainMenuScreen(Game game) {

        super(game);
    }

    @Override
    public void update(float deltaTime) {
        Graphics g = game.getGraphics();

        game.getInput().getKeyEvents();

        for(TouchEvent event: game.getInput().getTouchEvents()) {
            if(event.type == TouchEvent.TOUCH_UP) {
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
