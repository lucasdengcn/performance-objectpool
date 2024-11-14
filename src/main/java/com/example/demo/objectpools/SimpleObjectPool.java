package com.example.demo.objectpools;

import java.util.concurrent.ConcurrentLinkedQueue;

public class SimpleObjectPool<T> implements FlyObjectPool<T> {

    private final ObjectBuilder<T> builder;
    private final ConcurrentLinkedQueue<T> objects;

    public SimpleObjectPool(ObjectBuilder<T> builder) {
        this.builder = builder;
        objects = new ConcurrentLinkedQueue<>();
    }

    public T borrowObject() {
        T object = objects.poll();
        if (null == object) {
            object = builder.build();
        }
        return object;
    }

    public void returnObject(T object) {
        if (null == object){
            return;
        }
        builder.clear(object);
        objects.add(object);
    }

    public void warm(int count){
        for (int i = 0; i < count; i++) {
            T object = builder.build();
            objects.add(object);
        }
    }

    public long size() {
        return objects.size();
    }

    @Override
    public void shutdown() {
        objects.clear();
    }

}
