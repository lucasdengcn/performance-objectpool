package com.example.demo.runtime.nopool;

import com.example.demo.model.LargeObject;
import com.example.demo.objectpools.LargeObjectBuilder;
import com.example.demo.objectpools.NoObjectPool;
import com.example.demo.runtime.AbstractPoolApplication;

public class NoPoolApplicationLargeImpl extends AbstractPoolApplication {

    NoObjectPool<LargeObject> pool;

    public NoPoolApplicationLargeImpl(int duration, int concurrent) {
        super(duration, concurrent);
        this.pool = new NoObjectPool<LargeObject>(new LargeObjectBuilder());
    }

    @Override
    public void poolWarmUp(int size) {

    }

    @Override
    public void poolActions() {
        LargeObject borrowObject = pool.borrowObject();
        if (null == borrowObject){
            throw new NullPointerException("Bad object Pool");
        }
        pool.returnObject(borrowObject);
    }

    @Override
    public long poolSize() {
        return pool.size();
    }

}
