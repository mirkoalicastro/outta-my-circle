package com.acg.outtamycircle.utilities;

import com.acg.outtamycircle.GameStatus;
import com.acg.outtamycircle.entitycomponent.impl.gameobjects.Powerup;

public class PowerupRandomManager {
    private final GameStatus status;

    private final float minX, maxX, minY, maxY;
    private final short powerupsNumber;

    private final MyList<Powerup> actives;
    private final double THRESHOLD = 0.3;

    private double idleTime = 1.; //1 sec.
    private final double startTime;

    private static final double WEIGHT_RANDOM = 0.7, WEIGHT_ELAPSED_TIME = 0.15, WEIGHT_ACTIVE_POWERUPS = 0.15;

    public PowerupRandomManager(float arenaX, float arenaY, float arenaRadius, GameStatus status, long startTime){
        minX = arenaX - arenaRadius;
        maxX = arenaX + arenaRadius - minX;

        minY = arenaY - arenaRadius;
        maxY = arenaY + arenaRadius - minY;

        powerupsNumber = 1; //TODO (short)Assets.powerup.lenght;

        this.actives = status.getActivePowerups();

        this.startTime = startTime/1000; //millis.

        this.status = status;
    }

    public boolean randomBoolean(float deltaTime){
        idleTime -= deltaTime;
        if(idleTime > 0 || status.getPowerup() != null)
            return false;
        idleTime = 1.;

        double p1 = Math.random() * WEIGHT_RANDOM;
        double p2 = (1 - 1/(actives.size()+1)) * WEIGHT_ACTIVE_POWERUPS;
        double p3 = (1 - 1/(System.currentTimeMillis() - startTime)) * WEIGHT_ELAPSED_TIME;

        return p1 + p2 + p3 > THRESHOLD;
    }

    public float randomX(){
        return minX + (float)Math.random() * maxX;
    }

    public float randomY(){
        return  minY + (float)Math.random() * maxY;
    }

    public short randomPowerup(){ return (short)(Math.random() * powerupsNumber); }
}
