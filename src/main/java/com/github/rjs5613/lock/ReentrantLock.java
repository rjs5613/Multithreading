package com.github.rjs5613.lock;

import java.util.Deque;

public class ReentrantLock implements Lock {

    private Thread owner;

    private boolean isLocked;

    private int lockCount;

    @Override
    public void lock() throws InterruptedException {
        if (isLocked) {
            if (owner != Thread.currentThread()) {
                while (isLocked) {
                    wait();
                }
            }
        }
        isLocked = true;
        lockCount++;
        owner = Thread.currentThread();
    }

    @Override
    public void unlock() throws InterruptedException {
        if (!isLocked) {
            throw new IllegalMonitorStateException("");
        }
        if (owner != Thread.currentThread()) {
            throw new IllegalMonitorStateException("");
        }
        owner = null;
        isLocked = false;
        lockCount--;
        if (lockCount == 0) {
            notifyAll();
        }
    }
}
