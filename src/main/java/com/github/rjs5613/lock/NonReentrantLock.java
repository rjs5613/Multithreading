package com.github.rjs5613.lock;

import sun.misc.Unsafe;

import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.locks.LockSupport;

public class NonReentrantLock implements Lock {

    private final boolean isFair;

    private boolean isLocked;

    private Thread owner;

    private final LinkedList<Thread> waitingThreads = new LinkedList<>();

    public NonReentrantLock(boolean isFair) {
        this.isFair = isFair;
    }

    @Override
    public void lock() throws InterruptedException {
        synchronized (this) {
            Thread currentThread = Thread.currentThread();
            waitingThreads.add(currentThread);
            while (true) {
                if (isFair) {
                    if (isLocked || waitingThreads.peek() != currentThread) {
                        break;
                    }
                } else if (!isLocked) {
                    break;
                }
                System.out.println("Waiting Thread: " + currentThread);
                wait();
            }
            System.out.println("Locking Thread: " + currentThread);
            isLocked = true;
            owner = currentThread;
            waitingThreads.poll();
        }
    }

    @Override
    public void unlock() throws InterruptedException {
        synchronized (this) {
            if (isLocked && owner == Thread.currentThread()) {
                isLocked = false;
                owner = null;
                System.out.println("Unlocking Thread: " + Thread.currentThread());
                notifyAll();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Lock lock = new NonReentrantLock(false);
        Thread t1 = getThread(lock);
        Thread t2 = getThread(lock);
        Thread t3 = getThread(lock);
        Thread t4 = getThread(lock);
        Thread t5 = getThread(lock);
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
        t1.join();
        t2.join();
        t3.join();
        t4.join();
        t5.join();


    }

    private static Thread getThread(Lock lock) throws InterruptedException {
        return new Thread(() -> {
            try {
                lock.lock();
                Thread.sleep(200);
                lock.unlock();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, "Thread-" + new Random().nextInt());
    }
}
