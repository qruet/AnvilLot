package dev.qruet.anvillot.util.jdk1_8;


import dev.qruet.anvillot.util.ReflectionUtility;
import org.bukkit.Bukkit;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * A reflection class used to manage all things NMS related to avoid being version dependent
 * @author qruet
 */
public class ReflectionUtils implements ReflectionUtility {

    private final Map<Class<?>, Class<?>> CORRESPONDING_TYPES = new HashMap<>();

    private Class<?> getPrimitiveType(Class<?> clazz) {
        return CORRESPONDING_TYPES.containsKey(clazz) ? CORRESPONDING_TYPES.get(clazz) : clazz;
    }

    static {
         if(dev.qruet.anvillot.util.ReflectionUtils.getJDKVersion() == 11) {
             //Hide WARNING: An illegal reflective access operation has occurred that occurs in version 11
             //System.err.close();
             //System.setErr(System.out);
         }
    }

    @Override
    public void makeNonFinal(Field field) {
        try {
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        } catch(IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    private Class<?>[] toPrimitiveTypeArray(Class<?>[] classes) {
        int a = classes != null ? classes.length : 0;
        Class<?>[] types = new Class<?>[a];
        for (int i = 0; i < a; i++) {
            types[i] = getPrimitiveType(classes[i]);
        }
        return types;
    }

    private boolean equalsTypeArray(Class<?>[] a, Class<?>[] o) {
        if (a.length != o.length)
            return false;
        for (int i = 0; i < a.length; i++)
            if (!a[i].equals(o[i]) && !a[i].isAssignableFrom(o[i]))
                return false;
        return true;
    }

    private Object getHandle(Object obj) {
        try {
            return getMethod("getHandle", obj.getClass()).invoke(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param method Method to be invoked
     * @param obj    Instance of class where method exists
     * @return Returns any objects that the method may return
     */
    public Object invokeMethod(String method, Object obj) {
        try {
            return getMethod(method, obj.getClass()).invoke(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Calls a method
     *
     * @param method Method to be invoked
     * @param obj    Instance of the class where method exists
     * @param args   Arguments in the method to be passed
     * @return Returns any objects that the method may return
     */
    public Object invokeMethodWithArgs(String method, Object obj, Object... args) {
        try {
            return getMethod(method, obj.getClass()).invoke(obj, args);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Retrieves a method instance
     *
     * @param name       Name of method
     * @param clazz      Class where method exists
     * @param paramTypes Parameters the class may have
     * @return Instance of method
     */
    public Method getMethod(String name, Class<?> clazz, Class<?>... paramTypes) {
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
     *
     * @return String version
     */
    public String getVersion() {
        String name = Bukkit.getServer().getClass().getPackage().getName();
        return name.substring(name.lastIndexOf('.') + 1);
    }

    /**
     * Converts string name of class to Class object
     *
     * @param className Name of class
     * @return Class object
     */
    public Class<?> getNMSClass(String className) {
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
     *
     * @param className Name of class
     * @return Class Object
     */
    public Class<?> getCraftBukkitClass(String className) {
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
     *
     * @param clazz Class where field exists
     * @param name  Name of field
     * @return Field object
     */
    public Field getField(Class<?> clazz, String name) {
        try {
            Field field = clazz.getDeclaredField(name);
            field.setAccessible(true);
            makeNonFinal(field);
            return field;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Access/Update final fields
     *
     * @param field
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public static void disableFinal(Field field) throws NoSuchFieldException, IllegalAccessException {
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
    }

    /**
     * Retrieve method instance
     *
     * @param clazz Name of class where method exists
     * @param name  Name of method
     * @param args  Parameters that method may have
     * @return Method instance
     */
    public Method getMethod(Class<?> clazz, String name, Class<?>... args) {
        for (Method m : clazz.getDeclaredMethods())
            if (m.getName().equals(name)
                    && (args.length == 0 || ClassListEqual(args,
                    m.getParameterTypes()))) {
                m.setAccessible(true);
                return m;
            }
        return null;
    }

    private boolean ClassListEqual(Class<?>[] l1, Class<?>[] l2) {
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
