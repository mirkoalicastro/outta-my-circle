package com.acg.outtamycircle.physicsutilities;

//TODO valutare posizione nei package

/**
 * Convertitore di cordinate da mondo fisico a mondo grafico.
 */
public class Converter {
    private static final float PHYSIC_WIDTH = 32, PHYSIC_HEIGHT = 15;

    /**
     * Fattore di bilanciamento:
     * https://forum.unity.com/threads/canvas-scaler-scale-with-screen-size-same-as-setting-ui-objects-to-stretch.512005/
     */
    private static float BALANCE = 1f;
    private static float SCALE;

    //TODO KSF
    public static void setScale(int frameWidth, int frameHeight){
        SCALE = (float)(Math.pow(frameWidth/PHYSIC_WIDTH, 1f - BALANCE)*
                Math.pow(frameHeight/PHYSIC_HEIGHT, BALANCE));
    }

    public static float physicsToFrame(float val){ return val*SCALE; }

    public static float frameToPhysics(float val){ return val/SCALE; }

}
