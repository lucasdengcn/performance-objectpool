package com.example.demo.runtime;


import com.example.demo.runtime.blazepool.BlazePoolApplicationLargeImpl;
import com.example.demo.runtime.blazepool.BlazePoolApplicationSmallImpl;
import com.example.demo.runtime.nopool.NoPoolApplicationLargeImpl;
import com.example.demo.runtime.nopool.NoPoolApplicationSmallImpl;
import com.example.demo.runtime.simplepool.SimplePool2ApplicationLargeImpl;
import com.example.demo.runtime.simplepool.SimplePool2ApplicationSmallImpl;
import com.example.demo.runtime.simplepool.SimplePoolApplicationLargeImpl;
import com.example.demo.runtime.simplepool.SimplePoolApplicationSmallImpl;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class EntryPoint {

    public static void main(String[] args){
        // -Dobject=small or large
        String object = System.getProperty("object");
        // pool=no,simple,segment,blaze
        // -Dpool=1
        String pool = System.getProperty("pool");
        // -Dduration=10 minutes
        int duration = Integer.parseInt(System.getProperty("duration"));
        // -Dconcurrent=1000
        int concurrent = Integer.parseInt(System.getProperty("concurrent"));
        //
        Map<String, Class<?>> applicationMap = new HashMap<>();
        applicationMap.put("blaze-small", BlazePoolApplicationSmallImpl.class);
        applicationMap.put("blaze-large", BlazePoolApplicationLargeImpl.class);
        applicationMap.put("no-small", NoPoolApplicationSmallImpl.class);
        applicationMap.put("no-large", NoPoolApplicationLargeImpl.class);
        applicationMap.put("simple-small", SimplePoolApplicationSmallImpl.class);
        applicationMap.put("simple-large", SimplePoolApplicationLargeImpl.class);
        applicationMap.put("simple2-small", SimplePool2ApplicationSmallImpl.class);
        applicationMap.put("simple2-large", SimplePool2ApplicationLargeImpl.class);
        //
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String key = pool + "-" + object;
                Class<?> clazz = applicationMap.get(key);
                if (null == clazz){
                    System.out.println("Application class NOT FOUND for: poll=" + pool + ", object=" + object);
                    return;
                }
                try {
                    Constructor<?> constructor = clazz.getConstructors()[0];
                    System.out.println(constructor);
                    Object instance = constructor.newInstance(duration, concurrent);
                    ((AbstractPoolApplication)instance).start();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        thread.setName("EntryPoint");
        thread.start();
        System.out.println("Application Start");
    }

}
