package com.badlogic.androidgames.framework.impl;

import com.badlogic.androidgames.framework.Animation;
import com.badlogic.androidgames.framework.Graphics;

public class SpinAnimation implements Animation {
    private final Graphics graphics;
    private long lastTime;
    private final long delta;
    private final static long DEFAULT_DELTA = 250;
    public SpinAnimation(Graphics graphics) {
        this(graphics, DEFAULT_DELTA);
    }
    public SpinAnimation(Graphics graphics, long delta) {
        this.graphics = graphics;
        this.delta = delta;
    }
    public void draw() {
    }
}
