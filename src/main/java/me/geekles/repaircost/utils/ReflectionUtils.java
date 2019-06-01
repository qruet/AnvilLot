package me.geekles.repaircost.utils;


import org.bukkit.Bukkit;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A reflection class used to manage all things NMS related to avoid being version dependent
 */
public class ReflectionUtils {

    public static final Map<Class<?>, Class<?>> CORRESPONDING_TYPES = new HashMap<>();

    public static Class<?> getPrimitiveType(Class<?> clazz) {
        return CORRESPONDING_TYPES.containsKey(clazz) ? CORRESPONDING_TYPES.get(clazz) : clazz;
    }

    public static Class<?>[] toPrimitiveTypeArray(Class<?>[] classes) {
        int a = classes != null ? classes.length : 0;
        Class<?>[] types = new Class<?>[a];
        for (int i = 0; i < a; i++) {
            types[i] = getPrimitiveType(classes[i]);
        }
        return types;
    }

    public static boolean equalsTypeArray(Class<?>[] a, Class<?>[] o) {
        if (a.length != o.length)
            return false;
        for (int i = 0; i < a.length; i++)
            if (!a[i].equals(o[i]) && !a[i].isAssignableFrom(o[i]))
                return false;
        return true;
    }

    public static Object getHandle(Object obj) {
        try {
            return getMethod("getHandle", obj.getClass()).invoke(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     *
     * @param method Method to be invoked
     * @param obj Instance of class where method exists
     * @return Returns any objects that the method may return
     */
    public static Object invokeMethod(String method, Object obj) {
        try {
            return getMethod(method, obj.getClass()).invoke(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Calls a method
     * @param method Method to be invoked
     * @param obj Instance of the class where method exists
     * @param args Arguments in the method to be passed
     * @return Returns any objects that the method may return
     */
    public static Object invokeMethodWithArgs(String method, Object obj, Object... args) {
        try {
            return getMethod(method, obj.getClass()).invoke(obj, args);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Retrieves a method instance
     * @param name Name of method
     * @param clazz Class where method exists
     * @param paramTypes Parameters the class may have
     * @return Instance of method
     */
    public static Method getMethod(String name, Class<?> clazz, Class<?>... paramTypes) {
        Class<?>[] t = toPrimitiveTypeArray(paramTypes);
        for (Method m : clazz.getMethods()) {
            Class<?>[] types = toPrimitiveTypeArray(m.getParameterTypes());
            if (m.getName().equals(name) && equalsTypeArray(types, t))
                return m;
        }
        return null;
    }

    /**
     * Get version of server
     * @return String version
     */
    public static String getVersion() {
        String name = Bukkit.getServer().getClass().getPackage().getName();
        return name.substring(name.lastIndexOf('.') + 1);
    }

    /**
     * Converts string name of class to Class object
     * @param className Name of class
     * @return Class object
     */
    public static Class<?> getNMSClass(String className) {
        String fullName = "net.minecraft.server." + getVersion() + "." + className;
        Class<?> clazz = null;
        try {
            clazz = Class.forName(fullName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clazz;
    }

    /**
     * Use this instead of getNMSClass for classes in the craftbukkit package
     * @param className Name of class
     * @return Class Object
     */
    public static Class<?> getCraftBukkitClass(String className) {
        String fullName = "org.bukkit.craftbukkit." + getVersion() + "." + className;
        Class<?> clazz = null;
        try {
            clazz = Class.forName(fullName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clazz;
    }

    /**
     * Retrieve fields from classes
     * @param clazz Class where field exists
     * @param name Name of field
     * @return Field object
     */
    public static Field getField(Class<?> clazz, String name) {
        try {
            Field field = clazz.getDeclaredField(name);
            field.setAccessible(true);
            return field;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Retrieve method instance
     * @param clazz Name of class where method exists
     * @param name Name of method
     * @param args Parameters that method may have
     * @return Method instance
     */
    public static Method getMethod(Class<?> clazz, String name, Class<?>... args) {
        for (Method m : clazz.getMethods())
            if (m.getName().equals(name)
                    && (args.length == 0 || ClassListEqual(args,
                    m.getParameterTypes()))) {
                m.setAccessible(true);
                return m;
            }
        return null;
    }

    public static boolean ClassListEqual(Class<?>[] l1, Class<?>[] l2) {
        boolean equal = true;
        if (l1.length != l2.length)
            return false;
        for (int i = 0; i < l1.length; i++)
            if (l1[i] != l2[i]) {
                equal = false;
                break;
            }
        return equal;
    }

}
