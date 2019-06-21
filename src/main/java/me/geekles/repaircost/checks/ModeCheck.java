package me.geekles.repaircost.checks;

import me.geekles.repaircost.MaxRepairCost;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryView;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;


public class ModeCheck implements ModeChecker {
    private final JavaPlugin pl;
    private final UUID uuid; //player that is being checked

    public ModeCheck(JavaPlugin pl, Player player) {
        this.pl = pl;
        this.uuid = player.getUniqueId();
        ModeCheckManager.changeClientGamemode(ModeCheckManager.Mode.CREATIVE, player);
    }

    public UUID getUUID() {
        return uuid;
    }

    private Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    @Override
    public void terminate() {
        new BukkitRunnable() {
            public void run() {
                Player player = getPlayer();
                if (player != null) {
                    if (player.getOpenInventory() != null)
                        player.closeInventory(); //a good idea
                }
            }
        }.runTask(pl);
        //remove check
        ModeCheckManager.unregisterPlayerModeCheck(uuid);

    }

    /**
     * Method that checks to see if the player still has the anvil open
     */
    @Override
    public void update() {
        if (getPlayer() == null) {
            terminate();
            return;
        }
        InventoryView oi = getPlayer().getOpenInventory();
        if (oi != null && oi.getTopInventory() != null && oi.getTopInventory().getType() != InventoryType.ANVIL)
            terminate();
    }
}
