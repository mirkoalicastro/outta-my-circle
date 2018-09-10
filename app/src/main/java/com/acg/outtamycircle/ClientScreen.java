package com.acg.outtamycircle;

import com.acg.outtamycircle.entitycomponent.Component;
import com.acg.outtamycircle.entitycomponent.impl.GameCharacter;
import com.acg.outtamycircle.network.GameMessage;
import com.acg.outtamycircle.network.googleimpl.MyGoogleRoom;
import com.badlogic.androidgames.framework.impl.AndroidGame;

public class ClientScreen extends ClientServerScreen {
    public ClientScreen(AndroidGame game, MyGoogleRoom myGoogleRoom, String[] players, short[] skins, int[][] spawnPositions) {
        super(game, myGoogleRoom, players, skins, spawnPositions);

        initCharacterSettings(40);

        GameCharacter[] characters = new GameCharacter[players.length];
        for (int i = 0; i < characters.length; i++)
            characters[i] = createCharacter(spawnPositions[i][0], spawnPositions[i][1], Assets.skins[skins[i]], (short)i);

        status.setCharacters(characters);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        GameCharacter ch;

        for (GameMessage message : myGoogleRoom.getServerClientMessageHandler().getMessages()) {
            switch (interpreter.getType(message)){
                case MOVE_CLIENT:
                    break;
            }
        }
    }

    @Override
    public void setup(){

    }
}
