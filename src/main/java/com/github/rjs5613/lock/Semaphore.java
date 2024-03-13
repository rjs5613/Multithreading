package com.github.rjs5613.lock;

import java.util.HashSet;
import java.util.Set;

public class Semaphore {

    // We don't need to define available permit as volatile as this is accessed using reentrant lock only
    private int availablePermit;

    private final Set<Thread> acquiringThreads;

    private final Lock lock = new ReentrantLock();
    private final int maxPermits;

    public Semaphore(int maxPermits) {
        this.maxPermits = maxPermits;
        this.availablePermit = maxPermits;
        this.acquiringThreads = new HashSet<>();
    }


    public void acquire() throws InterruptedException {
        lock.lock();
        try {
            while (availablePermit == 0) {
                lock.wait();
            }
            this.acquiringThreads.add(Thread.currentThread());
            availablePermit--;
        } finally {
            lock.unlock();
        }
    }

    public void release() throws InterruptedException {
        lock.lock();
        try {
            if (acquiringThreads.contains(Thread.currentThread())) {
                if (availablePermit < maxPermits) {
                    availablePermit++;
                    acquiringThreads.remove(Thread.currentThread());
                    lock.notifyAll();
                }
            }
        } finally {
            lock.unlock();
        }
    }

    public int availablePermit() throws InterruptedException {
        lock.lock();
        try {
            return availablePermit;
        } finally {
            lock.unlock();
        }
    }
}
