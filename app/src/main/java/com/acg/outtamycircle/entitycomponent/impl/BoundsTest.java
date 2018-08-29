package com.acg.outtamycircle.entitycomponent.impl;

import android.graphics.Paint;

public class BoundsTest {
    /*private Paint paint = new Paint();
    private float xmin, xmax, ymin, ymax;
    private float screen_xmin, screen_xmax, screen_ymin, screen_ymax;

    public BoundsTest()
    {
        super(gw);
        this.xmin = xmin; this.xmax = xmax; this.ymin = ymin; this.ymax = ymax;
        this.screen_xmin = gw.toPixelsX(xmin+THICKNESS);
        this.screen_xmax = gw.toPixelsX(xmax-THICKNESS);
        this.screen_ymin = gw.toPixelsY(ymin+THICKNESS);
        this.screen_ymax = gw.toPixelsY(ymax-THICKNESS);

        // a body definition: position and type
        BodyDef bdef = new BodyDef();
        // default position is (0,0) and default type is staticBody
        this.body = gw.world.createBody(bdef);
        this.name = "Enclosure";
        body.setUserData(this);

        PolygonShape box = new PolygonShape();
        // top
        box.setAsBox(xmax-xmin, THICKNESS, xmin+(xmax-xmin)/2, ymin, 0); // last is rotation angle
        body.createFixture(box, 0); // no density needed
        // bottom
        box.setAsBox(xmax-xmin, THICKNESS, xmin+(xmax-xmin)/2, ymax, 0);
        body.createFixture(box, 0);
        // left
        box.setAsBox(THICKNESS, ymax-ymin, xmin, ymin+(ymax-ymin)/2, 0);
        body.createFixture(box, 0);
        // right
        box.setAsBox(THICKNESS, ymax-ymin, xmax, ymin+(ymax - ymin) / 2, 0);
        body.createFixture(box, 0);

        // clean up native objects
        bdef.delete();
        box.delete();
    }

    @Override
    public void draw(Bitmap buffer, float x, float y, float angle) {
        paint.setARGB(255, 0, 0, 255);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        Canvas canvas = new Canvas(buffer);
        canvas.drawRect(screen_xmin, screen_ymin, screen_xmax, screen_ymax, paint);
    }*/
}
