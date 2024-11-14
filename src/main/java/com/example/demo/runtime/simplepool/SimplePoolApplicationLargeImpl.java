package com.example.demo.runtime.simplepool;

import com.example.demo.model.LargeObject;
import com.example.demo.model.SmallObject;
import com.example.demo.objectpools.LargeObjectBuilder;
import com.example.demo.objectpools.ObjectPool;
import com.example.demo.objectpools.SimpleObjectPool;
import com.example.demo.objectpools.SmallObjectBuilder;
import com.example.demo.runtime.AbstractPoolApplication;

public class SimplePoolApplicationLargeImpl extends AbstractPoolApplication {

    private final ObjectPool<LargeObject> objectPool;

    public SimplePoolApplicationLargeImpl(int duration, int concurrent){
        super(duration, concurrent);
        objectPool = new SimpleObjectPool<>(new LargeObjectBuilder());
    }

    @Override
    public void poolWarmUp(int size) {
        objectPool.warm(size);
    }

    @Override
    public void poolActions() {
        LargeObject object = objectPool.borrowObject();
        if (null == object) {
            throw new NullPointerException("Bad pool object");
        }
        objectPool.returnObject(object);
    }

    @Override
    public long poolSize() {
        return objectPool.size();
    }

}
