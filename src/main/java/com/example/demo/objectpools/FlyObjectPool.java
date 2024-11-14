package com.example.demo.objectpools;

public interface FlyObjectPool<T> {
    T borrowObject();
    void returnObject(T object);
    void warm(int count);
    long size();
    void shutdown();
}
