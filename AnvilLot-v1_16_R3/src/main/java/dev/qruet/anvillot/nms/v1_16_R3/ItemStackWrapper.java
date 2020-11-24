package dev.qruet.anvillot.nms.v1_16_R3;

import dev.qruet.anvillot.nms.IItemStackWrapper;
import net.minecraft.server.v1_16_R3.ItemStack;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;

public class ItemStackWrapper implements IItemStackWrapper {

    private final ItemStack itemStack;

    public ItemStackWrapper(final ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    @Override
    public int getRepairCost() {
        return itemStack.getRepairCost();
    }

    @Override
    public void setRepairCost(int val) {
        itemStack.setRepairCost(val);
    }

    @Override
    public org.bukkit.inventory.ItemStack getBukkitCopy() {
        return CraftItemStack.asBukkitCopy(itemStack);
    }

    @Override
    public boolean isEmpty() {
        return itemStack.isEmpty();
    }

    public ItemStack getItemStack() {
        return itemStack;
    }
}
