package com.acg.outtamycircle.physicsutilities;

import android.util.Log;

/**
 * Convertitore di cordinate da mondo fisico a mondo grafico.
 */
public class Converter {
    private static final float PHYSIC_WIDTH = 30, PHYSIC_HEIGHT = 16,
            FRAME_WIDTH = 1280, FRAME_HEIGHT = 720;

    public static float physicsToFrameX(float x){
        return x/40;
    }

    public static float physicsToFrameY(float y){
        return y/48;
    }

    public static float frameToPhysicsX(float x){
        return x*40;
    }

    public static float frameToPhysicsY(float y){
        return y*48;
    }

    public static float frameToPhysicsRadius(float r){
        return (r*40);
    }
}
