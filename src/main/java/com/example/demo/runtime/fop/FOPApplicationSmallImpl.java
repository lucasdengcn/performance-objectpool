package com.example.demo.runtime.fop;

import cn.danielw.fop.Poolable;
import com.example.demo.model.SmallObject;
import com.example.demo.objectpools.SmallObjectBlazePool;
import com.example.demo.objectpools.SmallObjectFOP;
import com.example.demo.runtime.AbstractPoolApplication;
import stormpot.Pooled;

public class FOPApplicationSmallImpl extends AbstractPoolApplication {

    SmallObjectFOP pool;

    public FOPApplicationSmallImpl(int duration, int concurrent) {
        super(duration, concurrent);
        this.pool = new SmallObjectFOP(10_000);
    }

    @Override
    public void poolWarmUp(int size) {

    }

    @Override
    public void poolActions() {
        try (Poolable<SmallObject> borrowObject = pool.borrowObject()) {
            if (null == borrowObject){
                throw new NullPointerException("Bad object Pool");
            }
        }
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
