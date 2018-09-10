package com.acg.outtamycircle;

import android.graphics.Color;
import android.graphics.Shader;
import android.util.Log;
import android.widget.Toast;

import com.acg.outtamycircle.entitycomponent.Component;
import com.acg.outtamycircle.entitycomponent.DrawableComponent;
import com.acg.outtamycircle.entitycomponent.DrawableComponentFactory;
import com.acg.outtamycircle.entitycomponent.impl.Arena;
import com.acg.outtamycircle.entitycomponent.impl.GameCharacter;
import com.acg.outtamycircle.network.GameMessageInterpreterImpl;
import com.acg.outtamycircle.network.googleimpl.MyGoogleRoom;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input;
import com.badlogic.androidgames.framework.Pixmap;
import com.badlogic.androidgames.framework.impl.AndroidEffect;
import com.badlogic.androidgames.framework.impl.AndroidGame;
import com.badlogic.androidgames.framework.impl.AndroidJoystick;
import com.badlogic.androidgames.framework.impl.AndroidScreen;
import com.badlogic.androidgames.framework.impl.ComposerAndroidEffect;
import com.badlogic.androidgames.framework.impl.RadialGradientEffect;
import com.badlogic.androidgames.framework.impl.TimedCircularButton;

import java.util.List;

public abstract class ClientServerScreen extends AndroidScreen {
    protected GameStatus status;
    protected int frameHeight, frameWeight, arenaRadius; //height, width, radius

    private final TimedCircularButton timedCircularButton = new TimedCircularButton(androidGame.getGraphics(),1080,520,100,2000);

    protected final DrawableComponentFactory drawableComponentFactory = new DrawableComponentFactory();

    /*La cattura degli eventi è equivalente in client e server,
     ma va processata in maniera differente*/
    protected List<Input.TouchEvent> events;
    protected final AndroidJoystick androidJoystick = new AndroidJoystick(androidGame.getGraphics(),200,520,100);
    protected final MyGoogleRoom myGoogleRoom;
    protected final String[] players;
    protected final short[] skins;
    protected final int[][] spawnPositions;

    protected final GameMessageInterpreterImpl interpreter = new GameMessageInterpreterImpl();

    public ClientServerScreen(AndroidGame game, MyGoogleRoom myGoogleRoom, String[] players, short[] skins, int[][] spawnPositions) {
        super(game);
        this.myGoogleRoom = myGoogleRoom;
        this.players = players;
        this.skins = skins;
        this.spawnPositions = spawnPositions;

        androidJoystick.setSecondaryColor(Settings.WHITE50ALFA)
                .setEffect(new RadialGradientEffect(androidJoystick.getX(),androidJoystick.getY(),androidJoystick.getRadius(),
                        new int[]{Settings.INTERNAL_GRADIENT, Settings.EXTERNAL_GRADIENT},
                        new float[]{0f,1f}, Shader.TileMode.CLAMP))
                .setColor(Settings.DKGRAY).setStroke(15,Color.BLACK);

        timedCircularButton.setSecondaryColor(Settings.DKRED)
                .setSecondaryPixmap(Assets.swordsWhite)
                .setPixmap(Assets.swordsBlack)
                .setColor(Settings.DKGREEN)
                .setStroke(15, Color.BLACK);

        frameHeight = game.getGraphics().getHeight();
        frameWeight = game.getGraphics().getWidth();
        arenaRadius = frameHeight/2 - 40;

        setup();
        status = new GameStatus();

        drawableComponentFactory.setGraphics(game.getGraphics());
        initArenaSettings();
        status.setArena(createArena());
    }

    @Override
    public void present(float deltaTime) {
        //TODO
        Graphics g = game.getGraphics(); //se lo sfondo è un gameobject si può separare graphics da screen
        g.drawEffect(Assets.backgroundTile, 0,0, g.getWidth(), g.getHeight()); //inutile?

        drawArena();
        drawCharacters();

        //DrawableComponent powerupDrawable = (DrawableComponent) status.powerup.makeComponent(Component.Type.Drawable);
        //powerupDrawable.draw();

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

    public abstract void setup();

    public void update(float deltaTime) {
        events = androidJoystick.processAndRelease(game.getInput().getTouchEvents());

        for (Input.TouchEvent event : events) {
            if (timedCircularButton.inBounds(event) && event.type == Input.TouchEvent.TOUCH_UP) {
                Log.d("TCB", "PRESSED");
                if (timedCircularButton.isEnabled()) {
                    if(Settings.soundEnabled)
                        Assets.attackEnabled.play(Settings.volume);
                    timedCircularButton.resetTime();
                } else if(Settings.soundEnabled)
                    Assets.attackDisabled.play(Settings.volume);
            }
        }
    }

    private void drawCharacters(){
        for(GameCharacter ch : status.living)
            ((DrawableComponent)ch.getComponent(Component.Type.Drawable)).draw();
        for(GameCharacter ch : status.dying)
            ((DrawableComponent)ch.getComponent(Component.Type.Drawable)).draw();
        status.living.resetIterator();
        status.dying.resetIterator();
    }

    private void drawArena(){
        DrawableComponent arenaDrawable = (DrawableComponent) status.arena.getComponent(Component.Type.Drawable);
        arenaDrawable.draw();
    }

    @Override
    public void back() {
        Toast.makeText(androidGame,"E mo che si fa?",Toast.LENGTH_SHORT).show();
        androidGame.setScreen(new MainMenuScreen(androidGame));
    }

    protected void initCharacterSettings(float r){
        drawableComponentFactory.resetFactory();
        drawableComponentFactory.setStroke(6,Color.BLACK)
                .setHeight((int)(r*2)).setWidth((int)(r*2))
                .setShape(DrawableComponentFactory.DrawableShape.CIRCLE);
    }

    private void initArenaSettings(){
        drawableComponentFactory.resetFactory();
        int x = frameWeight/2, y = frameHeight/2;

        drawableComponentFactory.setWidth(arenaRadius*2).setHeight(arenaRadius*2)
                .setX(x).setY(y)
                .setEffect(new ComposerAndroidEffect(
                        new RadialGradientEffect(x,y,arenaRadius,
                                new int[]{Color.parseColor("#348496"), Color.parseColor("#4DC1DD")},
                                new float[]{0f,1f}, Shader.TileMode.CLAMP
                        ),
                        (AndroidEffect)Assets.arenaTile)
                ).setShape(DrawableComponentFactory.DrawableShape.CIRCLE);
    }

    //TODO spostare in clientserverscreen
    private Arena createArena(){
        Arena arena = new Arena();

        drawableComponentFactory.setOwner(arena);
        arena.addComponent(drawableComponentFactory.makeComponent());

        return arena;
    }

    protected GameCharacter createCharacter(int x, int y, Pixmap pixmap, short id){
        GameCharacter gc = new GameCharacter(id);

        drawableComponentFactory.setPixmap(pixmap).setX(x).setY(y).setOwner(gc);
        gc.addComponent(drawableComponentFactory.makeComponent());

        return gc;
    }
}
