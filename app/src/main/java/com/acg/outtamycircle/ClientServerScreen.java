package com.acg.outtamycircle;

import android.graphics.Color;
import android.graphics.Shader;
import android.util.Log;

import com.acg.outtamycircle.entitycomponent.Component;
import com.acg.outtamycircle.entitycomponent.DrawableComponent;
import com.acg.outtamycircle.entitycomponent.Entity;
import com.acg.outtamycircle.entitycomponent.impl.factories.DrawableComponentFactory;
import com.acg.outtamycircle.entitycomponent.impl.gameobjects.GameCharacter;
import com.acg.outtamycircle.entitycomponent.impl.gameobjects.Powerup;
import com.acg.outtamycircle.entitycomponent.impl.gameobjects.RadialForcePowerup;
import com.acg.outtamycircle.network.GameMessageInterpreterImpl;
import com.acg.outtamycircle.network.NetworkMessageHandlerImpl;
import com.acg.outtamycircle.network.googleimpl.GoogleAndroidGame;
import com.acg.outtamycircle.network.googleimpl.MyGoogleRoom;
import com.acg.outtamycircle.utilities.MyList;
import com.badlogic.androidgames.framework.Button;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input;
import com.badlogic.androidgames.framework.Pixmap;
import com.badlogic.androidgames.framework.impl.AndroidButton;
import com.badlogic.androidgames.framework.impl.AndroidEffect;
import com.badlogic.androidgames.framework.impl.AndroidGame;
import com.badlogic.androidgames.framework.impl.AndroidJoystick;
import com.badlogic.androidgames.framework.impl.AndroidRectangularButton;
import com.badlogic.androidgames.framework.impl.AndroidScreen;
import com.badlogic.androidgames.framework.impl.ComposerAndroidEffect;
import com.badlogic.androidgames.framework.impl.RadialGradientEffect;
import com.badlogic.androidgames.framework.impl.TimedCircularButton;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public abstract class ClientServerScreen extends AndroidScreen {
    protected static final int ROUNDS = 2;
    protected int roundNum = 1;
    protected long startAt;

    protected int radiusCharacter = 35;

    protected GameStatus status;
    protected final int frameHeight, frameWidth, arenaRadius;
    protected boolean isAlive = true;
    protected final int[] winnerId ;
    protected boolean endGame, endRound;

    protected final TimedCircularButton timedCircularButton = new TimedCircularButton(androidGame.getGraphics(),1080,520,100,3000);

    protected final DrawableComponentFactory drawableComponentFactory;
    protected final NetworkMessageHandlerImpl networkMessageHandler;

    private final AndroidButton backButton = new AndroidRectangularButton(androidGame.getGraphics(),66,550,324,124).setPixmap(Assets.back);

    protected List<Input.TouchEvent> events;
    protected final AndroidJoystick androidJoystick = new AndroidJoystick(androidGame.getGraphics(),200,520,100){
        @Override
        public float getNormX(){
            return super.getNormX() * 1000;
        }

        @Override
        public float getNormY(){
            return super.getNormY() * 1000;
        }
    };
    protected final MyGoogleRoom myGoogleRoom;
    protected final String[] players;
    protected final int[] skins;
    protected final int[][] spawnPositions;
    protected final int playerOffset;
    protected boolean shouldAttack;

    protected final GameMessageInterpreterImpl interpreter = new GameMessageInterpreterImpl();

    public ClientServerScreen(AndroidGame game, MyGoogleRoom myGoogleRoom, String[] players, int[] skins, int[][] spawnPositions, int playerOffset) {
        super(game);
        this.backButton.enable(false);
        this.myGoogleRoom = myGoogleRoom;
        this.players = players;
        this.skins = skins;
        this.spawnPositions = spawnPositions;
        this.playerOffset = playerOffset;
        this.networkMessageHandler = myGoogleRoom.getNetworkMessageHandler();
        this.winnerId = new int[ROUNDS];
        for(int i=0; i<winnerId.length; i++)
            this.winnerId[i] = -1;

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
        frameWidth = game.getGraphics().getWidth();
        arenaRadius = frameHeight/2 - 40;

        setup();

        drawableComponentFactory = new DrawableComponentFactory(game.getGraphics());
    }

    @Override
    public void present(float deltaTime) {
        //TODO
        Graphics g = game.getGraphics();
        g.drawEffect(Assets.backgroundTile, 0,0, g.getWidth(), g.getHeight());

        drawArena();
        drawList(status.living);
        drawList(status.dying);
        drawList(status.inactives);

        //DrawableComponent powerupDrawable = (DrawableComponent) status.powerup.makeComponent(Component.Type.Drawable);
        //powerupDrawable.draw();

        for(int i=0; i<ROUNDS; i++) {
            Pixmap winnerSkin;
            if(winnerId[i] == -1)
                winnerSkin = Assets.unknownSkin;
            else
                winnerSkin = Assets.skins[skins[winnerId[i]]];
            g.drawPixmap(winnerSkin, 30+(70*i),80, 50, 50);
        }

        g.drawText(androidGame.getString(R.string.round) + " " + Math.min(roundNum, ROUNDS) + "/" + ROUNDS, 30,60, 40, Color.BLACK);

        if(endGame) {
            if(gameResultPixmap == null)
                calculateGameResultPixmap();
            g.drawPixmap(gameResultPixmap, 515, 235);
            backButton.draw();
            g.drawText(androidGame.getString(R.string.game_over),1000,60,40,Color.BLACK);
            return;
        } else if(endRound || !isAlive) {
            if (winnerId[roundNum - 1] == playerOffset)
                g.drawPixmap(Assets.happy, 515, 235);
            else if(!isAlive)
                g.drawPixmap(Assets.sad, 515, 235);
        }

        androidJoystick.draw();
        timedCircularButton.draw();
    }

    private void calculateGameResultPixmap() {
        int[] results = new int[players.length];
        int max = 0;
        for(int i=0; i<players.length; i++)
            results[i] = 0;
        for(int i=0; i<ROUNDS; i++) {
            int cur = ++results[winnerId[i]];
            if(results[max] < cur)
                max = winnerId[i];
        }
        boolean oneWinner = true;
        for(int i=0; i<players.length; i++)
            if(results[i] == results[max] && i != max) {
                oneWinner = false;
                break;
            }
        if(results[max] == results[playerOffset]) {
            if(oneWinner)
                gameResultPixmap = Assets.happy;
            else
                gameResultPixmap = Assets.neutral;
        } else {
            gameResultPixmap = Assets.sad;
        }
    }

    private Pixmap gameResultPixmap;

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
            if(backButton.inBounds(event) && event.type == Input.TouchEvent.TOUCH_UP && backButton.isEnabled())
                privateBack();
            if(endGame)
                continue;
            if (timedCircularButton.inBounds(event) && event.type == Input.TouchEvent.TOUCH_UP) {
                if (timedCircularButton.isEnabled()) {
                    shouldAttack = true;
//  TODO va fanno in server e client screen:
//                  if(Settings.soundEnabled)
  //                      Assets.attackEnabled.play(Settings.volume);
                    timedCircularButton.resetTime();
                } else if(Settings.soundEnabled)
                    Assets.attackDisabled.play(Settings.volume);
            }
        }

        updateDyingRadius();
    }

    private void drawArena(){
        DrawableComponent arenaDrawable = (DrawableComponent) status.arena.getComponent(Component.Type.Drawable);
        arenaDrawable.draw();
    }


    private void drawList(MyList<? extends Entity> list){
        DrawableComponent comp;
        for(Entity e : list)
            if((comp = (DrawableComponent)e.getComponent(Component.Type.Drawable)) != null)
                comp.draw();
        list.resetIterator();
    }

    @Override
    public void back() {


    }
    private void privateBack() {
        androidGame.setScreen(new CustomizeGameCharacterScreen((GoogleAndroidGame)androidGame));
    }

    protected void initCharacterSettings(float r){
        drawableComponentFactory.resetFactory();
        drawableComponentFactory.setHeight((int)(r*2)).setWidth((int)(r*2))
                .setShape(DrawableComponentFactory.DrawableShape.CIRCLE);
    }

    protected void initPowerupSettings(int side){
        drawableComponentFactory.resetFactory();
        drawableComponentFactory.setShape(DrawableComponentFactory.DrawableShape.RECTANGLE)
                .setHeight(side).setWidth(side).setStroke(6,Color.BLACK);
    }

    protected void initArenaSettings(){
        drawableComponentFactory.resetFactory();
        int x = frameWidth/2, y = frameHeight/2;

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

    protected Entity createArena(){
        Entity arena = new Entity();

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

    private void updateDyingRadius() {
        Iterator<GameCharacter> iterator = status.dying.iterator();

        while(iterator.hasNext()) {
            GameCharacter ch = iterator.next();
            DrawableComponent component = (DrawableComponent)ch.getComponent(Component.Type.Drawable);
            int diam = component.getHeight();
            component.setHeight(diam - 1);

            if(diam <= 0)
                iterator.remove();

        }
        status.dying.resetIterator();
    }


    protected Powerup createPowerup(int x, int y, short id){
        Powerup powerup = null;
        switch (id){
            case RadialForcePowerup.id:
                powerup = new RadialForcePowerup(status);
                break;
        }

        drawableComponentFactory.setPixmap(Assets.skins[id]).setX(x).setY(y).setOwner(powerup);
        //TODO Assets.powerups[id]

        powerup.addComponent(drawableComponentFactory.makeComponent());
        return powerup;
    }

    protected void startRound() {
        roundNum++;
        if (roundNum > ROUNDS) {
            endGame = true;
            backButton.enable(true);
            androidJoystick.enable(false);
            timedCircularButton.enable(false);
            return;
        }
        isAlive = true;
        endRound = false;
        status = new GameStatus();
        initArenaSettings();
        status.setArena(createArena());
        initCharacterSettings(radiusCharacter);
        GameCharacter[] characters = new GameCharacter[players.length];
        for (int i=0; i<characters.length; i++)
            characters[i] = createCharacter(spawnPositions[i][0], spawnPositions[i][1], Assets.skins[skins[i]], (short)i);
        status.setCharacters(characters);
        status.setPlayerOne(characters[playerOffset]);
    }
}
