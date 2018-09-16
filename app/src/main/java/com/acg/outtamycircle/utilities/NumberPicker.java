package com.acg.outtamycircle.utilities;

import com.badlogic.androidgames.framework.Graphics;

public class NumberPicker {
    private static final int DEFAULT_STEPS = 1;
    private int min, max, steps;
    public NumberPicker setMin(int min) {
        this.min = min;
        return this;
    }
    public NumberPicker setMax(int max) {
        this.max = max;
        return this;
    }
    public NumberPicker setSteps(int steps) {
        this.steps = steps;
        return this;
    }
    public NumberPicker(Graphics graphics, int min, int max) {
        this(graphics, min, max, DEFAULT_STEPS);
    }
    public NumberPicker(Graphics graphics, int min, int max, int steps) {
        this.min = min;
        this.max = max;
        this.steps = steps;
    }

}
