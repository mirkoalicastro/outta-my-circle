package com.acg.outtamycircle.utilities;

import android.util.Log;

import com.acg.outtamycircle.GameStatus;
import com.acg.outtamycircle.entitycomponent.impl.gameobjects.Powerup;

public class PowerupRandomManager {
    private static int DEGREE_STEPS = 72;
    private static float[] cosine;
    private static float[] sine;

    private int current = 0;
    private float distance;

    static{
        cosine = new float[DEGREE_STEPS];
        sine = new float[DEGREE_STEPS];

        float step = (float) ((2*Math.PI)/DEGREE_STEPS);
        float angle = 0;
        for(int i=0 ; i<DEGREE_STEPS ; i++){
            cosine[i] = (float) Math.cos(angle);
            sine[i] = (float) Math.sin(angle);
            angle += step;
        }
    }
    private final GameStatus status;

    private final float arenaX, arenaY, radius;
    private final short powerupsNumber;

    private final MyList<Powerup> actives;
    private final double THRESHOLD = 0.65;

    private double idleTime = 1f; //1 sec.
    private final double startTime;

    private static final double WEIGHT_RANDOM = 0.4, WEIGHT_ELAPSED_TIME = 0.4, WEIGHT_ACTIVE_POWERUPS = 0.2;

    public PowerupRandomManager(float arenaX, float arenaY, float arenaRadius, GameStatus status, long startTime){
        radius = arenaRadius;
        this.arenaX = arenaX;
        this.arenaY = arenaY;

        powerupsNumber = 1; //TODO (short)Assets.powerup.lenght;

        this.actives = status.getActivePowerups();

        this.startTime = startTime/1000; //millis.

        this.status = status;
    }

    public boolean randomBoolean(float deltaTime){

        Log.d("POWERUP", "delta " + deltaTime + " ; idle " + idleTime);

        idleTime -= deltaTime;

        if(idleTime > 0 || status.getPowerup() != null)
            return false;

        double p1 = Math.random() * WEIGHT_RANDOM;
        double p2 = (1/(actives.size()+1)) * WEIGHT_ACTIVE_POWERUPS;
        double p3 = (1 - 1/(System.currentTimeMillis() - startTime)) * WEIGHT_ELAPSED_TIME;

        Log.d("POWERUP", String.format("P1:%f | P2:%f | P3:%f", p1, p2, p3));

        boolean result = (p1 + p2 + p3 > THRESHOLD);

        if(result) {
            current = (int) (Math.random() * DEGREE_STEPS);
            distance = (float) (Math.random() * radius);
            idleTime = 1f;
        }

        return result;
    }

    public float randomX(){
        return arenaX+(cosine[current]*distance);
    }

    public float randomY(){
        return  arenaY+(sine[current]*distance);
    }

    public short randomPowerup(){ return (short)(Math.random() * powerupsNumber); }
}
