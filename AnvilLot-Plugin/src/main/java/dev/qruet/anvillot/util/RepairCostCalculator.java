package dev.qruet.anvillot.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicInteger;

public class RepairCostCalculator {

    private static final Class<?> CraftItemStack;
    private static final Class<?> ItemStack;

    private static Method asNMSCopy;
    private static Method getRepairCost;

    static {
        CraftItemStack = ReflectionUtils.getCraftBukkitClass("inventory.CraftItemStack");
        ItemStack = ReflectionUtils.getNMSClass("ItemStack");
        try {
            asNMSCopy = CraftItemStack.getMethod("asNMSCopy", org.bukkit.inventory.ItemStack.class);
            getRepairCost = ItemStack.getMethod("getRepairCost");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public static int calculateCost(ItemStack item) {
        if (item == null || item.getType().name().toUpperCase().contains("AIR"))
            return 0;
        try {
            Object nmsItem = asNMSCopy.invoke(null, item);
            int cost = (int) getRepairCost.invoke(nmsItem);
            if (item.getType() == Material.ENCHANTED_BOOK) {
                EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
                AtomicInteger mL = new AtomicInteger();
                meta.getStoredEnchants().forEach((k, v) -> {
                    mL.set(Math.max(mL.get(), (4 % k.getMaxLevel() == 0 ?
                            4 * (int) ((double) v / ((double) k.getMaxLevel())) : v)));
                });
                cost += mL.get();
            }
            AtomicInteger mL = new AtomicInteger();
            item.getEnchantments().forEach((k, v) -> {
                mL.set(Math.max(mL.get(), v));
            });
            return cost + mL.get();

        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
