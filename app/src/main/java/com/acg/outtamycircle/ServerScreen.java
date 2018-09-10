package com.acg.outtamycircle;

import com.acg.outtamycircle.contactphase.ContactHandler;
import com.acg.outtamycircle.entitycomponent.Component;
import com.acg.outtamycircle.entitycomponent.DrawableComponent;
import com.acg.outtamycircle.entitycomponent.PhysicsComponentFactory;
import com.acg.outtamycircle.entitycomponent.impl.GameCharacter;
import com.acg.outtamycircle.entitycomponent.impl.LiquidFunPhysicsComponent;
import com.acg.outtamycircle.network.GameMessage;
import com.acg.outtamycircle.network.googleimpl.MyGoogleRoom;
import com.acg.outtamycircle.utilities.Converter;
import com.badlogic.androidgames.framework.Pixmap;
import com.badlogic.androidgames.framework.impl.AndroidGame;
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

    private float sxX, dxX, downY, upY;
    private float arenaX, arenaY;

    public ServerScreen(AndroidGame game, MyGoogleRoom myGoogleRoom, String[] players, short[] skins, int[][] spawnPositions, byte[] attacks) {
        super(game, myGoogleRoom, players, skins, spawnPositions);
        this.attacks = attacks;

        world = new World(0, 0);

        /*Powerup pu = EntityFactory.createServerDefaultPowerup(20, frameWidth/2, frameHeight/2);
        status.setPowerup(pu);*/

        initCharacterSettings(40);
        GameCharacter[] characters = new GameCharacter[players.length];
        for (int i = 0; i < characters.length; i++)
            characters[i] = createCharacter(spawnPositions[i][0], spawnPositions[i][1], Assets.skins[skins[i]], (short)i);

        status.setCharacters(characters);

        int i;
        for (i = 0; i < players.length; i++)
            if(myGoogleRoom.getPlayerId().equals(players[i]))
                break;
        status.setPlayerOne(characters[i]);

        ContactHandler contactHandler = new ContactHandler();
        contactHandler.init();

        world.setContactListener(contactHandler);

        initArenaBounds();

        //TODO comunica posizioni etc.
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        GameCharacter ch;

        for (GameMessage message : myGoogleRoom.getServerClientMessageHandler().getMessages()) {
            switch (interpreter.getType(message)){
                case MOVE_CLIENT:
                    ch = status.characters[interpreter.getObjectId(message)];
                    LiquidFunPhysicsComponent comp = (LiquidFunPhysicsComponent)ch.getComponent(Component.Type.Physics);
                    comp.applyForce(interpreter.getPosX(message), interpreter.getPosY(message));
                    break;
                case ATTACK:
                    break;
            }
        }

        LiquidFunPhysicsComponent comp = (LiquidFunPhysicsComponent)status.playerOne.getComponent(Component.Type.Physics);
        comp.applyForce(androidJoystick.getNormX(), androidJoystick.getNormY());


        world.step(deltaTime, VELOCITY_ITERATIONS, POSITION_ITERATIONS, 0);

        updateDrawablesPosition();
        updateCharactersStatus();
        updateDyingRadius();

        //TODO invia posizione

        sendStatus();
        //recv
    }

    @Override
    public void setup(){
        Converter.setScale(frameWidth, frameHeight);
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

        if(chX > dxX || chX < sxX || chY > upY || chY < downY)
            return false;

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
    protected GameCharacter createCharacter(int x, int y, Pixmap pixmap, short id){
        GameCharacter gc = super.createCharacter(x, y, pixmap, id);

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

    private void sendStatus(){
        DrawableComponent shape;
        GameMessage message = new GameMessage(); //TODO usare pool

        for(GameCharacter ch : status.living){
            shape = (DrawableComponent)ch.getComponent(Component.Type.Drawable);

            interpreter.makeMoveServerMessage(message, ch.getObjectId(), shape.getX(), shape.getY(), 0); //TODO getX(): int, rotation
            myGoogleRoom.getServerClientMessageHandler().putInBuffer(message);
        }
        status.living.resetIterator();
        myGoogleRoom.getServerClientMessageHandler().broadcastUnreliable();
    }

    private void initArenaBounds(){
        double l = (arenaRadius*Math.sqrt(2))/2;
        arenaX = Converter.frameToPhysics(frameHeight/2);
        arenaY = Converter.frameToPhysics(frameWidth/2);
        dxX = (float)(arenaX + l);
        sxX = (float)(arenaX - l);
        upY = (float)(arenaY + l);
        downY = (float)(arenaY - l);
    }
}