package com.badlogic.androidgames.framework.impl;

import android.graphics.RadialGradient;
import android.graphics.Shader;

import com.badlogic.androidgames.framework.Effect;

public class RadialGradientEffect extends AndroidEffect {
    public RadialGradientEffect(float centerX, float centerY, float radius, int[] colors, float[] stops, Shader.TileMode tileMode) {
        shader = new RadialGradient(centerX, centerY, radius, colors, stops, tileMode);
    }
}
