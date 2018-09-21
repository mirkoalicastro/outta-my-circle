package com.acg.outtamycircle;

import com.acg.outtamycircle.contactphase.ContactHandler;
import com.acg.outtamycircle.entitycomponent.AttackComponent;
import com.acg.outtamycircle.entitycomponent.Component;
import com.acg.outtamycircle.entitycomponent.DrawableComponent;
import com.acg.outtamycircle.entitycomponent.PhysicsComponent;
import com.acg.outtamycircle.entitycomponent.impl.components.LiquidFunPhysicsComponent;
import com.acg.outtamycircle.entitycomponent.impl.factories.AttackComponentFactory;
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
import com.google.fpl.liquidfun.World;

import java.util.Iterator;

public class ServerScreen extends ClientServerScreen {
    private final World world;

    private static final int VELOCITY_ITERATIONS = 8;
    private static final int POSITION_ITERATIONS = 3;
    private final int[] attacks;
    private final PhysicsComponentFactory physicsComponentFactory;
    private final PhysicsComponentFactory powerupPhysicsFactory;
    private final ContactHandler contactHandler; //keep the reference so that the object isn't deallocated and LiquidFun can work
    private final PowerupRandomManager powerupRandomManager;
    private final float arenaX, arenaY;
    private final float rightX, leftX, topY, bottomY;
    private final float threshold;

    private int messagesInBuffer; //counter for reliable messages buffer
    private int nextId;

