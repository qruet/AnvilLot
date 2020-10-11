package dev.qruet.anvillot.util;

import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Manages NBT data of an item
 *
 * @author Qruet
 */
public class NBTUtil {

    private static final Class<?> CRAFT_ITEM_STACK = ReflectionUtils.getCraftBukkitClass("inventory.CraftItemStack");
    private static final Class<?> ITEMSTACK = ReflectionUtils.getNMSClass("ItemStack");
    private static final Class<?> NBT_TAG_COMPOUND = ReflectionUtils.getNMSClass("NBTTagCompound");
    private static final Class<?> NBTBase = ReflectionUtils.getNMSClass("NBTBase");

    public static ItemStack set(ItemStack item, String key, String value) {
        try {
            Method asNMSCopy = CRAFT_ITEM_STACK.getMethod("asNMSCopy", ItemStack.class);
            Method asCraftMirror = CRAFT_ITEM_STACK.getMethod("asCraftMirror", ITEMSTACK);
            Method setString = NBT_TAG_COMPOUND.getMethod("setString", String.class, String.class);
            Method getTag = ITEMSTACK.getMethod("getTag");
            Method setTag = ITEMSTACK.getMethod("setTag", NBT_TAG_COMPOUND);

            Object craftitem = asNMSCopy.invoke(null, item);
            Object tag = getTag.invoke(craftitem);
            if (tag == null)
                tag = NBT_TAG_COMPOUND.getConstructor().newInstance();
            setString.invoke(tag, key, value);
            setTag.invoke(craftitem, tag);
            return (ItemStack) asCraftMirror.invoke(null, craftitem);

        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }
        return item;
    }

    public static ItemStack remove(ItemStack item, String key) {
        try {
            Method asNMSCopy = CRAFT_ITEM_STACK.getMethod("asNMSCopy", ItemStack.class);
            Method asCraftMirror = CRAFT_ITEM_STACK.getMethod("asCraftMirror", ITEMSTACK);
            Method remove = NBT_TAG_COMPOUND.getMethod("remove", String.class);
            Method getTag = ITEMSTACK.getMethod("getTag");
            Method setTag = ITEMSTACK.getMethod("setTag", NBT_TAG_COMPOUND);

            Object craftitem = asNMSCopy.invoke(null, item);
            Object tag = getTag.invoke(craftitem);
            if (tag == null)
                return item;

            remove.invoke(tag, key);
            setTag.invoke(craftitem, tag);
            return (ItemStack) asCraftMirror.invoke(null, craftitem);

        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return item;
    }

    public static String get(ItemStack item, String key) {
        try {
            Method asNMSCopy = CRAFT_ITEM_STACK.getMethod("asNMSCopy", ItemStack.class);
            Method getString = NBT_TAG_COMPOUND.getMethod("getString", String.class);
            Method getTag = ITEMSTACK.getMethod("getTag");

            Object craftitem = asNMSCopy.invoke(null, item);
            Object tag = getTag.invoke(craftitem);
            if (tag == null)
                return null;

            String val = "" + getString.invoke(tag, key);
            return val;

        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String toString(ItemStack item) {
        try {
            Method asNMSCopy = CRAFT_ITEM_STACK.getMethod("asNMSCopy", ItemStack.class);
            Method getTag = ITEMSTACK.getMethod("getTag");
            Method toString = NBT_TAG_COMPOUND.getMethod("toString");

            Object craftitem = asNMSCopy.invoke(null, item);
            Object tag = getTag.invoke(craftitem);
            if (tag == null)
                return null;

            return (String) toString.invoke(tag);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

}
