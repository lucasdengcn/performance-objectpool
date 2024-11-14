package com.example.demo.objectpools;

import java.util.concurrent.atomic.AtomicLong;

public class NoObjectPool<T> implements FlyObjectPool<T> {

    private final AtomicLong count = new AtomicLong(0);
    private final ObjectBuilder<T> builder;

    public NoObjectPool(ObjectBuilder<T> builder) {
        this.builder = builder;
    }

    @Override
    public T borrowObject() {
        count.incrementAndGet();
        return builder.build();
    }

    @Override
    public void returnObject(T object) {
        object = null;
    }

    @Override
    public void warm(int count) {

    }

    @Override
    public long size() {
        return count.get();
    }

    @Override
    public void shutdown() {

    }
}
