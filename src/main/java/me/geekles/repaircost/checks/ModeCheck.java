package me.geekles.repaircost.checks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryView;
import java.util.UUID;


public class ModeCheck implements ModeChecker {
    private final UUID uuid; //player that is being checked

    public ModeCheck(Player player) {
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
        Player player = getPlayer();
        if(player != null) {
            if (player.getOpenInventory() != null)
                player.closeInventory(); //a good idea
        }
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
        if(oi != null && oi.getTopInventory() != null && oi.getTopInventory().getType() != InventoryType.ANVIL)
            terminate();
    }
}
