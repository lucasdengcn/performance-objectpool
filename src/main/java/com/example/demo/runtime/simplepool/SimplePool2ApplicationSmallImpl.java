package com.example.demo.runtime.simplepool;

import com.example.demo.model.SmallObject;
import com.example.demo.objectpools.FlyObjectPool;
import com.example.demo.objectpools.SimpleObjectPool2;
import com.example.demo.objectpools.SmallObjectBuilder;
import com.example.demo.runtime.AbstractPoolApplication;

public class SimplePool2ApplicationSmallImpl extends AbstractPoolApplication {

    private final FlyObjectPool<SmallObject> objectPool;

    public SimplePool2ApplicationSmallImpl(int duration, int concurrent){
        super(duration, concurrent);
        objectPool = new SimpleObjectPool2<>(new SmallObjectBuilder());
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
