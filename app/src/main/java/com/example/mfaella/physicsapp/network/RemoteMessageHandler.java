package com.example.mfaella.physicsapp.network;

import com.google.android.gms.games.Games;

public class RemoteMessageHandler extends NetworkMessageHandler{
    public RemoteMessageHandler(){};

    @Override
    public void write() {
        //TODO parametri per GPGS
        //Games.getRealTimeMultiplayerClient()
    }

    @Override
    public void broadcast(){

    }

    @Override
    public GameMessage getMessage() {
        return null;
    }

}
