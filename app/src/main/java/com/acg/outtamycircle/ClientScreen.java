package com.acg.outtamycircle;

import android.util.Log;

import com.acg.outtamycircle.entitycomponent.Component;
import com.acg.outtamycircle.entitycomponent.DrawableComponent;
import com.acg.outtamycircle.entitycomponent.impl.GameCharacter;
import com.acg.outtamycircle.network.GameMessage;
import com.acg.outtamycircle.network.googleimpl.ClientMessageReceiver;
import com.acg.outtamycircle.network.googleimpl.MyGoogleRoom;
import com.badlogic.androidgames.framework.impl.AndroidGame;

public class ClientScreen extends ClientServerScreen {
    private boolean isAlive = true;
    public ClientScreen(AndroidGame game, MyGoogleRoom myGoogleRoom, String[] players, byte[] skins, int[][] spawnPositions, int playerOffset) {
        super(game, myGoogleRoom, players, skins, spawnPositions, playerOffset);

        int radiusCharacter = 40;

        initCharacterSettings(radiusCharacter);

        GameCharacter[] characters = new GameCharacter[players.length];
        for (int i = 0; i < characters.length; i++)
            characters[i] = createCharacter(spawnPositions[i][0], spawnPositions[i][1], Assets.skins[skins[i]], (short)i);

        status.setCharacters(characters);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if(isAlive) {
            GameMessage message = GameMessage.createInstance();
            interpreter.makeMoveClientMessage(message, (short)playerOffset, androidJoystick.getNormX(), androidJoystick.getNormY());
            myGoogleRoom.getNetworkMessageHandlerImpl().putInBuffer(message);
            if(shouldAttack) {
                shouldAttack = false;
                interpreter.makeAttackMessage(message, (short)playerOffset);
                myGoogleRoom.getNetworkMessageHandlerImpl().putInBuffer(message);
            }
            myGoogleRoom.getNetworkMessageHandlerImpl().sendUnreliable(myGoogleRoom.getServerId());
            GameMessage.deleteInstance(message);
        }

        for (GameMessage message : myGoogleRoom.getNetworkMessageHandlerImpl().getMessages()) {
            Log.d("JUANNINO", "ho ricevuot qualcosa");
            switch (interpreter.getType(message)){
                case MOVE_SERVER: {
                    int objectId = interpreter.getObjectId(message);
                    float posX = interpreter.getPosX(message);
                    float posY = interpreter.getPosY(message);
                    float rotation = interpreter.getRotation(message);
                    DrawableComponent comp = (DrawableComponent) status.characters[objectId].getComponent(Component.Type.Drawable);
                    comp.setX((int) posX).setY((int) posY);
                }
                break;
                case ATTACK: {
                    int objectId = interpreter.getObjectId(message);
                    if(Settings.soundEnabled) {
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
                    status.living.remove(status.characters[objectId]);
                    status.dying.add(status.characters[objectId]);
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
                    byte powerUpId = interpreter.getPowerUpId(message);
                    //TODO devo gestirlo? e se sì, devo rimuoverlo anche?!  => altro messaggio
                }
                break;
                case END: {
                    int winnerId = interpreter.getObjectId(message);
                    Log.d("WINNERIS", winnerId + " ha vinto");
                }
                break;
            }
        }
    }

    @Override
    public void present(float deltaTime) {
        super.present(deltaTime);
        if(!isAlive) {
            androidGame.getGraphics().drawPixmap(Assets.sad, 515, 235);
        }
    }

    @Override
    public void setup(){

    }
}
