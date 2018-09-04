package com.badlogic.androidgames.framework.impl;

import com.badlogic.androidgames.framework.OneJobScreen;

import java.util.LinkedList;
import java.util.Queue;

public abstract class AndroidLoadingScreen extends OneJobScreen {
    protected final AndroidGame androidGame;

    private volatile int progressValue;
    private final Queue<Integer> animations;
    private final GradualProgress thread;

    public abstract void doJob();

    public AndroidLoadingScreen(AndroidGame androidGame) {
        super(androidGame);
        this.androidGame = androidGame;
        progressValue = 0;
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
                        } catch (InterruptedException e) { }
                    }
                    int from = progressValue;
                    int to = animations.peek();
                    if (from == to) {
                        animations.remove();
                        continue;
                    }
                    int delta = from < to ? 1 : -1;
                    for (int i = from+delta; delta < 0 ? i >= to : i <= to; i += delta) {
                        progressValue = i;
                        onProgress(i);
                    }
                    animations.remove();
                    animations.notifyAll();
                }
            }
        }
    }
}
