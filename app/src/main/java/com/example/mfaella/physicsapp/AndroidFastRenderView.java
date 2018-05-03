package com.example.mfaella.physicsapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class AndroidFastRenderView extends SurfaceView implements Runnable {
    /*
    Changed comment again 
     */
    private Bitmap framebuffer;
    private Thread renderThread = null;
    private SurfaceHolder holder;
    private GameWorld gameworld;
    private volatile boolean running = false;
    
    public AndroidFastRenderView(Context context, GameWorld gw) {
        super(context);
        this.gameworld = gw;
        this.framebuffer = gw.buffer;
        this.holder = getHolder();
    }

    public void resume() { 
        running = true;
        renderThread = new Thread(this);
        renderThread.start();         
    }      
    
    public void run() {
        Rect dstRect = new Rect();
        long startTime = System.nanoTime(), fpsTime = startTime, frameCounter = 0;

        /*** The Game Main Loop ***/
        while (running) {
            if(!holder.getSurface().isValid()) {
                // too soon (busy waiting), this only happens on startup and resume
                continue;
            }

            long currentTime = System.nanoTime();
            // deltaTime is in seconds
            float deltaTime = (currentTime-startTime) / 1000000000f,
                  fpsDeltaTime = (currentTime-fpsTime) / 1000000000f;
            startTime = currentTime;

            gameworld.update();
            gameworld.render();

            Canvas canvas = holder.lockCanvas();

            canvas.getClipBounds(dstRect);
            // scaling to actual screen resolution
            canvas.drawBitmap(framebuffer, null, dstRect, null);

            holder.unlockCanvasAndPost(canvas);

            // measure FPS
            frameCounter++;
            if (fpsDeltaTime > 1) { // every second
                Log.d("FastRenderView", "Current FPS = " + frameCounter);
                frameCounter = 0;
                fpsTime = currentTime;
            }
        }
    }

    public void pause() {                        
        running = false;                        
        while(true) {
            try {
                renderThread.join();
                break;
            } catch (InterruptedException e) {
                // just retry
            }
        }
    }        
}