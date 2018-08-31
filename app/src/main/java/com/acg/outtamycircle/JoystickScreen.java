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

    private final World world;
    private final Arena arena;
    private final TimedCircularButton timedCircularButton = new TimedCircularButton(androidGame.getGraphics(),null,2000,1080,580,100,Settings.DKGREEN,Settings.DKRED,Assets.swords_black,Assets.swords_white,15,Color.BLACK);
  //  private final GameCharacter gameCharacter;

    public JoystickScreen(AndroidGame game) {
        super(game);
        world = new World(0,0);
        EntityFactory.setGraphics(game.getGraphics());
        EntityFactory.setWorld(world);

        arena = EntityFactory.createArena(300,500,400);
    //    gameCharacter = EntityFactory.createDefaultCharacter(30, 500,400);
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
//                    customizeScreen = true;
                    break;
                }
            }
        }
        if(customizeScreen)
            androidGame.setScreen(new CustomizeGameCharacterScreen(androidGame));
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
