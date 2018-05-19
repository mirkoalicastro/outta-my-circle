package com.example.mfaella.physicsapp.entitycomponent;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Pixmap;

public abstract class DrawableComponent extends Component {
    protected final Graphics graphics;

    public DrawableComponent(Graphics graphics){
        this.graphics = graphics;
    }

    @Override
    public Component.Type type() { return Component.Type.Drawable; }

    public abstract void draw();
}