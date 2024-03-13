package com.github.rjs5613.lock;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Semaphore {

    // We don't need to define available permit as volatile as this is accessed using reentrant lock only
    private int availablePermit;

    private final Set<Thread> acquiringThreads;

    private final Lock lock = new ReentrantLock();

    private final Condition condition = lock.newCondition();
    private final int maxPermits;

    public Semaphore(int maxPermits) {
        this.maxPermits = maxPermits;
        this.availablePermit = maxPermits;
        this.acquiringThreads = new HashSet<>();
    }


    public void acquire() throws InterruptedException {
        // We will use reentrant lock here. We can use synchronised here as well, in that case we won't need to use Condition variable we can use Object class's wait and notify.
        lock.lock();
        try {
            while (availablePermit == 0) {
                System.out.println("Waiting Thread: " + Thread.currentThread());
                //We can't use object class's wait method here as this code is not inside synchronised block / method
                //Since we need the thread to wait, we will use condition.await here.
                condition.await();
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
                    //Can't use notifyAll as this code is not synchronised.
                    // So we will use condition.signalAll to achieve the same functionality
                    condition.signalAll();
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

    public static void main(String[] args) throws InterruptedException {
        Semaphore s = new Semaphore(3);
        Thread t1 = getThread(s);
        Thread t2 = getThread(s);
        Thread t3 = getThread(s);
        Thread t4 = getThread(s);

        t1.start();
        t2.start();
        t3.start();
        t4.start();

        t1.join();
        t2.join();
        t3.join();
        t4.join();
    }

    private static Thread getThread(Semaphore s) {
        return new Thread(() -> {
            try {
                s.acquire();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

}
