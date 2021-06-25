package dev.qruet.anvillot.listeners;

import dev.qruet.anvillot.AnvilLot;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedList;

/**
 *
 */
public class AnvilInteractHandler implements Listener {

    private static final LinkedList<Material> MATERIAL_NODES = new LinkedList<Material>() {{
        add(Material.ANVIL);
        add(Material.CHIPPED_ANVIL);
        add(Material.DAMAGED_ANVIL);
    }};

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        Player player = e.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType().isBlock() && player.isSneaking()) //check if player is trying to place block around anvil
            return;

        if (player.getGameMode() == GameMode.CREATIVE) //no need to handle creative players (no anvil cap exists)
            return;

        Block block = e.getClickedBlock();
        if (!MATERIAL_NODES.contains(block.getType()))
            return;

        e.setCancelled(true);

        AnvilLot.getHandler().openContainer(player);
    }

}
