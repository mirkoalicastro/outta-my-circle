package com.acg.outtamycircle;

import android.util.Log;

import com.acg.outtamycircle.entitycomponent.Component;
import com.acg.outtamycircle.entitycomponent.DrawableComponent;
import com.acg.outtamycircle.entitycomponent.impl.gameobjects.GameCharacter;
import com.acg.outtamycircle.network.GameMessage;
import com.acg.outtamycircle.network.googleimpl.MyGoogleRoom;
import com.badlogic.androidgames.framework.impl.AndroidGame;

import java.util.Iterator;

public class ClientScreen extends ClientServerScreen {
    private boolean playCollision;

    public ClientScreen(AndroidGame game, MyGoogleRoom myGoogleRoom, String[] players, int[] skins, int[][] spawnPositions, int playerOffset) {
        super(game, myGoogleRoom, players, skins, spawnPositions, playerOffset);

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

        if(isAlive)
            send();

        receive();

        if(playCollision) {
            Log.d("COLLISIONE", "si");
            playCollision = false;
            if(Settings.soundEnabled)
                Assets.gameCharacterCollision.play(Settings.volume);
        }
    }

    private void updatePlayCollision(int objectId, int posX, int posY) {
        for(GameCharacter gameCharacter: status.living) {
            if (gameCharacter.getObjectId() != objectId) {
                DrawableComponent tmp = (DrawableComponent) gameCharacter.getComponent(Component.Type.Drawable);
                int deltaX = (posX - tmp.getX()) * (posX - tmp.getX());
                int deltaY = (posY - tmp.getY()) * (posY - tmp.getY());
                if(Math.sqrt(deltaX + deltaY) <= RADIUS_CHARACTER*2) {
                    playCollision = true;
                    break;
                }
            }
        }
        status.living.resetIterator();
    }

    private void send() {
        GameMessage message = GameMessage.createInstance();
        interpreter.makeMoveClientMessage(message, (short)playerOffset, (short) androidJoystick.getNormX(), (short) androidJoystick.getNormY());
        networkMessageHandler.putInBuffer(message);
        if(shouldAttack) {
            if(Settings.soundEnabled)
                Assets.attackEnabled.play(Settings.volume);
            shouldAttack = false;
            interpreter.makeAttackMessage(message, (short)playerOffset, (int)androidJoystick.getNormX(),(int) androidJoystick.getNormY());
            networkMessageHandler.putInBuffer(message);
        }
        networkMessageHandler.sendUnreliable(myGoogleRoom.getServerId());
        GameMessage.deleteInstance(message);
    }

    private void receive() {
        playCollision = false;
        for (GameMessage message : networkMessageHandler.getMessages()) {
            switch (interpreter.getType(message)){
                case MOVE_SERVER: {
                    int objectId = interpreter.getObjectId(message);
                    int posX = interpreter.getPosX(message);
                    int posY = interpreter.getPosY(message);
                    float rotation = interpreter.getRotation(message);
                    DrawableComponent comp = (DrawableComponent) status.characters[objectId].getComponent(Component.Type.Drawable);
                    if(comp.getX() != posX || comp.getY() != posY) {
                        comp.setX(posX).setY(posY);
                        if(!playCollision)
                            updatePlayCollision(objectId, posX, posY);
                    }
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
                    short objectId = (short)interpreter.getObjectId(message);
                    int x = interpreter.getPosX(message);
                    int y = interpreter.getPosY(message);
                    short powerupId = (short)interpreter.getPowerUpId(message);
                    if(Settings.soundEnabled)
                        Assets.newPowerup.play(Settings.volume);
                    //TODO piazza powerup da qualche parte
                    status.setPowerup(createPowerup(x, y, powerupId, objectId));
                }
                break;
                case POWERUP_ASSIGN: {
                    int objectId = interpreter.getObjectId(message);
                    int powerupId = interpreter.getPowerUpId(message);
                    if(Settings.soundEnabled && objectId == playerOffset)
                        Assets.powerupCollision.play(Settings.volume);
                    //TODO devo gestirlo? e se sì, devo rimuoverlo anche?!  => altro messaggio
                    status.setPowerup(null);
                }
                break;
                //TODO messaggio collisione per audio
                case END: {
                    startAt = System.currentTimeMillis()+5000;
                    winnerId[roundNum-1] = interpreter.getObjectId(message);
                    endRound = true;
                }
                break;
            }
        }
    }

    @Override
    protected void startRound(){
        if (startAt > System.currentTimeMillis())
            return;
        super.startRound();
    }

}
