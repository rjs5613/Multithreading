package com.github.rjs5613.lock;

public class NonReentrantLock implements Lock {

    private volatile boolean isLocked;

    private Thread owner;

    @Override
    public void lock() throws InterruptedException {
        while (isLocked) {
            wait();
        }
        isLocked = true;
        owner = Thread.currentThread();
    }

    @Override
    public void unlock() throws InterruptedException {
        if (!isLocked) {
            throw new IllegalMonitorStateException("");
        }
        if (owner == Thread.currentThread()) {
            throw new IllegalMonitorStateException("");
        }
        isLocked = false;
        owner = null;
        notifyAll();
    }
}
