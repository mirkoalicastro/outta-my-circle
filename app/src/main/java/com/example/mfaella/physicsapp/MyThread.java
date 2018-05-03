package com.example.mfaella.physicsapp;

import android.util.Log;


/**
 * Created by mfaella on 08/02/16.
 */
public class MyThread extends Thread {
    public volatile int counter;
    private GameWorld gw;

    public MyThread(GameWorld gw)
    {
        this.gw = gw;
    }

    @Override
    public void run() {

        while (true) {
            try {
                sleep(3000);
                counter++;
                Log.i("MyThread", "counter: " + counter);
                // inverts gravity
                /* float gravity_x = -4 + 8*(counter%2),
                        gravity_y = 0;
                   gw.setGravity(gravity_x, gravity_y); */
            } catch (InterruptedException e) {
                return;
            }
        }
    }
}
