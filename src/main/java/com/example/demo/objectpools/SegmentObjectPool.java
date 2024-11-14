package com.example.demo.objectpools;

public class SegmentObjectPool<T> implements ObjectPool<T> {

    @Override
    public T borrowObject() {
        return null;
    }

    @Override
    public void returnObject(T object) {

    }

    @Override
    public void warm(int count) {

    }

    @Override
    public long size() {
        return 0;
    }

}
