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
            switch (interpreter.getType(message)){
                case MOVE_SERVER:
                    int objectId = interpreter.getObjectId(message);
                    float posX = interpreter.getPosX(message);
                    float posY = interpreter.getPosY(message);
                    float rotation = interpreter.getRotation(message);
                    DrawableComponent comp = (DrawableComponent) status.characters[objectId].getComponent(Component.Type.Drawable);
                    comp.setX((int)posX).setY((int)posY);
                    break;
                case ATTACK:
                    //TODO
                    break;
                case DESTROY:
                    //TODO se muoio setta vivo a false
                    //TODO
                    break;
                case POWERUP:
                    //TODO piazza powerup da qualche parte
                    break;
                case POWERUP_ASSIGN:
                    //TODO devo gestirlo?
                    break;
                case END:
                    int winnerId = interpreter.getObjectId(message);
                    Log.d("WINNERIS", winnerId + " ha vinto");
                    break;
            }
        }
    }

    @Override
    public void setup(){

    }
}
