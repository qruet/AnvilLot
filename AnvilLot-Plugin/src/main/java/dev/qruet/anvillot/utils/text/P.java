package dev.qruet.anvillot.utils.text;

import dev.qruet.anvillot.AnvilLot;
import dev.qruet.anvillot.utils.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.NoSuchElementException;

public enum P {
    PV("%pv", AnvilLot.PLUGIN_VERSION),
    SV("%sv", ReflectionUtils.getVersion()),
    PLAYER("%player", ReflectionUtils.getCraftBukkitClass("entity.CraftHumanEntity"), "getName", true),
    EXP("%exp", ReflectionUtils.getCraftBukkitClass("entity.CraftPlayer"), "getLevel", true);

    private String placeholder;
    private boolean objectRequired = false;
    private Object value;

    private Class<?> clazz;
    private Method method;

    P(String placeholder, Object val) {
        this.placeholder = placeholder;
        this.value = val;
    }

    P(String placeholder, Class<?> clazz, String methodHeader, boolean objectRequired) {
        this.clazz = clazz;
        this.placeholder = placeholder;
        this.objectRequired = objectRequired;
        try {
            this.method = clazz.getDeclaredMethod(methodHeader);
        } catch (NoSuchMethodException e) { }
    }

    public String getPlaceholder() {
        return this.placeholder;
    }

    public String replace(String message, Object... objects) {
        if (value != null) {
            return message != null ? message.replaceAll(getPlaceholder(), "" + value) : "";
        }
        if(method != null) {
            Object obj = null;
            if (objectRequired) {
                if (objects == null || objects.length == 0) {
                    return message;
                }
                Object required;
                try {
                    required = Arrays.asList(objects).stream().filter(o -> clazz.isAssignableFrom(o.getClass())).findFirst().get();
                } catch(NoSuchElementException e){
                    return message;
                }
                try {
                    obj = method.invoke(required);
                } catch (IllegalAccessException | InvocationTargetException e) { }
            } else {
                try {
                    obj = method.invoke(null);
                } catch (IllegalAccessException | InvocationTargetException e) { }
            }
            return message != null ? (obj != null ? message.replaceAll(getPlaceholder(), "" + obj) : message) : "";
        }
        return message;
    }

    public static String R(String message, Object... objects) {
        for (P placeholder : values())
            message = placeholder.replace(message, objects);
        return T.C(message);
    }

}
