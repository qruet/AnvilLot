package dev.qruet.anvillot.nms.v1_13_R1;

import net.minecraft.server.v1_13_R2.*;
import org.bukkit.Location;

import javax.annotation.Nullable;

/**
 * A rewritten form of ITileInventory. Handles opening the custom AnvilLot container
 *
 * @author Qruet
 * @version 3.1.0-Beta-Snapshot
 */
public class AnvilLotTileInventory implements ITileEntityContainer {

    private final IChatBaseComponent a;

    public AnvilLotTileInventory(IChatBaseComponent var1) {
        this.a = var1;
    }

    @Override
    public IChatBaseComponent getDisplayName() {
        return new ChatMessage(Blocks.ANVIL.m(), new Object[0]);
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Nullable
    public IChatBaseComponent getCustomName() {
        return null;
    }

    @Override
    public IChatBaseComponent getScoreboardDisplayName() {
        return this.a;
    }

    @Override
    public Container createContainer(PlayerInventory var1, EntityHuman var2) {
        Location location = var2.getBukkitEntity().getLocation();
        return new ContainerAnvilLot(var1, var2.world, new BlockPosition(location.getX(), location.getY(), location.getZ()), var2);
    }

    @Override
    public String getContainerName() {
        return "minecraft:anvil";
    }

}
