package com.acg.outtamycircle;

import android.util.Log;

import com.acg.outtamycircle.contactphase.ContactHandler;
import com.acg.outtamycircle.entitycomponent.Component;
import com.acg.outtamycircle.entitycomponent.DrawableComponent;
import com.acg.outtamycircle.entitycomponent.PhysicsComponent;
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

    private short winner = -1;

    public ServerScreen(AndroidGame game, MyGoogleRoom myGoogleRoom, String[] players, byte[] skins, int[][] spawnPositions, byte[] attacks, int playerOffset) {
        super(game, myGoogleRoom, players, skins, spawnPositions, playerOffset);
        this.attacks = attacks;

        world = new World(0, 0);

        /*Powerup pu = EntityFactory.createServerDefaultPowerup(20, frameWeight/2, frameHeight/2);
        status.setPowerup(pu);*/

        int radiusCharacter = 40;

        initCharacterSettings(radiusCharacter);
        GameCharacter[] characters = new GameCharacter[players.length];
        for (int i = 0; i < characters.length; i++)
            characters[i] = createCharacter(spawnPositions[i][0], spawnPositions[i][1], Assets.skins[skins[i]], (short)i);

        status.setCharacters(characters);

        status.setPlayerOne(characters[playerOffset]);

        ContactHandler contactHandler = new ContactHandler();
        contactHandler.init();

        world.setContactListener(contactHandler);

        float squareHalfSide = Converter.frameToPhysics((float)(arenaRadius*Math.sqrt(2)/2));
        arenaX = Converter.frameToPhysics(frameWidth/2);
        arenaY = Converter.frameToPhysics(frameHeight/2);
        rightX = arenaX + squareHalfSide;
        leftX = arenaX - squareHalfSide;
        topY = arenaY + squareHalfSide;
        bottomY = arenaY - squareHalfSide;
        threshold = Converter.frameToPhysics(arenaRadius) + Converter.frameToPhysics(radiusCharacter*2)/4;

        //TODO comunica posizioni etc.
    }

    private final float arenaX, arenaY;
    private final float rightX, leftX, topY, bottomY;
    private final float threshold;

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        Log.d("SERVERINA","io sto a " + ((LiquidFunPhysicsComponent)status.playerOne.getComponent(Component.Type.Physics)).getX());
        GameCharacter ch;

        for (GameMessage message : myGoogleRoom.getNetworkMessageHandlerImpl().getMessages()) {
            switch (interpreter.getType(message)){
                case MOVE_CLIENT:
                    Log.d("JUANNINO", "ho ricevuto move_client");
                    ch = status.characters[interpreter.getObjectId(message)];
                    LiquidFunPhysicsComponent comp = (LiquidFunPhysicsComponent)ch.getComponent(Component.Type.Physics);
                    comp.applyForce(interpreter.getPosX(message), interpreter.getPosY(message));
                    break;
                case ATTACK:
                    Log.d("JUANNINO", "ho ricevuto attack");
                    break;
            }
        }

        LiquidFunPhysicsComponent comp = (LiquidFunPhysicsComponent)status.playerOne.getComponent(Component.Type.Physics);
        Log.d("SERVERA", "applico forza " + androidJoystick.getNormX());
        comp.applyForce(androidJoystick.getNormX(), androidJoystick.getNormY());


        Log.d("SERVERA","deltatime " + deltaTime);
        world.step(deltaTime, VELOCITY_ITERATIONS, POSITION_ITERATIONS, 0);

        updateDrawablesPosition();
        updateCharactersStatus();
        updateDyingRadius();

        //TODO invia posizione

        sendStatus();

        if(winner!=-1)
            back();

    }

    @Override
    public void setup(){
        Converter.setScale(frameWidth, frameHeight);
    }

    private void updateCharactersStatus(){
        Iterator<GameCharacter> iterator = status.living.iterator();
        short lastAlive = -1, charId = -1;
        while(iterator.hasNext()) {
            GameCharacter character = iterator.next();
            charId = character.getObjectId();
            if (isOut(character)) {
                status.dying.add(character);
                GameMessage message = GameMessage.createInstance();
                if(lastAlive==-1)
                    lastAlive = charId;
                interpreter.makeDestroyMessage(message, character.getObjectId());
                myGoogleRoom.getNetworkMessageHandlerImpl().putInBuffer(message);
                GameMessage.deleteInstance(message);
                iterator.remove();
                disablePhysicsComponent(character); //TODO
                Log.d("AGLIA", "sei fuori!");
            } else {
                lastAlive = charId;
            }
        }
        status.living.resetIterator();

        if(status.living.size()<=1)
            winner = lastAlive;
    }

    private boolean isOut(GameCharacter ch){
        LiquidFunPhysicsComponent circle = (LiquidFunPhysicsComponent) ch.getComponent(Component.Type.Physics);
        float chX = circle.getX();
        float chY = circle.getY();

        if(chX <= rightX && chX >= leftX && chY <= topY && chY >= bottomY)
            return false;

        //Log.d("ATTENZIONE","arenax: " +arenaX + ", arenay: " + arenaY);

        float deltaX = (chX - arenaX) * (chX - arenaX);
        float deltaY = (chY - arenaY) * (chY - arenaY);

        float delta = (float)Math.sqrt(deltaX + deltaY);

        //Log.d("ATTENZIONE", "delta: " + delta + "\nthreshold: " + threshold);
        //Log.d("ATTENZIONE", "Il corpo a " + chX + "," + chY + "  e' morto!");

        return delta > threshold;
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

            if(diam <= 0)
                iterator.remove();

        }
        status.dying.resetIterator();
    }

    private void disablePhysicsComponent(GameCharacter ch){
        LiquidFunPhysicsComponent component = (LiquidFunPhysicsComponent)ch.getComponent(Component.Type.Physics);
        component.deleteBody();
    }

    private void sendStatus(){
        DrawableComponent shape;
        GameMessage message = GameMessage.createInstance(); //TODO usare pool

        if(winner!=-1){
            interpreter.makeEndMessage(message, winner);
            myGoogleRoom.getNetworkMessageHandlerImpl().putInBuffer(message);
            myGoogleRoom.getNetworkMessageHandlerImpl().broadcastReliable();
            GameMessage.deleteInstance(message);
            return;
        }

        for(GameCharacter ch : status.living){
            shape = (DrawableComponent)ch.getComponent(Component.Type.Drawable);
            interpreter.makeMoveServerMessage(message, ch.getObjectId(), shape.getX(), shape.getY(), 0); //TODO getX(): int, rotation
            myGoogleRoom.getNetworkMessageHandlerImpl().putInBuffer(message);
            Log.d("JUANNINO", "ho creato un move_server");
        }

        status.living.resetIterator();

        GameMessage.deleteInstance(message);

        myGoogleRoom.getNetworkMessageHandlerImpl().broadcastUnreliable();
        //myGoogleRoom.getNetworkMessageHandlerImpl().clearBuffer();
    }
}