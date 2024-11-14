package com.example.demo.objectpools;

import com.example.demo.model.LargeObject;
import stormpot.Allocator;
import stormpot.Pool;
import stormpot.Pooled;
import stormpot.Slot;

public class LargeObjectBlazePool implements FlyObjectPool<Pooled<LargeObject>> {

    Allocator<Pooled<LargeObject>> allocator = new Allocator<Pooled<LargeObject>>() {

        @Override
        public Pooled<LargeObject> allocate(Slot slot) throws Exception {
            return new Pooled<>(slot, new LargeObject());
        }

        @Override
        public void deallocate(Pooled<LargeObject> poolable) throws Exception {

        }
    };

    private final Pool<Pooled<LargeObject>> pool;

    public LargeObjectBlazePool(int size){
        pool = Pool.from(allocator).setSize(10_000).build();
    }


    @Override
    public Pooled<LargeObject> borrowObject() {
        return pool.tryClaim();
    }

    @Override
    public void returnObject(Pooled<LargeObject> object) {
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
