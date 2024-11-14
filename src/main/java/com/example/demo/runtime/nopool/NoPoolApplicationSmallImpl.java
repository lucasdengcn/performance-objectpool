package com.example.demo.runtime.nopool;

import com.example.demo.model.SmallObject;
import com.example.demo.objectpools.NoObjectPool;
import com.example.demo.objectpools.SmallObjectBuilder;
import com.example.demo.runtime.AbstractPoolApplication;

public class NoPoolApplicationSmallImpl extends AbstractPoolApplication {

    NoObjectPool<SmallObject> pool;

    public NoPoolApplicationSmallImpl(int duration, int concurrent) {
        super(duration, concurrent);
        this.pool = new NoObjectPool<SmallObject>(new SmallObjectBuilder());
    }

    @Override
    public void poolWarmUp(int size) {

    }

    @Override
    public void poolActions() {
        SmallObject borrowObject = pool.borrowObject();
        if (null == borrowObject){
            throw new NullPointerException("Bad object Pool");
        }
        pool.returnObject(borrowObject);
    }

    @Override
    public long poolSize() {
        return pool.size();
    }

    @Override
    public void shutdown() {
        pool.shutdown();
    }

}
