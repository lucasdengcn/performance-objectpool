package com.example.demo.runtime.simplepool;

import com.example.demo.model.SmallObject;
import com.example.demo.objectpools.*;
import com.example.demo.runtime.AbstractPoolApplication;

public class SimplePoolApplicationSmallImpl extends AbstractPoolApplication {

    private final FlyObjectPool<SmallObject> objectPool;

    public SimplePoolApplicationSmallImpl(int duration, int concurrent){
        super(duration, concurrent);
        objectPool = new SimpleObjectPool<>(new SmallObjectBuilder());
    }

    @Override
    public void poolWarmUp(int size) {
        objectPool.warm(size);
    }

    @Override
    public void poolActions() {
        SmallObject object = objectPool.borrowObject();
        if (null == object) {
            throw new NullPointerException("Bad pool object");
        }
        objectPool.returnObject(object);
    }

    @Override
    public long poolSize() {
        return objectPool.size();
    }

    @Override
    public void shutdown() {
        objectPool.shutdown();
    }

}
