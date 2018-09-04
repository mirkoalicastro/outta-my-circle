package com.acg.outtamycircle.entitycomponent.impl;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;

import com.acg.outtamycircle.physicsutilities.Converter;
import com.badlogic.androidgames.framework.Graphics;
import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.BodyDef;
import com.google.fpl.liquidfun.CircleShape;
import com.google.fpl.liquidfun.FixtureDef;
import com.google.fpl.liquidfun.World;

/*Classe da eliminare*/
public class BoundsTest {
    Graphics graphics;
    int x, y, radius;
    Body body;
    private Paint paint = new Paint();

    public BoundsTest(Graphics g, World world, int radius, int x, int y){
        this.x = x;
        this.y = y;
        this.radius = radius;
        graphics = g;

        BodyDef bdef = new BodyDef();
        bdef.setPosition(Converter.frameToPhysics(x), Converter.frameToPhysics(y));
        //default type is staticBody
        body = world.createBody(bdef);

        CircleShape circle = new CircleShape();
        circle.setRadius(radius);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.setShape(circle);
        body.createFixture(fixtureDef);

        // release native objects
        bdef.delete();
        circle.delete();
        fixtureDef.delete();
    }

    /*public void draw(Bitmap buffer, float x, float y, float angle) {
        paint.setARGB(255, 0, 0, 255);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        Canvas canvas = new Canvas(buffer);
        canvas.drawRect(screen_xmin, screen_ymin, screen_xmax, screen_ymax, paint);
    }*/

    public void draw(){
        graphics.drawCircle(x, y, radius, Color.YELLOW);
    }
}
