package com.acg.outtamycircle;

import android.graphics.Color;
import android.graphics.Shader;
import android.util.Log;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input;
import com.badlogic.androidgames.framework.Joystick;
import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.impl.AndroidGame;
import com.badlogic.androidgames.framework.impl.AndroidJoystick;
import com.acg.outtamycircle.entitycomponent.Component;
import com.acg.outtamycircle.entitycomponent.DrawableComponent;
import com.acg.outtamycircle.entitycomponent.EntityFactory;
import com.acg.outtamycircle.entitycomponent.impl.Arena;
import com.badlogic.androidgames.framework.impl.AndroidScreen;
import com.badlogic.androidgames.framework.impl.RadialGradientEffect;
import com.badlogic.androidgames.framework.impl.TimedCircularButton;
import com.google.fpl.liquidfun.World;

import java.util.Calendar;
import java.util.List;
import java.util.Set;

public class JoystickScreen extends AndroidScreen {
    private final AndroidJoystick androidJoystick = new AndroidJoystick(androidGame.getGraphics(),200,520,100);

    private final TimedCircularButton timedCircularButton = new TimedCircularButton(androidGame.getGraphics(),1080,520,100,2000);

    public JoystickScreen(AndroidGame game) {
        super(game);
        androidJoystick.setSecondaryColor(Settings.WHITE50ALFA)
                .setEffect(new RadialGradientEffect(androidJoystick.getX(),androidJoystick.getY(),androidJoystick.getRadius(),
                        new int[]{Settings.INTERNAL_GRADIENT, Settings.EXTERNAL_GRADIENT},
                        new float[]{0f,1f}, Shader.TileMode.CLAMP))
                .setColor(Settings.DKGRAY).setStroke(15,Color.BLACK);

        timedCircularButton.setSecondaryColor(Settings.DKRED)
                .setSecondaryPixmap(Assets.swords_white)
                .setPixmap(Assets.swords_black)
                .setColor(Settings.DKGREEN)
                .setStroke(15,Color.BLACK);
    }

    @Override
    public void update(float deltaTime) {
        List<Input.TouchEvent> events;
        events = androidJoystick.processAndRelease(androidGame.getInput().getTouchEvents());
//        events = androidGame.getInput().getTouchEvents();
        boolean customizeScreen = false;
        for (Input.TouchEvent event : events) {
            if (timedCircularButton.inBounds(event)) {
                if (timedCircularButton.isEnabled()) {
                    timedCircularButton.resetTime();
                    customizeScreen = true;
                    break;
                }
            }
        }
        if(customizeScreen) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(3000);
                    } catch(InterruptedException e) {

                    }
                    androidGame.setScreen(new CustomizeGameCharacterScreen(androidGame));
                }
            }).start();
        }
    }

    @Override
    public void present(float deltaTime) {
        Graphics g = androidGame.getGraphics();
        g.drawEffect(Assets.backgroundTile, 0,0, g.getWidth(), g.getHeight());
        androidJoystick.draw();
        timedCircularButton.draw();
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
