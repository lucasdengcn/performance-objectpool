package com.example.demo.objectpools;

import cn.danielw.fop.ObjectFactory;
import cn.danielw.fop.PoolConfig;
import cn.danielw.fop.Poolable;
import com.example.demo.model.LargeObject;


public class LargeObjectFOP implements FlyObjectPool<Poolable<LargeObject>> {

    ObjectFactory<LargeObject> factory = new ObjectFactory<>() {
        @Override public LargeObject create() {
            return LargeObject.builder().build(); // create your object here
        }
        @Override public void destroy(LargeObject o) {
            // clean up and release resources
        }
        @Override public boolean validate(LargeObject o) {
            return true; // validate your object here
        }
    };

    private final cn.danielw.fop.ObjectPool<LargeObject> pool;

    public LargeObjectFOP(int size){
        PoolConfig config = new PoolConfig();
        config.setPartitionSize(5);
        config.setMaxSize(size);
        config.setMinSize(size);
        config.setMaxIdleMilliseconds(60 * 1000 * 5);
        //
        pool = new cn.danielw.fop.ObjectPool<>(config, factory);
    }


    @Override
    public Poolable<LargeObject> borrowObject() {
        return pool.borrowObject();
    }

    @Override
    public void returnObject(Poolable<LargeObject> object) {
        pool.returnObject(object);
    }

    @Override
    public void warm(int count) {

    }

    @Override
    public long size() {
        return pool.getSize();
    }

    @Override
    public void shutdown() {
        try {
            pool.shutdown();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
