package dev.qruet.anvillot.utils;

import dev.qruet.anvillot.AnvilLot;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionUtils {

    private static ReflectionUtility utility;

    static {
        Class<?> clazz = null;
        try {
            if (getJDKVersion() < 12) {
                clazz = Class.forName(ReflectionUtils.class.getPackage().getName() + ".jdk1_8.ReflectionUtils");
            } else {
                clazz = Class.forName(ReflectionUtils.class.getPackage().getName() + ".jdk1_14.ReflectionUtils");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            utility = (ReflectionUtility) clazz.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            utility = null;
            e.printStackTrace();
        } finally {
            if (utility == null) {
                AnvilLot pl = JavaPlugin.getPlugin(AnvilLot.class);
                pl.getLogger().severe("Unsupported JDK version, " + System.getProperty("java.version") + "!");
                pl.getPluginLoader().disablePlugin(pl);
            }
        }
    }

    public static String getVersion() {
        return utility.getVersion();
    }

    public static Class<?> getNMSClass(String classname) {
        return utility.getNMSClass(classname);
    }

    public static Class<?> getCraftBukkitClass(String classname) {
        return utility.getCraftBukkitClass(classname);
    }

    public static Method getMethod(String name, Class<?> clazz, Class<?>... paramTypes) {
        return utility.getMethod(name, clazz, paramTypes);
    }

    public static Method getMethod(Class<?> clazz, String name, Class<?>... args) {
        return utility.getMethod(clazz, name, args);
    }

    public static void makeNonFinal(Field field) {
        utility.makeNonFinal(field);
    }

    public static int getJDKVersion() {
        String version = System.getProperty("java.version");
        if (version.startsWith("1.")) {
            version = version.substring(2, 3);
        } else {
            int dot = version.indexOf(".");
            if (dot != -1) {
                version = version.substring(0, dot);
            }
        }
        return Integer.parseInt(version);
    }

}
