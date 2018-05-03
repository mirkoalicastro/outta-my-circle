package com.example.mfaella.physicsapp;

/**
 * Created by mfaella on 05/03/16.
 */
public class Box {
    public float xmin, ymin, xmax, ymax, width, height;
    public Box(float xmin, float ymin, float xmax, float ymax)
    {
        this.xmin = xmin;
        this.xmax = xmax;
        this.ymin = ymin;
        this.ymax = ymax;
        this.width = xmax - xmin;
        this.height = ymax - ymin;
    }
}
