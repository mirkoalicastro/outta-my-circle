package com.acg.outtamycircle;

import com.acg.outtamycircle.network.googleimpl.MyGoogleRoom;
import com.badlogic.androidgames.framework.impl.AndroidGame;

public class ClientScreen extends ClientServerScreen {
    public ClientScreen(AndroidGame game, MyGoogleRoom myGoogleRoom, String[] players, short[] skins, int[][] spawnPositions) {
        super(game, myGoogleRoom, players, skins, spawnPositions);
    }

    @Override
    public void update(float deltaTime) {

    }

    @Override
    public void setup(){

    }
}
