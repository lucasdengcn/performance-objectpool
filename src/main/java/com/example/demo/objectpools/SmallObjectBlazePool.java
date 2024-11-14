package com.example.demo.objectpools;

import com.example.demo.model.SmallObject;
import stormpot.*;

import java.util.concurrent.TimeUnit;

public class SmallObjectBlazePool implements FlyObjectPool<Pooled<SmallObject>> {

    Allocator<Pooled<SmallObject>> allocator = new Allocator<Pooled<SmallObject>>() {

        @Override
        public Pooled<SmallObject> allocate(Slot slot) throws Exception {
            return new Pooled<>(slot, new SmallObject());
        }

        @Override
        public void deallocate(Pooled<SmallObject> poolable) throws Exception {

        }
    };

    private final Pool<Pooled<SmallObject>> pool;
    private final Timeout timeout;

    public SmallObjectBlazePool(int size){
        pool = Pool.from(allocator).setSize(size).build();
        timeout = new Timeout(200, TimeUnit.MILLISECONDS);
    }


    @Override
    public Pooled<SmallObject> borrowObject() {
        try {
            return pool.claim(timeout);
        } catch (PoolException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void returnObject(Pooled<SmallObject> object) {
        object.release();
    }

    @Override
    public void warm(int count) {

    }

    @Override
    public long size() {
        return pool.getTargetSize();
    }

    @Override
    public void shutdown() {
        pool.shutdown();
    }

}
