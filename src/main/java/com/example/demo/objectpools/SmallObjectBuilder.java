package com.example.demo.objectpools;

import com.example.demo.model.SmallObject;

public class SmallObjectBuilder implements ObjectBuilder<SmallObject> {

    @Override
    public SmallObject build() {
        return SmallObject.builder().build();
    }

    @Override
    public void clear(SmallObject object) {

    }

}
