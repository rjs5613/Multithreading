package com.github.rjs5613.lock;

import java.util.LinkedList;

public class ReentrantLock implements Lock {

    private Thread owner;

    private final boolean isFair;

    private boolean isLocked;

    private int lockCount;

    private LinkedList<Thread> waitingThreads = new LinkedList<>();

    public ReentrantLock(boolean isFair) {
        this.isFair = isFair;
    }

    @Override
    public void lock() throws InterruptedException {
        synchronized (this) {
            Thread currentThread = Thread.currentThread();
            if (isLocked) {
                if (owner != currentThread) {
                    while (true) {
                        if (isFair) {
                            if (!isLocked && waitingThreads.peek() == currentThread) {
                                break;
                            }
                        } else {
                            if (!isLocked) {
                                break;
                            }
                        }
                        wait();
                    }
                }
            }
            isLocked = true;
            lockCount++;
            owner = currentThread;
            waitingThreads.poll();
            System.out.println("Giving Lock to " + currentThread);
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
        ReentrantLock lock = new ReentrantLock(true);
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
