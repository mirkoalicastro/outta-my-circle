package com.example.mfaella.physicsapp.entitycomponent.impl;

import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Pixmap;
import com.example.mfaella.physicsapp.entitycomponent.DrawableComponent;
import com.example.mfaella.physicsapp.entitycomponent.PositionComponent;

public class PixmapDrawableComponent extends DrawableComponent {
    private Pixmap pixmap;


    public PixmapDrawableComponent(Graphics graphics){
        super(graphics);
    }

    public void setPixmap(Pixmap pixmap) {
        this.pixmap = pixmap;
    }

    @Override
    public void draw() {
        PositionComponent pos = (PositionComponent) owner.getComponent(Type.Position);

        graphics.drawPixmap(pixmap, pos.x, pos.y);
    }
}