    public ServerScreen(AndroidGame game, MyGoogleRoom myGoogleRoom, String[] players, int[] skins, int[][] spawnPositions, int[] attacks, int playerOffset) {
        super(game, myGoogleRoom, players, skins, spawnPositions, playerOffset);
        Converter.setScale(frameWidth, frameHeight);
        this.attacks = attacks;

        world = new World(0, 0);
        physicsComponentFactory = new PhysicsComponentFactory(world);
        powerupPhysicsFactory = new PhysicsComponentFactory(world);
        powerupPhysicsFactory.setShape(new CircleShape()).setHeight(Converter.frameToPhysics(RADIUS_CHARACTER)/2f)
                .setWidth(Converter.frameToPhysics(RADIUS_CHARACTER)/2f).setRadius(Converter.frameToPhysics(RADIUS_CHARACTER))
                .setBullet(true).setAwake(true).setSleepingAllowed(true).setType(BodyType.dynamicBody)
                .setDensity(0.000000001f).setRestitution(0.000000001f).setFriction(0.000000001f); //very small values supported by LiquidFun (NO Float.MIN_VALUE)

        contactHandler = new ContactHandler();
        world.setContactListener(contactHandler);

        float squareHalfSide = Converter.frameToPhysics((float)(arenaRadius*Math.sqrt(2)/2));
        arenaX = Converter.frameToPhysics(frameWidth/2);
        arenaY = Converter.frameToPhysics(frameHeight/2);
        rightX = arenaX + squareHalfSide;
        leftX = arenaX - squareHalfSide;
        topY = arenaY + squareHalfSide;
        bottomY = arenaY - squareHalfSide;
        threshold = Converter.frameToPhysics(arenaRadius) + Converter.frameToPhysics(RADIUS_CHARACTER*2)/4;

        powerupRandomManager = new PowerupRandomManager(arenaX, arenaY, Converter.frameToPhysics(arenaRadius));

        nextId = players.length;

        startRound();
        roundNum--;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if(endGame)
            return;

        if(endRound) {
            startRound();
            return;
        }

        // attack handling
        Iterator<AttackComponent> attackIterator = status.activeAttacks.iterator();
        while(attackIterator.hasNext()){
            AttackComponent attackComponent = attackIterator.next();
            if(attackComponent.isActive())
                attackComponent.attack();
            else {
                attackComponent.stop();
                attackIterator.remove();
            }
        }
        status.activeAttacks.resetIterator();

        boolean receivedAtLeastOne = false;
        // read inbox
        for (GameMessage message : networkMessageHandler.getMessages()) {
            receivedAtLeastOne = true;
            GameCharacter gameCharacter;
            switch (interpreter.getType(message)){
                case MOVE_CLIENT:
                    gameCharacter = status.characters[interpreter.getObjectId(message)];
                    LiquidFunPhysicsComponent comp = (LiquidFunPhysicsComponent)gameCharacter.getComponent(Component.Type.Physics);
                    comp.applyForce(interpreter.getPosX(message), interpreter.getPosY(message));
                    break;
                case ATTACK:
                    if(Settings.soundEnabled)
                        Assets.attackEnabled.play(Settings.volume);
                    gameCharacter = null;
                    int objId = interpreter.getObjectId(message);
                    for(GameCharacter character: status.living) {
                        if(character.getObjectId() == objId) {
                            gameCharacter = character;
                            break;
                        }
                    }
                    if(gameCharacter != null) {
                        AttackComponent attackComponent = (AttackComponent) gameCharacter.getComponent(Component.Type.Attack);
                        attackComponent.start(status, interpreter.getPosX(message), interpreter.getPosY(message));
                        networkMessageHandler.putInBuffer(message);
                        messagesInBuffer++;
                        status.activeAttacks.add(attackComponent);
                    }
                    break;
            }
        }

        // get server player input
        if(shouldAttack){
            if(Settings.soundEnabled)
                Assets.attackEnabled.play(Settings.volume);
            GameCharacter gameCharacter = status.characters[playerOffset];
            AttackComponent attackComponent = (AttackComponent) gameCharacter.getComponent(Component.Type.Attack);
            float x = androidJoystick.getNormX();
            float y = androidJoystick.getNormY();
            attackComponent.start(status, x, y);
            GameMessage message = GameMessage.createInstance();
            interpreter.makeAttackMessage(message, playerOffset, (int) x, (int) y);
            networkMessageHandler.putInBuffer(message);
            messagesInBuffer++;
            GameMessage.deleteInstance(message);
            status.activeAttacks.add(attackComponent);
            shouldAttack = false;
        }
        LiquidFunPhysicsComponent comp = (LiquidFunPhysicsComponent)status.playerOne.getComponent(Component.Type.Physics);
        comp.applyForce(androidJoystick.getNormX(), androidJoystick.getNormY());

        // update world
        world.step(deltaTime, VELOCITY_ITERATIONS, POSITION_ITERATIONS, 0);

        // update game status
        updateDrawablesPosition();
        updateCharactersStatus();
        updatePowerupsStatus(deltaTime);

        // notify the client
        sendStatus();

        if(receivedAtLeastOne)
            updateLastTimeReceived();
    }

