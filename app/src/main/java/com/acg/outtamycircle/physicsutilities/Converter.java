package com.acg.outtamycircle.physicsutilities;

import android.util.Log;

/**
 * Convertitore di cordinate da mondo fisico a mondo grafico.
 */
public class Converter {
    private static final float PHYSIC_WIDTH = 32, PHYSIC_HEIGHT = 15;
    private static float SCALE_WIDTH = 40, SCALE_HEIGHT = 48;

    public static void setScale(float FRAME_WIDTH, float FRAME_HEIGHT){
        SCALE_WIDTH = FRAME_WIDTH/PHYSIC_WIDTH;
        SCALE_HEIGHT = FRAME_HEIGHT/PHYSIC_HEIGHT;
    }

    public static float physicsToFrameX(float x){
        return x*SCALE_WIDTH;
    }

    public static float physicsToFrameY(float y){
        return y*SCALE_HEIGHT;
    }

    public static float frameToPhysicsX(float x){
        return x/SCALE_WIDTH;
    }

    public static float frameToPhysicsY(float y){
        return y/SCALE_HEIGHT;
    }

    public static float frameToPhysicsRadius(float r){ return r/SCALE_WIDTH; }
}
