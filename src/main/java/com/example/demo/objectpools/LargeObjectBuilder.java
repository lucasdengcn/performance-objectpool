package com.example.demo.objectpools;

import com.example.demo.model.LargeObject;

public class LargeObjectBuilder implements ObjectBuilder<LargeObject> {

    @Override
    public LargeObject build() {
        return LargeObject.builder().build();
    }

    @Override
    public void clear(LargeObject object) {

    }

}
