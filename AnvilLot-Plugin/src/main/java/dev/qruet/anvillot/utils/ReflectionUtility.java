package dev.qruet.anvillot.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public interface ReflectionUtility {

    void makeNonFinal(Field field);

    String getVersion();

    Class<?> getNMSClass(String className);

    Class<?> getCraftBukkitClass(String className);

    Method getMethod(Class<?> clazz, String name, Class<?>... args);

    Method getMethod(String name, Class<?> clazz, Class<?>... paramTypes);

    Field getField(Class<?> clazz, String name);

    Object invokeMethodWithArgs(String method, Object obj, Object... args);

}
