package me.geekles.repaircost.assets.items;

import me.geekles.repaircost.utils.MaterialLibrary;
import me.geekles.repaircost.utils.ReflectionUtils;
import me.geekles.repaircost.utils.config.items.PlaceholderItemPreset;
import me.geekles.repaircost.utils.text.P;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class PlaceholderItem extends ItemStack {

    private static final Class<?> CraftItemStack = ReflectionUtils.getCraftBukkitClass("inventory.CraftItemStack");
    private static final Class<?> ItemStack = ReflectionUtils.getNMSClass("ItemStack");
    private static final Class<?> NBTTagCompound = ReflectionUtils.getNMSClass("NBTTagCompound");
    private static final Class<?> NBTTagString = ReflectionUtils.getNMSClass("NBTTagString");
    private static final Class<?> NBTBase = ReflectionUtils.getNMSClass("NBTBase");

    private static final String KEY = "maxrepaircost";
    private static final String VALUE = "placeholder";

    public PlaceholderItem() {
        super(PlaceholderItemPreset.getMaterial(), 1);
        if (!MaterialLibrary.isNewVersion()) { //only to be run if version is less then 1.13
            try {
                Class<?> MaterialData = Class.forName("org.bukkit.material.MaterialData");
                Method setData = MaterialData.getDeclaredMethod("setData", byte.class);

                Object data = getData();
                setData.invoke(data, PlaceholderItemPreset.getIdentifier());
                setData.invoke(this, data);
            } catch(ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e){
                e.printStackTrace();
            }
        }

        setItemMeta(PlaceholderItemPreset.getItemMeta());
        try {
            Method asNMSCopy = CraftItemStack.getDeclaredMethod("asNMSCopy", ItemStack.class);
            Method getTag = ItemStack.getDeclaredMethod("getTag");
            Method set = NBTTagCompound.getDeclaredMethod("set", String.class, NBTBase);
            Method setTag = ItemStack.getDeclaredMethod("setTag", NBTTagCompound);
            Method asBukkitCopy = CraftItemStack.getDeclaredMethod("asBukkitCopy", ItemStack);

            Object stack = asNMSCopy.invoke(null, this);
            Object tag = getTag.invoke(stack) != null ? getTag.invoke(stack) : NBTTagCompound.getConstructor().newInstance();
            set.invoke(tag, KEY, NBTTagString.getConstructor(String.class).newInstance(VALUE));
            setTag.invoke(stack, tag);
            setItemMeta(((ItemStack)asBukkitCopy.invoke(null, stack)).getItemMeta());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    public PlaceholderItem(Player player){
        this();
        ItemMeta meta = getItemMeta();
        meta.setDisplayName(P.R(meta.getDisplayName(), player));
        List<String> lore = meta.getLore();
        lore.stream().forEach(s -> lore.set(lore.indexOf(s), P.R(s, player)));
        meta.setLore(lore);
        setItemMeta(meta);
    }

    public static boolean equals(ItemStack item) {
        try {
            Method asNMSCopy = CraftItemStack.getDeclaredMethod("asNMSCopy", ItemStack.class);
            Method getTag = ItemStack.getDeclaredMethod("getTag");
            Method getString = NBTTagCompound.getDeclaredMethod("getString", String.class);

            Object stack = asNMSCopy.invoke(null, item);
            Object tag = getTag.invoke(stack) != null ? getTag.invoke(stack) : NBTTagCompound.getConstructor().newInstance();
            if(tag == null)
                return false;
            return getString.invoke(tag, KEY).equals(VALUE);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }
        return false;
    }
}
