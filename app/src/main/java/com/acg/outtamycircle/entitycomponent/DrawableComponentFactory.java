package com.acg.outtamycircle.entitycomponent;

import android.util.Log;

import com.badlogic.androidgames.framework.Effect;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Pixmap;

public class DrawableComponentFactory{
    private Graphics graphics;
    private int x, y;
    private int width, height;
    private Pixmap pixmap;
    private Integer color;
    private Effect effect;
    private int strokeWidth;
    private Integer strokeColor;

    private DrawableShape shape = DrawableShape.RECTANGLE;
    public enum DrawableShape{CIRCLE, RECTANGLE}



    public DrawableComponentFactory setGraphics(Graphics graphics){
        this.graphics = graphics;
        return this;
    }

    public DrawableComponentFactory setStroke(int strokeWidth, Integer strokeColor) {
        this.strokeWidth = strokeWidth;
        this.strokeColor = strokeColor;
        return this;
    }

    public DrawableComponentFactory setColor(Integer color) {
        this.color = color;
        return this;
    }

    public DrawableComponentFactory setEffect(Effect effect) {
        this.effect = effect;
        return this;
    }

    public DrawableComponentFactory setPixmap(Pixmap pixmap) {
        this.pixmap = pixmap;
        return this;
    }

    public DrawableComponentFactory setWidth(int width) {
        this.width = width;
        return this;
    }

    public DrawableComponentFactory setHeight(int height) {
        this.height = height;
        return this;
    }

    public DrawableComponentFactory setX(int x) {
        this.x = x;
        return this;
    }

    public DrawableComponentFactory setY(int y) {
        this.y = y;
        return this;
    }

    public DrawableComponentFactory setShape(DrawableShape shape){
        this.shape = shape;
        return this;
    }


    public DrawableComponent getComponent(){
        DrawableComponent component = null;

        switch(shape){
            case CIRCLE:
                component = new CircleDrawableComponent(graphics);
                break;
        }

        return component.setStroke(strokeWidth, strokeColor).setColor(color).setEffect(effect)
                .setPixmap(pixmap).setWidth(width).setHeight(height).setX(x).setY(y);
    }

    private class CircleDrawableComponent extends DrawableComponent{
        private int radius;

        CircleDrawableComponent(Graphics graphics){ super(graphics); }

        @Override
        public void draw() {
            if(strokeWidth > 0 && strokeColor != null)
                graphics.drawCircleBorder(x,y,radius,strokeWidth,strokeColor);
            if(color != null)
                graphics.drawCircle(x,y,radius,color);
            if(effect != null)
                graphics.drawEffect(effect, x, y, width, height);
            if(pixmap != null)
                graphics.drawPixmap(pixmap, x-radius, y-radius, width,height);
        }

        @Override
        public DrawableComponent setWidth(int width) {
            return setRadius(width/2);
        }

        @Override
        public DrawableComponent setHeight(int height) {
            return setRadius(height/2);
        }

        DrawableComponent setRadius(int radius) {
            this.radius = radius;
            width = radius*2;
            height = radius*2;
            return this;
        }
    }
}
