package com.acg.outtamycircle;

import com.acg.outtamycircle.contactphase.ContactHandler;
import com.acg.outtamycircle.entitycomponent.Component;
import com.acg.outtamycircle.entitycomponent.DrawableComponent;
import com.acg.outtamycircle.entitycomponent.impl.components.LiquidFunPhysicsComponent;
import com.acg.outtamycircle.entitycomponent.impl.factories.PhysicsComponentFactory;
import com.acg.outtamycircle.entitycomponent.impl.gameobjects.GameCharacter;
import com.acg.outtamycircle.entitycomponent.impl.gameobjects.Powerup;
import com.acg.outtamycircle.network.GameMessage;
import com.acg.outtamycircle.network.googleimpl.MyGoogleRoom;
import com.acg.outtamycircle.utilities.Converter;
import com.acg.outtamycircle.utilities.PowerupRandomManager;
import com.badlogic.androidgames.framework.Pixmap;
import com.badlogic.androidgames.framework.impl.AndroidGame;
import com.google.fpl.liquidfun.BodyType;
import com.google.fpl.liquidfun.CircleShape;
import com.google.fpl.liquidfun.PolygonShape;
import com.google.fpl.liquidfun.World;

import java.util.Iterator;

public class ServerScreen extends ClientServerScreen {
    private final World world;

    private static final int VELOCITY_ITERATIONS = 8;
    private static final int POSITION_ITERATIONS = 3;
    protected final int[] attacks;
    private final PhysicsComponentFactory physicsComponentFactory;
    private final ContactHandler contactHandler;
    private final PowerupRandomManager powerupRandomManager;

    private void startRound() { //TODO porta sopra
        if(startAt > System.currentTimeMillis())
            return;
        if(status != null)
            for(GameCharacter gameCharacter: status.living)
                ((LiquidFunPhysicsComponent)gameCharacter.getComponent(Component.Type.Physics)).deleteBody();
        roundNum++;
        if(roundNum > ROUNDS) {
            endGame = true;
            return;
        }
        isAlive = true;
        endRound = false;
        status = new GameStatus();
        initArenaSettings();
        status.setArena(createArena());
        initCharacterSettings(radiusCharacter);
        GameCharacter[] characters = new GameCharacter[players.length];
        for (int i = 0; i < characters.length; i++)
            characters[i] = createCharacter(spawnPositions[i][0], spawnPositions[i][1], Assets.skins[skins[i]], (short)i);
        status.setCharacters(characters);

        status.setPlayerOne(characters[playerOffset]);
    }

    public ServerScreen(AndroidGame game, MyGoogleRoom myGoogleRoom, String[] players, int[] skins, int[][] spawnPositions, int[] attacks, int playerOffset) {
        super(game, myGoogleRoom, players, skins, spawnPositions, playerOffset);
        this.attacks = attacks;

        world = new World(0, 0);
        physicsComponentFactory = new PhysicsComponentFactory(world);

        startRound();
        roundNum--;

        contactHandler = new ContactHandler();
        contactHandler.init(status);

        world.setContactListener(contactHandler); //TODO

        float squareHalfSide = Converter.frameToPhysics((float)(arenaRadius*Math.sqrt(2)/2));
        arenaX = Converter.frameToPhysics(frameWidth/2);
        arenaY = Converter.frameToPhysics(frameHeight/2);
        rightX = arenaX + squareHalfSide;
        leftX = arenaX - squareHalfSide;
        topY = arenaY + squareHalfSide;
        bottomY = arenaY - squareHalfSide;
        threshold = Converter.frameToPhysics(arenaRadius) + Converter.frameToPhysics(radiusCharacter*2)/4;

        powerupRandomManager = new PowerupRandomManager(arenaX, arenaY, Converter.frameToPhysics(arenaRadius));

        //TODO comunica posizioni etc.
    }

    private final float arenaX, arenaY;
    private final float rightX, leftX, topY, bottomY;
    private final float threshold;

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if(endGame)
            return;

        if(endRound) {
            startRound();
            return;
        }

