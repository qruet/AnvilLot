package dev.qruet.anvillot.nms.v1_17_R1;

import net.minecraft.core.BlockPosition;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.world.ITileInventory;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.entity.player.PlayerInventory;
import net.minecraft.world.inventory.Container;
import net.minecraft.world.inventory.ContainerAccess;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_17_R1.block.CraftBlock;

/**
 * @author qruet
 * @version 3.1.0-Beta-SNAPSHOT
 */
public class AnvilLotTileInventory implements ITileInventory {

    private final IChatBaseComponent a;
    private final BlockPosition pos;

    public AnvilLotTileInventory(Block block, IChatBaseComponent var1) {
        this.a = var1;
        this.pos = ((CraftBlock) block).getPosition();
    }

    @Override
    public IChatBaseComponent getScoreboardDisplayName() {
        return this.a;
    }

    @Override
    public Container createMenu(int var1, PlayerInventory var2, EntityHuman var3) {
        return new ContainerAnvilLot(var1, var2, ContainerAccess.at(var3.getWorld(), pos));
    }

}
