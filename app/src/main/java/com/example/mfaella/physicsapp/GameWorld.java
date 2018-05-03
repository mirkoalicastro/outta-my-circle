package com.example.mfaella.physicsapp;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.Log;

import com.badlogic.androidgames.framework.Input;
import com.badlogic.androidgames.framework.impl.TouchHandler;
import com.google.fpl.liquidfun.ContactListener;
import com.google.fpl.liquidfun.ParticleSystem;
import com.google.fpl.liquidfun.ParticleSystemDef;
import com.google.fpl.liquidfun.World;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

/**
 * The game objects and the viewport.
 *
 * Created by mfaella on 27/02/16.
 */
public class GameWorld {
    // Rendering
    final static int bufferWidth = 400, bufferHeight = 600;    // actual pixels
    Bitmap buffer;
    private Canvas canvas;
    private Paint particlePaint;
    private final boolean isLittleEndian;

    // Simulation
    List<GameObject> objects;
    World world;
    final Box physicalSize, screenSize;
    private ContactListener contactListener; // kept to prevent GC
    private TouchConsumer touchConsumer;
    private TouchHandler touchHandler;
    private final int bufferOffset; // an architecture-dependent parameter

    // Particles
    ParticleSystem particleSystem;
    private byte[] particlePositions;
    private ByteBuffer particlePositionsBuffer;
    private static final int BYTESPERPARTICLE = 8;
    private static final int MAXPARTICLECOUNT = 1000;
    private static final float PARTICLE_RADIUS = 0.3f;

    // Parameters for world simulation
    private static final float TIME_STEP = 1 / 50f; // 60 fps
    private static final int VELOCITY_ITERATIONS = 8;
    private static final int POSITION_ITERATIONS = 3;
    private static final int PARTICLE_ITERATIONS = 3;

    // Arguments are in physical simulation units.
    public GameWorld(Box physicalSize, Box screenSize) {
        this.physicalSize = physicalSize;
        this.screenSize = screenSize;

        this.buffer = Bitmap.createBitmap(bufferWidth, bufferHeight, Bitmap.Config.ARGB_8888);
        this.world = new World(0, 0);  // gravity vector

        // The particle system
        ParticleSystemDef psysdef = new ParticleSystemDef();
        this.particleSystem = world.createParticleSystem(psysdef);
        particleSystem.setRadius(PARTICLE_RADIUS);
        particleSystem.setMaxParticleCount(MAXPARTICLECOUNT);
        psysdef.delete();
        particlePositionsBuffer = ByteBuffer.allocateDirect(MAXPARTICLECOUNT * BYTESPERPARTICLE);
        particlePositions = particlePositionsBuffer.array();

        particlePaint = new Paint();
        particlePaint.setARGB(255, 0, 255, 0);
        particlePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        // stored to prevent GC
        contactListener = new MyContactListener();
        world.setContactListener(contactListener);

        touchConsumer = new TouchConsumer(this, physicalSize.width/screenSize.width, physicalSize.height/screenSize.height,
                physicalSize.xmin, physicalSize.ymin);

        this.objects = new ArrayList<>();
        this.canvas = new Canvas(buffer);

        isLittleEndian = (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN);
        // An ugly trick, can we do better?
        Log.d("DEBUG", "Build.FINGERPRINT=" + Build.FINGERPRINT);
        Log.d("DEBUG", "Build.PRODUCT=" + Build.PRODUCT);
        if (Build.FINGERPRINT.contains("generic") ||
            Build.FINGERPRINT.contains("unknown") ||
            Build.PRODUCT.contains("sdk"))
            bufferOffset = 0; // emulator
        else
            bufferOffset = 4; // real device
    }

    public synchronized GameObject addGameObject(GameObject obj)
    {
        objects.add(obj); return obj;
    }

    public synchronized void addParticleGroup(GameObject obj)
    {
        objects.add(obj);
    }

    private void drawParticles()
    {
        final int particleCount = particleSystem.getParticleCount();

        // Log.d("GameWorld", "about to draw " + particleCount + " particles");

        particleSystem.copyPositionBuffer(0, particleCount, particlePositionsBuffer);

        for (int i = 0; i < particleCount; i++) {
            int xint, yint;
            if (isLittleEndian) {
                xint = (particlePositions[i * 8 + bufferOffset] & 0xFF) | (particlePositions[i * 8 + bufferOffset + 1] & 0xFF) << 8 |
                       (particlePositions[i * 8 + bufferOffset + 2] & 0xFF) << 16 | (particlePositions[i * 8 + bufferOffset + 3] & 0xFF) << 24;
                yint = (particlePositions[i * 8 + bufferOffset + 4] & 0xFF) | (particlePositions[i * 8 + bufferOffset + 5] & 0xFF) << 8 |
                       (particlePositions[i * 8 + bufferOffset + 6] & 0xFF) << 16 | (particlePositions[i * 8 + bufferOffset + 7] & 0xFF) << 24;
            } else {
                xint = (particlePositions[i * 8] & 0xFF) << 24 | (particlePositions[i * 8 + 1] & 0xFF) << 16 |
                       (particlePositions[i * 8 + 2] & 0xFF) << 8 | (particlePositions[i * 8 + 3] & 0xFF);
                yint = (particlePositions[i * 8 + 4] & 0xFF) << 24 | (particlePositions[i * 8 + 5] & 0xFF) << 16 |
                       (particlePositions[i * 8 + 6] & 0xFF) << 8 | (particlePositions[i * 8 + 7] & 0xFF);
            }

            float x = Float.intBitsToFloat(xint), y = Float.intBitsToFloat(yint);
            canvas.drawCircle(toPixelsX(x), toPixelsY(y), 6, particlePaint);
        }
    }

    public synchronized void update()
    {
        // advance the physics simulation
        world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS, PARTICLE_ITERATIONS);
        // handle touch events
        for (Input.TouchEvent event: touchHandler.getTouchEvents())
            touchConsumer.consumeTouchEvent(event);
    }

    public synchronized void render()
    {
        // clear the screen (with black)
        canvas.drawARGB(255, 0, 0, 0);
        for (GameObject obj: objects)
            obj.draw(buffer);
        drawParticles();
    }


    public float toPixelsX(float x)
    {
        return (x-physicalSize.xmin)/physicalSize.width*bufferWidth;
    }

    public float toPixelsY(float y)
    {
        return (y-physicalSize.ymin)/physicalSize.height*bufferHeight;
    }

    public float toPixelsXLength(float x)
    {
        return x/physicalSize.width*bufferWidth;
    }

    public float toPixelsYLength(float y)
    {
        return y/physicalSize.height*bufferHeight;
    }

    public synchronized void setGravity(float x, float y)
    {
        world.setGravity(x, y);
    }

    @Override
    public void finalize()
    {
        world.delete();
    }

    public void setTouchHandler(TouchHandler touchHandler) {
        this.touchHandler = touchHandler;
    }
}