        for (GameMessage message : networkMessageHandler.getMessages()) {
            GameCharacter gameCharacter;
            switch (interpreter.getType(message)){
                case MOVE_CLIENT:
                    gameCharacter = status.characters[interpreter.getObjectId(message)];
                    LiquidFunPhysicsComponent comp = (LiquidFunPhysicsComponent)gameCharacter.getComponent(Component.Type.Physics);
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
        updatePowerupsStatus();

        sendStatus();
    }

    @Override
    public void setup(){
        Converter.setScale(frameWidth, frameHeight);
    }

    private void updateCharactersStatus(){
        Iterator<GameCharacter> iterator = status.living.iterator();
        GameMessage message = null;
        while(iterator.hasNext()) {
            GameCharacter character = iterator.next();
            short charId = character.getObjectId();
            if(status.living.size()==1) {
                winnerId[roundNum-1] = charId;
                break;
            }
            if (isOut(character)) {
                if(charId == playerOffset)
                    isAlive = false;
                status.dying.add(character);
                if(message == null)
                    message = GameMessage.createInstance();
                interpreter.makeDestroyMessage(message, character.getObjectId());
                networkMessageHandler.putInBuffer(message);
                GameMessage.deleteInstance(message);
                iterator.remove();
                disablePhysicsComponent(character); //TODO
            }
        }
        if(message != null) {
            networkMessageHandler.broadcastReliable();
            GameMessage.deleteInstance(message);
        }
        status.living.resetIterator();
    }

    private boolean isOut(GameCharacter ch){
        LiquidFunPhysicsComponent circle = (LiquidFunPhysicsComponent) ch.getComponent(Component.Type.Physics);
        float chX = circle.getX();
        float chY = circle.getY();

        if(chX <= rightX && chX >= leftX && chY <= topY && chY >= bottomY)
            return false;

        float deltaX = (chX - arenaX) * (chX - arenaX);
        float deltaY = (chY - arenaY) * (chY - arenaY);

        float delta = (float)Math.sqrt(deltaX + deltaY);

        return delta > threshold;
    }

    @Override
    protected void initCharacterSettings(float r){
        super.initCharacterSettings(r);

        physicsComponentFactory.setDensity(1f).setFriction(1f).setRestitution(1f)
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

    private void disablePhysicsComponent(GameCharacter ch){
        LiquidFunPhysicsComponent component = (LiquidFunPhysicsComponent)ch.getComponent(Component.Type.Physics);
        component.deleteBody();
    }

    @Override
    protected void initPowerupSettings(int side){
        super.initPowerupSettings(side);

        physicsComponentFactory.resetFactory();
        physicsComponentFactory.setDensity(0f).setFriction(0f).setRestitution(0f)
                .setType(BodyType.dynamicBody).setShape(new PolygonShape());
    }

    protected Powerup createPowerup(float x, float y, short id){
        Powerup powerup = super.createPowerup((int)Converter.physicsToFrame(x),
                (int)Converter.physicsToFrame(y), id);

        physicsComponentFactory.setOwner(powerup)
                .setPosition(x, y);
        powerup.addComponent(physicsComponentFactory.makeComponent());

        return powerup;
    }

    private long startAt;

    private void sendStatus(){
        DrawableComponent shape;
        GameMessage message = GameMessage.createInstance();

        if(winnerId[roundNum-1] != -1) {
            interpreter.makeEndMessage(message, (short)winnerId[roundNum-1]);
            networkMessageHandler.putInBuffer(message);
            networkMessageHandler.broadcastReliable();
            GameMessage.deleteInstance(message);
            startAt = System.currentTimeMillis()+5000;
            endRound = true;
            return;
        }

        for(GameCharacter ch : status.living){
            shape = (DrawableComponent)ch.getComponent(Component.Type.Drawable);
            interpreter.makeMoveServerMessage(message, ch.getObjectId(), shape.getX(), shape.getY(), 0); //TODO getX(): int, rotation
            networkMessageHandler.putInBuffer(message);
        }

        status.living.resetIterator();

        GameMessage.deleteInstance(message);

        networkMessageHandler.broadcastUnreliable();
    }

    private void updatePowerupsStatus(){
        if(powerupRandomManager.randomBoolean()){
            Powerup powerup = createPowerup(powerupRandomManager.randomX(),
                    powerupRandomManager.randomY(),
                    powerupRandomManager.randomPowerup());

            status.inactives.add(powerup);
        }

        for(Powerup pu : status.actives)
            if(!pu.isEnded())
                pu.start();
        status.actives.resetIterator();
    }
}