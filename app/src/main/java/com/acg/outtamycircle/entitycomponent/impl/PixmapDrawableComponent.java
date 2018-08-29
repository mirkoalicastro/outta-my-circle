package com.acg.outtamycircle.entitycomponent.impl;

import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Pixmap;
import com.acg.outtamycircle.entitycomponent.DrawableComponent;

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
        graphics.drawPixmap(pixmap, x, y);
    }
}
