package com.badlogic.androidgames.framework;

import java.util.LinkedList;
import java.util.Queue;

public abstract class LoadingScreen extends Screen {
    private volatile int progressValue;
    private final Queue<Integer> animations;
    private final int delay;
    private final int duration;
    private final GradualProgress thread;
    public LoadingScreen(Game game) {
        this(game, 0, 100);
    }
    public LoadingScreen(Game game, int delay, int duration) {
        //TODO raise exception
        super(game);
        this.progressValue = 0;
        this.delay = Math.max(0, delay);
        this.duration = Math.max(0, duration);
        animations = new LinkedList<>();
        thread = new GradualProgress();
        thread.start();
    }

    public void setProgress(final int progress) {
        synchronized (animations) {
            animations.add(progress);
            animations.notifyAll();
        }
    }

    public int getProgress() {
        return progressValue;
    }

    /**
     * If a class wants to override dispose method, it must first call 'super.dispose()'
     */
    public void dispose() {
        synchronized (animations) {
            while(!animations.isEmpty()) {
                try {
                    animations.wait();
                } catch (InterruptedException e) {

                }
            }
        }
        thread.interrupt();
    }

    public abstract void onProgress(int progress);

    private class GradualProgress extends Thread {
        @Override
        public void run() {
            while(!Thread.currentThread().isInterrupted()) {
                synchronized (animations) {
                    while(animations.isEmpty()) {
                        try {
                            animations.wait();
                        } catch (InterruptedException e) {

                        }
                    }
                    int from = progressValue;
                    int to = animations.peek();
                    if (from == to) {
                        animations.remove();
                        continue;
                    }
                    //TODO: sleep o attesa attiva?
                    try {
                        Thread.sleep(delay);
                    } catch(InterruptedException ex) {

                    }
                    //TODO fix calculation
                    int sleepingTime = duration / Math.abs(to - from);
                    // Riduco lo sleepingTime per via euristica
                    sleepingTime *= 0.5;
                    int sign = from < to ? 1 : -1;
                    int delta = 1 * sign;
                    for (int i = from+delta; sign < 0 ? i >= to : i <= to; i += delta) {
                        progressValue = i;
                        onProgress(i);
                        //TODO: sleep o attesa attiva?
                        try {
                            Thread.sleep(sleepingTime);
                        } catch(InterruptedException ex) {

                        }
                    }
                    animations.remove();
                    animations.notifyAll();
                }
            }
        }
    }

}
