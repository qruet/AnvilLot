package me.geekles.anvillot.listeners;

import me.geekles.anvillot.nms.BetterAnvilTileInventory;
import me.geekles.anvillot.utils.ReflectionUtils;
import me.geekles.anvillot.utils.Tasky;
import net.minecraft.server.v1_15_R1.ChatMessage;
import net.minecraft.server.v1_15_R1.EntityPlayer;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

public class AnvilManagementListener implements Listener {

    private static final Class<?> ContainerAnvil = ReflectionUtils.getNMSClass("ContainerAnvil");
    private static final Class<?> CraftInventoryAnvil = ReflectionUtils.getCraftBukkitClass("inventory.CraftInventoryAnvil");

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        Player player = e.getPlayer();
        if (player.getGameMode() == GameMode.CREATIVE)
            return;

        Block block = e.getClickedBlock();
        if (block.getType() != Material.ANVIL)
            return;

        e.setCancelled(true);
        Tasky.sync(t -> {
            if (!player.isOnGround()) {
                player.setVelocity(new Vector(0, -0.3, 0));
            } else {
                t.cancel();
            }
        }, 1L, 5L);
        EntityPlayer eplayer = ((CraftPlayer) e.getPlayer()).getHandle();
        eplayer.openContainer(new BetterAnvilTileInventory(new ChatMessage("Repair & Name")));
    }

}
