package com.example.demo.objectpools;

public interface ObjectPool<T> {
    T borrowObject();
    void returnObject(T object);
    void warm(int count);
    long size();
}
