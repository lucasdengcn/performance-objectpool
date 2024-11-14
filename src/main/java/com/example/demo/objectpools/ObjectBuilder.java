package com.example.demo.objectpools;

public interface ObjectBuilder<T> {

    T build();

    void clear(T object);
}
