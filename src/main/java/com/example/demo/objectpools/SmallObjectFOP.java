package com.example.demo.objectpools;

import cn.danielw.fop.ObjectFactory;
import cn.danielw.fop.PoolConfig;
import cn.danielw.fop.Poolable;
import com.example.demo.model.SmallObject;


public class SmallObjectFOP implements FlyObjectPool<Poolable<SmallObject>> {

    ObjectFactory<SmallObject> factory = new ObjectFactory<>() {
        @Override public SmallObject create() {
            return SmallObject.builder().build(); // create your object here
        }
        @Override public void destroy(SmallObject o) {
            // clean up and release resources
        }
        @Override public boolean validate(SmallObject o) {
            return true; // validate your object here
        }
    };

    private final cn.danielw.fop.ObjectPool<SmallObject> pool;

    public SmallObjectFOP(int size){
        PoolConfig config = new PoolConfig();
        config.setPartitionSize(5);
        config.setMaxSize(size);
        config.setMinSize(size);
        config.setMaxIdleMilliseconds(60 * 1000 * 5);
        //
        //
        // pool = new cn.danielw.fop.ObjectPool<>(config, factory);
        // DisruptorObjectPool
        pool = new cn.danielw.fop.DisruptorObjectPool<>(config, factory);
    }


    @Override
    public Poolable<SmallObject> borrowObject() {
        return pool.borrowObject();
    }

    @Override
    public void returnObject(Poolable<SmallObject> object) {
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
