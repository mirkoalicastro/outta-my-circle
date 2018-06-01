package com.acg.outtamycircle;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.acg.outtamycircle.entitycomponent.impl.GameObject;
import com.google.fpl.liquidfun.BodyDef;
import com.google.fpl.liquidfun.BodyType;
import com.google.fpl.liquidfun.FixtureDef;
import com.google.fpl.liquidfun.PolygonShape;

/**
 * A moving box.
 *
 * Created by mfaella on 27/02/16.
 */
public class DynamicBoxGO extends GameObject
{
    private static final float width = 2.5f, height = 2.5f, density = 0.5f;
    private static float screen_semi_width, screen_semi_height;
    private static int instances = 0;

    private final Canvas canvas;
    private final Paint paint = new Paint();

    public DynamicBoxGO(GameWorld gw, float x, float y)
    {
       // super(gw);

        instances++;

        this.canvas = new Canvas(gw.buffer);
        this.screen_semi_width = gw.toPixelsXLength(width)/2;
        this.screen_semi_height = gw.toPixelsYLength(height)/2;

        // a body definition: position and type
        BodyDef bdef = new BodyDef();
        bdef.setPosition(x, y);
        bdef.setType(BodyType.dynamicBody);
        // a body
   //     this.body = gw.world.createBody(bdef);
     //   body.setSleepingAllowed(false);
       // this.name = "Box" + instances;
        //body.setUserData(this);

        PolygonShape box = new PolygonShape();
        box.setAsBox(width / 2, height / 2);
        FixtureDef fixturedef = new FixtureDef();
        fixturedef.setShape(box);
        fixturedef.setFriction(0.1f);       // default 0.2
        fixturedef.setRestitution(0.4f);    // default 0
        fixturedef.setDensity(density);     // default 0
//        body.createFixture(fixturedef);

        int green = (int)(255*Math.random());
        int color = Color.argb(200, 255, green, 0);
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);

        // clean up native objects
        fixturedef.delete();
        bdef.delete();
        box.delete();
    }

//    @Override
    public void draw(Bitmap buffer, float x, float y, float angle) {
        canvas.save();
        canvas.rotate((float) Math.toDegrees(angle), x, y);
        canvas.drawRect(x- screen_semi_width, y- screen_semi_height, x + screen_semi_width, y + screen_semi_height, paint);
        canvas.restore();
    }
}
