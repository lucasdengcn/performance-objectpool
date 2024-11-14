package com.example.demo.runtime.fop;

import cn.danielw.fop.Poolable;
import com.example.demo.model.LargeObject;
import com.example.demo.model.SmallObject;
import com.example.demo.objectpools.LargeObjectFOP;
import com.example.demo.objectpools.SmallObjectFOP;
import com.example.demo.runtime.AbstractPoolApplication;

public class FOPApplicationLargeImpl extends AbstractPoolApplication {

    LargeObjectFOP pool;

    public FOPApplicationLargeImpl(int duration, int concurrent) {
        super(duration, concurrent);
        this.pool = new LargeObjectFOP(10_000);
    }

    @Override
    public void poolWarmUp(int size) {

    }

    @Override
    public void poolActions() {
        try (Poolable<LargeObject> borrowObject = pool.borrowObject()) {
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
