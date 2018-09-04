package com.acg.outtamycircle;

import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input.TouchEvent;
import com.badlogic.androidgames.framework.impl.AndroidButton;
import com.badlogic.androidgames.framework.impl.AndroidGame;
import com.badlogic.androidgames.framework.impl.AndroidRectangularButton;
import com.badlogic.androidgames.framework.impl.AndroidScreen;

public class MainMenuScreen extends AndroidScreen {
    private final AndroidButton startButton = new AndroidRectangularButton(androidGame.getGraphics(), 890,550,324,124);
    private boolean unchanged; //TODO che si fa?

    public MainMenuScreen(AndroidGame game) {
        super(game);
        startButton.setPixmap(Assets.start);
    }

    @Override
    public void update(float deltaTime) {
        for(TouchEvent event: androidGame.getInput().getTouchEvents()) {
            if(event.type == TouchEvent.TOUCH_UP) {
//                if(Settings.soundEnabled)
  //                  Assets.click.play(1);
                if(startButton.inBounds(event))
                    androidGame.setScreen(new CustomizeGameCharacterScreen(androidGame));
            }
        }
    }

    @Override
    public void present(float deltaTime) {
        if(unchanged)
            return;
        Graphics g = androidGame.getGraphics();
        g.drawEffect(Assets.backgroundTile, 0,0, g.getWidth(), g.getHeight());
        g.drawPixmap(Assets.logo, 406,126);

        startButton.draw();

        unchanged = true;
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
}
