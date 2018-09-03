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
    private final AndroidJoystick androidJoystick =
            new AndroidJoystick(androidGame.getGraphics(),
                    new RadialGradientEffect(200,580,100,
                            new int[]{Settings.INTERNAL_GRADIENT, Settings.EXTERNAL_GRADIENT},
                            new float[]{0f,1f}, Shader.TileMode.CLAMP
                    ),200,580,100,Settings.DKGRAY,Settings.WHITE50ALFA,null,15,Color.BLACK
            );

    private final TimedCircularButton timedCircularButton = new TimedCircularButton(androidGame.getGraphics(),null,2000,1080,580,100,Settings.DKGREEN,Settings.DKRED,Assets.swords_black,Assets.swords_white,15,Color.BLACK);

    public JoystickScreen(AndroidGame game) {
        super(game);
    }

    @Override
    public void update(float deltaTime) {
        List<Input.TouchEvent> events;
        events = androidJoystick.processAndRelease(androidGame.getInput().getTouchEvents());
//        events = androidGame.getInput().getTouchEvents();
        boolean customizeScreen = false;
        for (Input.TouchEvent event : events) {
            if (timedCircularButton.inBounds(event)) {
                Log.d("TCB", "PRESSED");
                if (timedCircularButton.isEnabled()) {
                    Log.d("TCB", "WOW ATTACKK WHOAAAA");
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

  //  int num = 0;
  //  double sum = 0;
    @Override
    public void present(float deltaTime) {
  //      long start = Calendar.getInstance().getTimeInMillis();
        Graphics g = androidGame.getGraphics();
        g.drawEffect(Assets.backgroundTile, 0,0, g.getWidth(), g.getHeight());
        androidJoystick.draw();
        timedCircularButton.draw();
/*        sum += Calendar.getInstance().getTimeInMillis()-start;
        num++;
        Log.d("Media","vale " + (sum/num) + " (" + num + ")");*/
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
