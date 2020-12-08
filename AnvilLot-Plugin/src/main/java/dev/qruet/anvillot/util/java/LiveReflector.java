package dev.qruet.anvillot.util.java;

import java.lang.reflect.Field;

public class LiveReflector<T> {

    private final Object key;
    private final Field field;

    public LiveReflector(Object key, Field field) {
        this.key = key;
        this.field = field;
        this.field.setAccessible(true);
    }

    public T get() {
        T t = null;
        try {
            t = (T) field.get(key);
        } catch(IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return t;
    }

}
