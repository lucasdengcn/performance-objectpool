package com.example.demo.runtime.blazepool;

import com.example.demo.model.SmallObject;
import com.example.demo.objectpools.SmallObjectBlazePool;
import com.example.demo.runtime.AbstractPoolApplication;
import stormpot.Pooled;

public class BlazePoolApplicationSmallImpl extends AbstractPoolApplication {

    SmallObjectBlazePool pool;

    public BlazePoolApplicationSmallImpl(int duration, int concurrent) {
        super(duration, concurrent);
        this.pool = new SmallObjectBlazePool(100_000);
    }

    @Override
    public void poolWarmUp(int size) {

    }

    @Override
    public void poolActions() {
        try (Pooled<SmallObject> borrowObject = pool.borrowObject()) {
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
