package com.example.demo.runtime.blazepool;

import com.example.demo.model.LargeObject;
import com.example.demo.objectpools.LargeObjectBlazePool;
import com.example.demo.runtime.AbstractPoolApplication;
import stormpot.Pooled;

public class BlazePoolApplicationLargeImpl extends AbstractPoolApplication {

    LargeObjectBlazePool pool;

    public BlazePoolApplicationLargeImpl(int duration, int concurrent) {
        super(duration, concurrent);
        this.pool = new LargeObjectBlazePool(100_000);
    }

    @Override
    public void poolWarmUp(int size) {

    }

    @Override
    public void poolActions() {
        try (Pooled<LargeObject> borrowObject = pool.borrowObject()) {
            if (null == borrowObject){
                throw new NullPointerException("Bad object Pool");
            }
        }
    }

    @Override
    public long poolSize() {
        return pool.size();
    }

}
