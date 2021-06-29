package dev.qruet.anvillot.nms;

import org.bukkit.inventory.ItemStack;

/**
 * Version handler for net.minecraft ItemStack class
 * @author qruet
 */
public interface IItemStackWrapper {

    /**
     * Retrieve ItemStack's repair cost
     * @return Repair Cost
     */
    int getRepairCost();

    /**
     * Update ItemStack's repair cost
     * @param val Repair Cost
     */
    void setRepairCost(int val);

    /**
     * Get an org.bukkit ItemStack instance copy of the net.minecraft ItemStack instance
     * @return org.bukkit ItemStack
     */
    ItemStack getBukkitCopy();

    /**
     * Get an instance of NMS copy of ItemStack
     * @return
     */
    Object getNMS();

    /**
     * Check if item is "air" (exists)
     * @return net.minecraft ItemStack#isEmpty
     */
    boolean isEmpty();

}
