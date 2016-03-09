/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kurniakue.trxreader.data;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author harun1
 */
public abstract class Persistor {

    private static final ThreadLocal<Map<Class<? extends Persistor>, Persistor>> persistorMap = 
            new ThreadLocal<>();

    public static <T extends Persistor> T get(Class<T> clazz) {
        Map<Class<? extends Persistor>, Persistor> map = persistorMap.get();
        if (map == null)
        {
            map = new HashMap<>();
            persistorMap.set(map);
        }
        Persistor persistorObj = map.get(clazz);
        if (persistorObj == null) {
            persistorObj = newPersistor(clazz);
            map.put(clazz, persistorObj);
        }
        if (clazz.isAssignableFrom(persistorObj.getClass()))
        {
            return (T) persistorObj;
        }
        else
        {
            return null;
        }
    }

    private static <T extends Persistor> Persistor newPersistor(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public abstract void init();

    public abstract void stop();

    public abstract void persist(Record record);

    public abstract void reset();

    public abstract boolean exists(String path);

}
