package com.github.rjs5613.lock;

public class NonReentrantLock implements Lock {

    private volatile boolean isLocked;

    private Thread owner;


    @Override
    public void lock() throws InterruptedException {
        synchronized (this) {
            while (isLocked) {
                System.out.println("Waiting Thread: " + Thread.currentThread());
                wait();
            }
            isLocked = true;
            owner = Thread.currentThread();
            System.out.println("Giving Lock to" + Thread.currentThread());
        }
    }

    @Override
    public void unlock() throws InterruptedException {
        synchronized (this) {
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

    public static void main(String[] args) throws InterruptedException {
        Lock lock = new NonReentrantLock();
        acquireLock(1, lock);
    }

    private static void acquireLock(int count, Lock lock) throws InterruptedException {
        if (count == 5) {
            return;
        }
        lock.lock();
        acquireLock(count + 1, lock);
    }
}
