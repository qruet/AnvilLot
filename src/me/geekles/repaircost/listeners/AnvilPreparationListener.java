package me.geekles.repaircost.listeners;

import me.geekles.repaircost.MaxRepairCost;
import me.geekles.repaircost.utils.ModeCheckManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AnvilPreparationListener implements Listener {

    private Map<UUID, Integer> EXPPurchaseClick = new HashMap();
    private MaxRepairCost main = null;

    public AnvilPreparationListener(MaxRepairCost main) {
        this.main = main;
    }

    /**
     * @param item Check if item is the placeholder put into the anvil to notify them that they can not afford the item that they want.
     * @return True or False
     */
    protected boolean isPlaceholder(Player player, ItemStack item) {
        ItemStack placeholder = getPlaceholder(player);
        if (item == null) {
            return false;
        }
        if (item.hasItemMeta()) {
            return (item.getEnchantments().keySet().containsAll(placeholder.getEnchantments().keySet()) &&
                    item.getEnchantments().values().containsAll(placeholder.getEnchantments().values())) &&
                    item.getType() == placeholder.getType();
        }
        return false;
    }

    /**
     * @return Returns predefined placeholder item that is placed into the anvil to notify the player that they can not afford the item that they are trying to forge.
     */
    private ItemStack getPlaceholder(Player player) {
        return main.getPlaceholder(player);
    }


    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent e) {
        Entity victim = e.getEntity();
        Entity attacker = e.getDamager();
        if ((victim instanceof Player || attacker instanceof Player) && !ModeCheckManager.isEmpty()) {
            if (victim instanceof Player || attacker instanceof Player) {
                Player p = (Player) (victim instanceof Player ? victim : attacker);
                ModeCheckManager check = ModeCheckManager.getCheckFromUUID(p.getUniqueId());
                if (check != null) {
                    check.terminate();
                }
            }
        }

    }

    private InventoryAction[] blacklisted_actions = {InventoryAction.CLONE_STACK};

    /**
     * Handles everything that is clicked within the anvil. Either forces the player to pay the necessary exp or cancel the ability to retrieve the forged item if they can not afford
     * @param e
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        Inventory clicked = e.getClickedInventory();
        if (clicked != null) {
            if (clicked.getType() == InventoryType.ANVIL) {
                if (e.getSlot() == 2) {
                    if (EXPPurchaseClick.containsKey(player.getUniqueId())) {
                        if (Arrays.asList(new InventoryAction[]{InventoryAction.DROP_ONE_SLOT, InventoryAction.DROP_ALL_SLOT}).contains(e.getAction())) { //drops item
                            EXPPurchaseClick.remove(player.getUniqueId());
                            return;
                        }
                        if (!Arrays.asList(blacklisted_actions).contains(e.getAction())) { //claims item
                            player.setLevel(player.getLevel() - EXPPurchaseClick.get(player.getUniqueId()));
                            EXPPurchaseClick.remove(player.getUniqueId());
                            return;
                        }
                        e.setCancelled(true);
                        return;
                    } else {
                        ItemStack item = e.getCurrentItem();
                        if (isPlaceholder(player, item)) {
                            e.setCancelled(true);
                        }
                    }
                }
            }
        }

    }

    //40 exp is default maximum experience level
    @EventHandler
    public void onAnvilPrepare(PrepareAnvilEvent e) {
        Player player = (Player) e.getViewers().get(0);
        if (player.getGameMode() == GameMode.SURVIVAL) {
            int cost = e.getInventory().getRepairCost();

            ItemStack first = e.getInventory().getItem(0);
            ItemStack second = e.getInventory().getItem(1);

            int max = main.getMaxRepairCost();

            for (PermissionAttachmentInfo pi : player.getEffectivePermissions()) {
                if (pi.getPermission().contains("maxrepaircost.limit.")) {
                    max = Integer.parseInt(pi.getPermission().replace("maxrepaircost.limit.", ""));
                    break;
                }
            }

            if (cost > max && max != -1) {
                e.getInventory().setRepairCost(max);
                cost = max;
            }

            if (first != null) {

                ModeCheckManager check = ModeCheckManager.addPlayerCheck(player);
                if (player.getLevel() >= cost) {
                    EXPPurchaseClick.put(player.getUniqueId(), cost);
                } else {
                    EXPPurchaseClick.remove(player.getUniqueId());
                    e.setResult(getPlaceholder(player)); //sets the result
                    e.getInventory().setItem(2, getPlaceholder(player)); //ensures the visual appearance of the placeholder
                    player.updateInventory();
                }

            }
        }
    }
}
