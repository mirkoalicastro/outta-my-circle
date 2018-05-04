package com.example.mfaella.physicsapp;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

/**
 * Created by mfaella on 28/02/16.
 */
public class AccelerometerListener implements SensorEventListener {

    private final GameWorld gw;

    public AccelerometerListener(GameWorld gw)
    {
        this.gw = gw;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0], y = event.values[1], z = event.values[2];
        gw.setGravity(-x, y);
        // Log.i("AccelerometerListener", "new gravity x= " + -x + "\t y= " + y);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // NOP
    }
}
