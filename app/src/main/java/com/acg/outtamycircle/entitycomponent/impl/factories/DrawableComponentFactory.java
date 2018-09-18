package com.acg.outtamycircle.entitycomponent.impl.factories;

import com.acg.outtamycircle.entitycomponent.DrawableComponent;
import com.acg.outtamycircle.entitycomponent.Entity;
import com.acg.outtamycircle.entitycomponent.impl.components.CircleDrawableComponent;
import com.acg.outtamycircle.entitycomponent.impl.components.RectangleDrawableComponent;
import com.badlogic.androidgames.framework.Effect;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Pixmap;

public class DrawableComponentFactory{
    private final Graphics graphics;
    private int x, y;
    private int width, height;
    private Pixmap pixmap;
    private Integer color;
    private Effect effect;
    private int strokeWidth;
    private Integer strokeColor;
    private Entity owner;

    private DrawableShape shape = DrawableShape.CIRCLE;
    public enum DrawableShape{CIRCLE, RECTANGLE}

    public DrawableComponentFactory(Graphics graphics){
        this.graphics = graphics;
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

    public DrawableComponentFactory setOwner(Entity owner) {
        this.owner = owner;
        return this;
    }

    public void resetFactory(){
        x = y = width = height = strokeWidth = -1;
        pixmap = null; color = null; effect = null;
        strokeColor = null; owner = null;
        shape = null;
    }

    public DrawableComponent makeComponent(){
        DrawableComponent component = null;

        switch(shape){
            case CIRCLE:
                component = new CircleDrawableComponent(graphics);
                break;
            case RECTANGLE:
                component = new RectangleDrawableComponent(graphics);
        }

        component.setStroke(strokeWidth, strokeColor).setColor(color)
                .setEffect(effect).setPixmap(pixmap).setWidth(width)
                .setHeight(height).setX(x).setY(y).setOwner(owner);

        return component;
    }
}
