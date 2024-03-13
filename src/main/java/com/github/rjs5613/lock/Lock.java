package com.github.rjs5613.lock;

public interface Lock {

    void lock() throws InterruptedException;

    void unlock() throws InterruptedException;
}
