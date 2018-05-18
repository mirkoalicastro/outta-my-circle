package com.example.mfaella.physicsapp.entitycomponent;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Pixmap;

public abstract class DrawableComponent extends Component {
    protected Graphics graphics;
    protected int color;
    protected Pixmap pixmap = null;

    public DrawableComponent(Graphics graphics){
        this.graphics = graphics;
    }

    @Override
    public Component.Type type() { return Component.Type.Drawable; }

    public abstract void drawColor();

    public abstract void drawPixmap();

    public void setColor(int color){
        this.color = color;
    }

    public void setPixmap(Pixmap pixmap) {
        this.pixmap = pixmap;
    }
}