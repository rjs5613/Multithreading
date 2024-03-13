package com.github.rjs5613.deadlock;

public class DeadLock {

    public static void main(String[] args) throws InterruptedException {
        SharedData s = new SharedData();
        Thread t1 = new Thread(new DecrementWorker(s));
        Thread t2 = new Thread(new IncrementWorker(s));
        t1.start();
        t2.start();
        System.out.println("Both Thread Started");
        t1.join();
        t2.join();

    }

    static class SharedData {

        private final Object lock1 = new Object();
        private final Object lock2 = new Object();

        private int count = 0;

        public void increase() {
            synchronized (lock1) {
                System.out.println("Acquired Lock1 in increment");
                synchronized (lock2) {
                    count++;
                }
            }

        }

        public void decrease() {
            synchronized (lock2) {
                System.out.println("Acquired Lock2 in decrement");
                synchronized (lock1) {
                    count++;
                }
            }
        }
    }


    static class IncrementWorker implements Runnable {

        private final SharedData sharedData;

        IncrementWorker(SharedData data) {
            this.sharedData = data;
        }

        @Override
        public void run() {
            for (int i = 0; i < 2000; i++) {
                sharedData.increase();
            }
        }
    }

    static class DecrementWorker implements Runnable {

        private final SharedData sharedData;

        DecrementWorker(SharedData sharedData) {
            this.sharedData = sharedData;
        }

        @Override
        public void run() {
            for (int i = 0; i < 2000; i++) {
                sharedData.decrease();
            }
        }
    }
}
