package dev.qruet.anvillot.nms;

import dev.qruet.anvillot.config.GeneralPresets;
import dev.qruet.anvillot.util.ReflectionUtils;
import dev.qruet.anvillot.util.RepairCostCalculator;
import dev.qruet.anvillot.util.java.Pair;
import dev.qruet.anvillot.util.math.Equation;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

/**
 * @author qruet
 */
public interface IContainerAnvilLot {

    /**
     * Retrieve owner of inventory (current viewer)
     *
     * @return Player
     */
    Player getOwner();

    /**
     * Retrieve assigned repair cost
     *
     * @return Cost
     */
    int getCost();

    /**
     * Maximum possible repair cost
     *
     * @return Cost
     */
    int getMaximumCost();

    /**
     * Get updated name of result item
     *
     * @return Result item's name
     */
    String getRenameText();

    /**
     * Updates cost and correlating bar messages
     *
     * @param val New cost
     */
    void updateRepairCost(int val);

    default void sendSlotUpdate(int slot, IItemStackWrapper item, int id) {
        Class<?> PacketPlayOutSetSlot = null, ItemStack = null, CraftPlayer = null;

        CraftPlayer = ReflectionUtils.getCraftBukkitClass("entity.CraftPlayer");

        PacketPlayOutSetSlot = ReflectionUtils.getNMSClass("PacketPlayOutSetSlot");
        ItemStack = ReflectionUtils.getNMSClass("ItemStack");

        Object packet = null;
        try {
            packet = PacketPlayOutSetSlot.getConstructor(int.class, int.class, ItemStack).newInstance(id, slot, item.getNMS());
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException | SecurityException e) {
            e.printStackTrace();
        }

        Object player = ReflectionUtils.invokeMethod("getHandle", CraftPlayer.cast(getOwner()));
        Object playerConnection = ReflectionUtils.getField(player, "playerConnection");

        ReflectionUtils.invokeMethodWithArgs("sendPacket", playerConnection, packet);
    }

    default void sendSlotUpdate(int slot, IItemStackWrapper item, int id, int sId) {
        Class<?> PacketPlayOutSetSlot = null, ItemStack = null, CraftPlayer = null;

        CraftPlayer = ReflectionUtils.getCraftBukkitClass("entity.CraftPlayer");

        try {
            PacketPlayOutSetSlot = Class.forName("net.minecraft.network.protocol.game.PacketPlayOutSetSlot");
            ItemStack = Class.forName("net.minecraft.world.item.ItemStack");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        Object packet = null;
        try {
            packet = PacketPlayOutSetSlot.getConstructor(int.class, int.class, int.class, ItemStack).newInstance(id, sId, slot, item.getNMS());
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException | SecurityException e) {
            e.printStackTrace();
        }

        Object player = ReflectionUtils.invokeMethod("getHandle", CraftPlayer.cast(getOwner()));
        Object playerConnection = ReflectionUtils.getField(player, "b");
        ReflectionUtils.invokeMethodWithArgs(VersionHandlerNMS.MINECRAFT_BASE_VERSION > 17 ? "a" : "sendPacket", playerConnection, packet);
    }

    /**
     * Handle anvil specific cost calculations
     *
     * @param first     First slot item
     * @param second    Second slot item
     * @param result    Result slot item
     * @param levelCost Original repair cost
     */
    default void calculate(IItemStackWrapper first, IItemStackWrapper second, IItemStackWrapper result, int levelCost) {
        if (getMaximumCost() != -1 && levelCost > getMaximumCost()) {
            if (GeneralPresets.HARD_LIMIT) {
                updateRepairCost(-1);
            } else {
                updateRepairCost(getMaximumCost());
            }
        } else {
            int rPa = first.getRepairCost();
            int rPb = RepairCostCalculator.calculateCost(second.getBukkitCopy());

            int bonus = 0;
            if (!first.getName().equals(getRenameText())) {
                bonus++;
            }

            int cost = (int) Equation.evaluate(GeneralPresets.REPAIR_COST_EQUATION,
                    new Pair<>("first_item", (double) rPa),
                    new Pair<>("second_item", (double) rPb),
                    new Pair<>("rename_fee", (double) bonus));

            if (getMaximumCost() != -1 && levelCost > getMaximumCost()) {
                if (GeneralPresets.HARD_LIMIT) {
                    updateRepairCost(-1);
                }
            } else {
                updateRepairCost(cost);

                if (!result.isEmpty()) {
                    int eval = (int) Equation.evaluate(GeneralPresets.REPAIR_PROGRESSION_EQUATION,
                            new Pair<>("first_item", (double) rPa),
                            new Pair<>("second_item", (double) rPb));
                    result.setRepairCost(eval); //increment result item's repair cost
                }
            }
        }
    }

}
