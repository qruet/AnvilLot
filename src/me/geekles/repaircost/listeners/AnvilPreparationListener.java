package me.geekles.repaircost.listeners;

import me.geekles.repaircost.MaxRepairCost;
import me.geekles.repaircost.checks.ModeCheckManager;
import me.geekles.repaircost.utils.items.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.*;

public class AnvilPreparationListener implements Listener {

    private static MaxRepairCost main;

    public AnvilPreparationListener(MaxRepairCost main) {
        this.main = main;
    }

    //40 exp is default maximum experience level
    @EventHandler
    public void onAnvilPrepare(PrepareAnvilEvent e) {
        if (!e.getViewers().isEmpty()) { //TODO Figure out why viewers may be empty?
            Player player = (Player) e.getViewers().get(0);
            if (player.getGameMode() == GameMode.SURVIVAL) {
                int cost = e.getInventory().getRepairCost();
                int max = main.getMaxRepairCost();

                ItemStack first = e.getInventory().getItem(0);

                for (PermissionAttachmentInfo pi : player.getEffectivePermissions()) {
                    if (pi.getPermission().contains("maxrepaircost.limit.")) {
                        max = Integer.parseInt(pi.getPermission().replace("maxrepaircost.limit.", ""));
                        break;
                    }
                }

                if (cost > max && max != -1 /*If set to -1 the maximum cost should not be calculated (infinite)*/) {
                    e.getInventory().setRepairCost(max);
                    cost = max;
                }

                if (first != null) {
                    ModeCheckManager.addPlayerCheck(player); //Begin to check player for when they close the inventory
                    if (player.getLevel() >= cost) {
                        new PriceCheckout(player.getUniqueId(), cost); //Tells the plugin that when the player selects their item, they should pay a certain price for doing so.
                    } else {
                        e.setResult(Placeholder.getItem(player)); //sets the result
                        e.getInventory().setItem(2, Placeholder.getItem(player)); //ensures the visual appearance of the placeholder
                        player.updateInventory();
                    }

                }
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){
        Player player = (Player) e.getWhoClicked();
        Inventory clicked = e.getClickedInventory();
        if (clicked == null) {
            return;
        }
        if (clicked.getType() == InventoryType.ANVIL) {
            if(Placeholder.equals(player, e.getCurrentItem())){
                e.setCancelled(true);
            }
        }
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

    private static class PriceCheckout implements Listener {

        private UUID id;
        private int cost;

        private static Map<UUID, PriceCheckout> priceCheckouts = new HashMap<>();

        public PriceCheckout(UUID id, int cost) {
            this.id = id;
            this.cost = cost;
            register(this);
        }

        private final List<InventoryAction> BLACKLISTED_ACTIONS = Arrays.asList(new InventoryAction[]{InventoryAction.CLONE_STACK});

        public static void register(PriceCheckout pc){
            if(priceCheckouts.containsKey(pc.id)){
                priceCheckouts.get(pc.id).cancel();
            }
            priceCheckouts.put(pc.id, pc);
            Bukkit.getPluginManager().registerEvents(pc, AnvilPreparationListener.main);
        }

        public static void cancel(UUID id) {
            if (priceCheckouts.containsKey(id)) {
                priceCheckouts.get(id).cancel();
            }
        }

        private void cancel() {
            HandlerList.unregisterAll(this);
            priceCheckouts.remove(id);
        }

        public void purchaseItem() {
            Player player = Bukkit.getPlayer(id);
            if(player.getLevel() >= cost){
                player.setLevel(player.getLevel() - cost);
            }
            cancel();
        }

        @EventHandler
        public void onInventoryClick(InventoryClickEvent e) {
            Player player = (Player) e.getWhoClicked();
            Inventory clicked = e.getClickedInventory();
            if (clicked == null) {
                return;
            }
            if (clicked.getType() == InventoryType.ANVIL) {
                if (player.getUniqueId().equals(id)) {
                    if (e.getSlot() == 2 /*The result slot*/) {
                        if (BLACKLISTED_ACTIONS.contains(e.getAction()) || Placeholder.equals(player, e.getCurrentItem())) {
                            e.setCancelled(true);
                        }
                        else {
                            purchaseItem();
                        }
                    }
                }
            }
        }

    }

}