    private void updateCharactersStatus(){
        Iterator<GameCharacter> iterator = status.living.iterator();
        GameMessage message = null;
        while(iterator.hasNext()) {
            GameCharacter character = iterator.next();
            int charId = character.getObjectId();
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
                interpreter.makeDestroyMessage(message, character.getObjectId(), roundNum);
                networkMessageHandler.putInBuffer(message);
                GameMessage.deleteInstance(message);
                iterator.remove();
                disablePhysicsComponent(character);
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
    protected GameCharacter createCharacter(int x, int y, Pixmap pixmap, int id){
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
        component.delete();
    }

    @Override
    protected Powerup createPowerup(int x, int y, int powerupId, int objectId){
        Powerup powerup = super.createPowerup((int)Converter.physicsToFrame(x),
                (int)Converter.physicsToFrame(y), powerupId, objectId);

        powerupPhysicsFactory.setOwner(powerup)
                .setPosition(x, y);
        powerup.addComponent(powerupPhysicsFactory.makeComponent());

        return powerup;
    }

    private void sendStatus(){
        DrawableComponent shape;
        GameMessage message = GameMessage.createInstance();

        if(winnerId[roundNum-1] != -1) {
            interpreter.makeEndMessage(message, winnerId[roundNum-1]);
            networkMessageHandler.putInBuffer(message);
            networkMessageHandler.broadcastReliable();
            GameMessage.deleteInstance(message);
            startAt = System.currentTimeMillis()+5000;
            endRound = true;
            return;
        }

        /* Reliable Messages */

        if(messagesInBuffer>0)
            networkMessageHandler.broadcastReliable();

        /* Unreliable Messages */

        if(status.collisionDetected) {
            interpreter.makeCollisionMessage(message);
            networkMessageHandler.putInBuffer(message);
            status.collisionDetected = false;
        }


        for(GameCharacter ch : status.living){
            shape = (DrawableComponent)ch.getComponent(Component.Type.Drawable);
            interpreter.makeMoveServerMessage(message, ch.getObjectId(), shape.getX(), shape.getY(), 0);
            networkMessageHandler.putInBuffer(message);
        }
        status.living.resetIterator();

        GameMessage.deleteInstance(message);

        networkMessageHandler.broadcastUnreliable();

        messagesInBuffer = 0;
    }

    private void updatePowerupsStatus(float deltaTime){
        // generation
        if(powerupRandomManager.randomBoolean(deltaTime)){
            int x = (int)powerupRandomManager.randomX();
            int y = (int)powerupRandomManager.randomY();
            int powerupId = powerupRandomManager.randomPowerup();
            int objectId = ++nextId;
            if(Settings.soundEnabled)
                Assets.newPowerup.play(Settings.volume);
            status.setPowerup(createPowerup(x, y, powerupId, objectId));

            GameMessage message = GameMessage.createInstance();
            interpreter.makePowerupMessage(message, objectId, (int)Converter.physicsToFrame(x),
                    (int)Converter.physicsToFrame(y), powerupId);
            messagesInBuffer++;
            networkMessageHandler.putInBuffer(message);
            GameMessage.deleteInstance(message);
        }

        // working
        Iterator<Powerup> powerupIterator = status.actives.iterator();
        while(powerupIterator.hasNext()) {
            Powerup powerup = powerupIterator.next();
            if (!powerup.isEnded()) {
                powerup.work();
            }
            else {
                powerup.stop();
                powerupIterator.remove();
            }
        }
        status.actives.resetIterator();

        // assignment to player
        if(status.powerupsToActivate.size()>0){
            powerupRandomManager.setStartTime(System.currentTimeMillis());
            Iterator<Powerup> toActivateIterator = status.powerupsToActivate.iterator();
            while(toActivateIterator.hasNext()){
                Powerup powerup = toActivateIterator.next();

                GameMessage message = GameMessage.createInstance();
                interpreter.makePowerupAssign(message, powerup.getCharacter().getObjectId(), powerup.getObjectId(), powerup.getCode());
                networkMessageHandler.putInBuffer(message);
                messagesInBuffer++;
                GameMessage.deleteInstance(message);

                powerup.start();
                PhysicsComponent comp = (PhysicsComponent) powerup.getComponent(Component.Type.Physics);
                comp.delete();
                powerup.removeComponent(Component.Type.Physics);
                status.actives.add(powerup);
                toActivateIterator.remove();
            }
            status.powerupsToActivate.resetIterator();
        }
    }

    @Override
    protected void startRound() {
        updateLastTimeReceived();
        if (startAt > System.currentTimeMillis())
            return;
        if(status != null)
            for(GameCharacter gameCharacter: status.living)
                ((LiquidFunPhysicsComponent)gameCharacter.getComponent(Component.Type.Physics)).delete();
        super.startRound();

        contactHandler.init(status);
        powerupRandomManager.setGameStatus(status).setStartTime(startAt);

        status.world = world;

        AttackComponentFactory attackFactory = new AttackComponentFactory();
        for(int i=0 ; i<attacks.length ; i++) {
            Component comp = attackFactory.makeAttackComponent(attacks[i]);
            status.characters[i].addComponent(comp);
            comp.setOwner(status.characters[i]);
        }
    }
}