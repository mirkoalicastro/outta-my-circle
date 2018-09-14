package com.acg.outtamycircle;

import com.acg.outtamycircle.entitycomponent.Component;
import com.acg.outtamycircle.entitycomponent.DrawableComponent;
import com.acg.outtamycircle.entitycomponent.impl.gameobjects.GameCharacter;
import com.acg.outtamycircle.network.GameMessage;
import com.acg.outtamycircle.network.googleimpl.MyGoogleRoom;
import com.badlogic.androidgames.framework.impl.AndroidGame;

import java.util.Iterator;

public class ClientScreen extends ClientServerScreen {
    public ClientScreen(AndroidGame game, MyGoogleRoom myGoogleRoom, String[] players, int[] skins, int[][] spawnPositions, int playerOffset) {
        super(game, myGoogleRoom, players, skins, spawnPositions, playerOffset);

        startRound();
        roundNum--;
    }

    private void startRound() { //TODO porta sopra
        if (startAt > System.currentTimeMillis())
            return;
        roundNum++;
        if (roundNum > ROUNDS) {
            endGame = true;
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

        if(isAlive)
            send();

        receive();
    }

    @Override
    public void setup(){

    }

    private void send() {
        GameMessage message = GameMessage.createInstance();
        interpreter.makeMoveClientMessage(message, (short)playerOffset, (short) androidJoystick.getNormX(), (short) androidJoystick.getNormY());
        networkMessageHandler.putInBuffer(message);
        if(shouldAttack) {
            if(Settings.soundEnabled)
                Assets.attackEnabled.play(Settings.volume);
            shouldAttack = false;
            interpreter.makeAttackMessage(message, (short)playerOffset);
            networkMessageHandler.putInBuffer(message);
        }
        networkMessageHandler.sendUnreliable(myGoogleRoom.getServerId());
        GameMessage.deleteInstance(message);
    }

    private void receive() {
        for (GameMessage message : networkMessageHandler.getMessages()) {
            switch (interpreter.getType(message)){
                case MOVE_SERVER: {
                    int objectId = interpreter.getObjectId(message);
                    float posX = interpreter.getPosX(message);
                    float posY = interpreter.getPosY(message);
                    float rotation = interpreter.getRotation(message);
                    DrawableComponent comp = (DrawableComponent) status.characters[objectId].getComponent(Component.Type.Drawable);
                    comp.setX((int)posX).setY((int)posY);
                }
                break;
                case ATTACK: {
                    int objectId = interpreter.getObjectId(message);
                    if(objectId != playerOffset && Settings.soundEnabled) {
                        Assets.attackEnabled.play(Settings.volume);
                        //TODO devo far succedere altro?
                    }
                }
                break;
                case DESTROY: {
                    //TODO controlla che funziona
                    int objectId = interpreter.getObjectId(message);
                    if(objectId == playerOffset)
                        isAlive = false;
                    Iterator<GameCharacter> iterator = status.living.iterator();
                    while(iterator.hasNext()) {
                        GameCharacter curr = iterator.next();
                        if(curr.getObjectId() == objectId) {
                            iterator.remove();
                            status.dying.add(curr);
                        }
                    }
                }
                break;
                case POWERUP: {
                    int objectId = interpreter.getObjectId(message);
                    float posX = interpreter.getPosX(message);
                    float posY = interpreter.getPosY(message);
                    int powerUpId = interpreter.getPowerUpId(message);
                    //TODO piazza powerup da qualche parte
                }
                break;
                case POWERUP_ASSIGN: {
                    int objectId = interpreter.getObjectId(message);
                    int powerUpId = interpreter.getPowerUpId(message);
                    //TODO devo gestirlo? e se sì, devo rimuoverlo anche?!  => altro messaggio
                }
                break;
                case END: {
                    startAt = System.currentTimeMillis()+5000;
                    winnerId[roundNum-1] = interpreter.getObjectId(message);
                    endRound = true;
                }
                break;
            }
        }
    }

}
