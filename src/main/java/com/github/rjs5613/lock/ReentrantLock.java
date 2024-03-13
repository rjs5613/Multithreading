package com.github.rjs5613.lock;

import java.util.Deque;

public class ReentrantLock implements Lock {

    private Thread owner;

    private boolean isLocked;

    private int lockCount;

    @Override
    public void lock() throws InterruptedException {
        synchronized (this) {
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
            System.out.println("Giving Lock to " + Thread.currentThread());
        }

    }

    @Override
    public void unlock() throws InterruptedException {
        synchronized (this) {
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

    public static void main(String[] args) throws InterruptedException {
        ReentrantLock lock = new ReentrantLock();
        acquireLock(1, lock);
    }

    private static void acquireLock(int count, ReentrantLock lock) throws InterruptedException {
        if (count == 5) {
            return;
        }
        lock.lock();
        acquireLock(count + 1, lock);
    }
}
