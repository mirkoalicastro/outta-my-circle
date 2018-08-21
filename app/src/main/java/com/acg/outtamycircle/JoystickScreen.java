package com.acg.outtamycircle;

import android.graphics.Color;
import android.util.Log;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input;
import com.badlogic.androidgames.framework.Joystick;
import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.impl.AndroidJoystick;
import com.acg.outtamycircle.entitycomponent.Component;
import com.acg.outtamycircle.entitycomponent.DrawableComponent;
import com.acg.outtamycircle.entitycomponent.EntityFactory;
import com.acg.outtamycircle.entitycomponent.impl.Arena;
import com.badlogic.androidgames.framework.impl.TimedCircularButton;
import com.google.fpl.liquidfun.World;

import java.util.List;

public class JoystickScreen extends Screen {
    private final Joystick androidJoystick = new AndroidJoystick(game.getInput(),200,580,100);
    private final World world;
    private final Arena arena;
    private final TimedCircularButton timedCircularButton = new TimedCircularButton(2000,1080,580,100);
  //  private final Character gameCharacter;

    public JoystickScreen(Game game) {
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
        events = androidJoystick.processAndRelease(game.getInput().getTouchEvents());

        for (Input.TouchEvent event : events) {
            if(timedCircularButton.inBounds(event)) {
                Log.d("TCB", "PRESSED");
                if (timedCircularButton.isActive()) {
                    Log.d("TCB", "WOW ATTACKK WHOAAAA");
                    game.setScreen(new CustomizeGameCharacterScreen(game));
                }
            }
        }
    }

    @Override
    public void present(float deltaTime) {
        Graphics g = game.getGraphics();
        g.drawTile(Assets.backgroundTile, 0,0, g.getWidth(), g.getHeight());
        DrawableComponent arenaDrawable = (DrawableComponent) arena.getComponent(Component.Type.Drawable);
        //arenaDrawable.setColor(Color.BLUE);
        //arenaDrawable.drawColor();
/*        DrawableComponent gameCharacterDrawable = (DrawableComponent) gameCharacter.getComponent(Component.Type.Drawable);
        gameCharacterDrawable.setColor(Color.RED);
        gameCharacterDrawable.drawColor();
*/
        androidJoystick.draw(g, Color.DKGRAY);
        timedCircularButton.draw(g, Color.GREEN);
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
