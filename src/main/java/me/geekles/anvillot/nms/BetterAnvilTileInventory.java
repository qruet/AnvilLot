package me.geekles.anvillot.nms;

import net.minecraft.server.v1_15_R1.*;
import org.bukkit.Location;

public class BetterAnvilTileInventory implements ITileInventory {

    private final IChatBaseComponent a;

    public BetterAnvilTileInventory(IChatBaseComponent var1) {
        this.a = var1;
    }

    @Override
    public IChatBaseComponent getScoreboardDisplayName() {
        return this.a;
    }

    @Override
    public Container createMenu(int var1, PlayerInventory var2, EntityHuman var3) {
        Location location = var3.getBukkitEntity().getLocation();
        return new BetterContainerAnvil(var1, var2, ContainerAccess.at(var3.world, new BlockPosition(location.getX(), location.getY(), location.getZ())));
    }

}
