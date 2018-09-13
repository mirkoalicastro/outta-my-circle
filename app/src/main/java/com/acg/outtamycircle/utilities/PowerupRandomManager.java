package com.acg.outtamycircle.utilities;

import com.acg.outtamycircle.Assets;

import java.util.Random;

public class PowerupRandomManager {
    private final float minX, maxX, minY, maxY;
    private final short powerupsNumber;

    public PowerupRandomManager(float arenaX, float arenaY, float arenaRadius){
        minX = arenaX - arenaRadius;
        maxX = arenaX + arenaRadius - minX;

        minY = arenaY - arenaRadius;
        maxY = arenaY + arenaRadius - minY;

        powerupsNumber = 1; //TODO (short)Assets.powerup.lenght;
    }

    public boolean randomBoolean(){
        return false;
    }

    public float randomX(){
        return minX + (float)Math.random() * maxX;
    }

    public float randomY(){
        return  minY + (float)Math.random() * maxY;
    }

    public short randomPowerup(){ return (short)(Math.random() * powerupsNumber); }
}
