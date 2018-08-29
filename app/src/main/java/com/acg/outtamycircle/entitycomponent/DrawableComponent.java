package com.acg.outtamycircle.entitycomponent;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Pixmap;

public abstract class DrawableComponent extends Component {
    protected final Graphics graphics;
    protected int x, y;

    public DrawableComponent(Graphics graphics){
        this.graphics = graphics;
    }

    @Override
    public Component.Type type() { return Component.Type.Drawable; }

    public abstract void draw();

    public void setPosition(int x, int y){
        this.x = x;
        this.y = y;
    }

}