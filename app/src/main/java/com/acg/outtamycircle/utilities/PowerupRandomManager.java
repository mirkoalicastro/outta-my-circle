package com.acg.outtamycircle.utilities;

import android.util.Log;

import com.acg.outtamycircle.Assets;
import com.acg.outtamycircle.GameStatus;

public class PowerupRandomManager {
    private static int DEGREE_STEPS = 72;
    private static final double DEFAULT_MINIMUM_TIME = 2f;
    private static float[] cosine;
    private static float[] sine;

    private int current;
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

    private GameStatus status;
    private long startTime;

    private final float arenaX, arenaY, arenaRadius;
    private final int powerupsNumber = Assets.powerups.length;
    private final double THRESHOLD = 0.80;

    private double minimumTime = DEFAULT_MINIMUM_TIME;

    private static final double WEIGHT_RANDOM = 0.45, WEIGHT_ELAPSED_TIME = 0.4, WEIGHT_ACTIVE_POWERUPS = 0.15;

    public PowerupRandomManager(float arenaX, float arenaY, float arenaRadius){
        this.arenaRadius = arenaRadius;
        this.arenaX = arenaX;
        this.arenaY = arenaY;
    }

    public PowerupRandomManager setGameStatus(GameStatus status) {
        this.status = status;
        return this;
    }

    public PowerupRandomManager setStartTime(long startTime) {
        this.startTime = startTime;
        return this;
    }

    public boolean randomBoolean(float deltaTime){
        minimumTime -= deltaTime;

        if(minimumTime > 0 || status.getPowerup() != null)
            return false;

        minimumTime = DEFAULT_MINIMUM_TIME;

        long ora = System.currentTimeMillis();
        long diff = ora - startTime;
        diff /= 5000;

        double p1 = Math.random() * WEIGHT_RANDOM;
        double p2 = (1./(3*status.getActivePowerups().size()+1)) * WEIGHT_ACTIVE_POWERUPS;
        double p3 = (1 - 1./(diff+1)) * WEIGHT_ELAPSED_TIME;

        Log.d("POWERUP", String.format("P1:%f | P2:%f | P3:%f", p1, p2, p3));

        boolean result = (p1 + p2 + p3 > THRESHOLD);

        if(result) {
            current = (int) (Math.random() * DEGREE_STEPS);
            distance = (float) (Math.random() * arenaRadius);
            startTime = System.currentTimeMillis();
        }

        return result;
    }

    public float randomX(){
        return arenaX+(cosine[current]*distance);
    }

    public float randomY(){
        return  arenaY+(sine[current]*distance);
    }

    public int randomPowerup(){ return (int)(Math.random() * powerupsNumber); }
}
