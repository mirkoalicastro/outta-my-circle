package com.acg.outtamycircle;

import android.graphics.Color;
import android.graphics.Shader;
import android.util.Log;

import com.acg.outtamycircle.contactphase.ContactHandler;
import com.acg.outtamycircle.entitycomponent.Component;
import com.acg.outtamycircle.entitycomponent.DrawableComponent;
import com.acg.outtamycircle.entitycomponent.DrawableComponentFactory;
import com.acg.outtamycircle.entitycomponent.PhysicsComponentFactory;
import com.acg.outtamycircle.entitycomponent.impl.Arena;
import com.acg.outtamycircle.entitycomponent.impl.GameCharacter;
import com.acg.outtamycircle.entitycomponent.impl.LiquidFunPhysicsComponent;
import com.acg.outtamycircle.network.googleimpl.MyGoogleRoom;
import com.acg.outtamycircle.utilities.Converter;
import com.badlogic.androidgames.framework.impl.AndroidEffect;
import com.badlogic.androidgames.framework.impl.AndroidGame;
import com.badlogic.androidgames.framework.impl.ComposerAndroidEffect;
import com.badlogic.androidgames.framework.impl.RadialGradientEffect;
import com.google.fpl.liquidfun.BodyType;
import com.google.fpl.liquidfun.CircleShape;
import com.google.fpl.liquidfun.World;

import java.util.Iterator;

public class ServerScreen extends ClientServerScreen {
    private final World world;

    private static final float TIME_STEP = 1 / 60f;   //60 fps
    private static final int VELOCITY_ITERATIONS = 8;
    private static final int POSITION_ITERATIONS = 3;
    protected final byte[] attacks;
    private final PhysicsComponentFactory physicsComponentFactory = new PhysicsComponentFactory();

    public ServerScreen(AndroidGame game, MyGoogleRoom myGoogleRoom, String[] players, short[] skins, int[][] spawnPositions, byte[] attacks) {
        super(game, myGoogleRoom, players, skins, spawnPositions);
        this.attacks = attacks;

        world = new World(0, 0);

        /*Powerup pu = EntityFactory.createServerDefaultPowerup(20, frameWeight/2, frameHeight/2);
        status.setPowerup(pu);*/

        initCharacterSettings(40);

        GameCharacter[] characters = {
                createCharacter(spawnPositions[0][0], spawnPositions[0][1], Color.GREEN),
                createCharacter(spawnPositions[1][0], spawnPositions[1][1], Color.WHITE),
                createCharacter(spawnPositions[2][0], spawnPositions[2][1], Color.YELLOW),
                createCharacter(spawnPositions[3][0], spawnPositions[3][1], Color.RED),
        };

        status.setCharacters(characters);
        status.setPlayerOne(characters[0]);

        ContactHandler contactHandler = new ContactHandler();
        contactHandler.init();

        world.setContactListener(contactHandler);

        //TODO comunica posizioni etc.
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        LiquidFunPhysicsComponent comp = (LiquidFunPhysicsComponent)status.playerOne.getComponent(Component.Type.Physics);
        comp.applyForce(androidJoystick.getNormX(), androidJoystick.getNormY());

        world.step(deltaTime, VELOCITY_ITERATIONS, POSITION_ITERATIONS, 0);

        updateDrawablesPosition();
        updateCharactersStatus();
        updateDyingRadius();

        //TODO invia posizione
    }

    @Override
    public void setup(){
        Converter.setScale(frameWeight, frameHeight);
    }

    /**
     * Distribuzione di n cordinate equidistanti su di una circonferenza
     *
     * @param r raggio
     * @param w fattore di shift sull'asse x
     * @param h fattore di shift sull'asse y
     * @param n numero di giocatori
     * @return
     */
    private int[][] distributePoints(int r, int w, int h, int n){
        int[][] points = new int[n][2];
        double x, y;
        double p = (Math.PI*2)/n;
        double theta = Math.PI/2;

        for(int i=0 ; i<n ; i++){
            x = Math.cos(theta)*r;
            y = Math.sin(theta)*r;


            points[i][0] = (int)x + w;
            points[i][1] = (int)y + h;

            theta += p;
        }
        return points;
    }

    private void updateCharactersStatus(){
        Iterator<GameCharacter> iterator = status.living.iterator();
        while(iterator.hasNext()) {
            GameCharacter character = iterator.next();
            if (isOut(character)) {
                status.dying.add(character);
                iterator.remove();
                disablePhysicsComponent(character);
            }
        }
        status.living.resetIterator();
    }

    private boolean isOut(GameCharacter ch){
        LiquidFunPhysicsComponent circle = (LiquidFunPhysicsComponent) ch.getComponent(Component.Type.Physics);
        float chX = circle.getX();
        float chY = circle.getY();

        DrawableComponent arenaDrawable = (DrawableComponent)status.arena.getComponent(Component.Type.Drawable);
        float arenaX = Converter.frameToPhysics(arenaDrawable.getX());
        float arenaY = Converter.frameToPhysics(arenaDrawable.getY());

        float deltaX = (chX - arenaX)*(chX - arenaX);
        float deltaY = (chY - arenaY)*(chY - arenaY);

        float delta = (float)Math.sqrt(deltaX + deltaY);

        return delta > Converter.frameToPhysics(arenaRadius) + circle.getWidth()/4;
    }

    @Override
    protected void initCharacterSettings(float r){
        super.initCharacterSettings(r);

        physicsComponentFactory.setWorld(world).setDensity(1f).setFriction(1f).setRestitution(1f)
                .setType(BodyType.dynamicBody).setShape(new CircleShape())
                .setRadius(Converter.frameToPhysics(r)).setAwake(true)
                .setBullet(true).setSleepingAllowed(true);
    }

    @Override
    protected GameCharacter createCharacter(int x, int y, int color){
        GameCharacter gc = super.createCharacter(x, y, color);

        physicsComponentFactory.setPosition(Converter.frameToPhysics(x), Converter.frameToPhysics(y)).setOwner(gc);
        gc.addComponent(physicsComponentFactory.makeComponent());

        return gc;
    }

    private void updateDrawablesPosition(){
        LiquidFunPhysicsComponent component;
        DrawableComponent shape;

        for(GameCharacter ch : status.living) {
            component = (LiquidFunPhysicsComponent)ch.getComponent(Component.Type.Physics);

            shape = (DrawableComponent)ch.getComponent(Component.Type.Drawable);
            shape.setX((int) Converter.physicsToFrame(component.getX()))
                    .setY((int) Converter.physicsToFrame(component.getY()));
        }
        status.living.resetIterator();
    }

    private void updateDyingRadius(){
        int diam;
        DrawableComponent component = null;
        Iterator<GameCharacter> iterator = status.dying.iterator();

        while(iterator.hasNext()) {
            GameCharacter ch = iterator.next();
            component = (DrawableComponent)ch.getComponent(Component.Type.Drawable);
            diam = component.getHeight();
            component.setHeight(diam - 1);

            if(diam <= 0) iterator.remove();
        }
        status.dying.resetIterator();
    }

    private void disablePhysicsComponent(GameCharacter ch){
        LiquidFunPhysicsComponent component = (LiquidFunPhysicsComponent)ch.getComponent(Component.Type.Physics);
        component.deleteBody();
    }
}