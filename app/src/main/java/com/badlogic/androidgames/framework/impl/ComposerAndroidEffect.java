package com.badlogic.androidgames.framework.impl;

import android.graphics.Canvas;
import android.graphics.Paint;


public class ComposerAndroidEffect extends AndroidEffect {
    protected Iterable<AndroidEffect> iterable;
    public ComposerAndroidEffect(Iterable<AndroidEffect> iterable) {
        //TODO eccezione
        this.iterable = iterable;
    }

    @Override
    void apply(Canvas canvas, Paint paint, int x, int y, int width, int height) {
        for(AndroidEffect androidEffect: iterable)
            androidEffect.apply(canvas, paint, x, y, width, height);
    }
}
