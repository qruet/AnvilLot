package me.geekles.repaircost.listeners;

import me.geekles.repaircost.assets.items.PlaceholderItem;
import me.geekles.repaircost.checks.ModeCheckManager;
import me.geekles.repaircost.utils.ReflectionUtils;
import me.geekles.repaircost.utils.config.GeneralPresets;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public class AnvilManagementListener implements Listener {

    private static final Class<?> ContainerAnvil = ReflectionUtils.getNMSClass("ContainerAnvil");
    private static final Class<?> CraftInventoryAnvil = ReflectionUtils.getCraftBukkitClass("inventory.CraftInventoryAnvil");

    //40 exp is default maximum experience level
    @EventHandler(priority = EventPriority.HIGH)
    public void onAnvilPrepare(PrepareAnvilEvent e) {
        if (e.getViewers().isEmpty())
            return;
        Player player = (Player) e.getViewers().get(0);
        if (player.getGameMode() == GameMode.SURVIVAL) {
            AnvilInventory inventory = e.getInventory();
            if (inventory.getMaximumRepairCost() != Integer.MAX_VALUE) {
                try {
                    Object anvil = CraftInventoryAnvil.cast(inventory);
                    Field container = CraftInventoryAnvil.getDeclaredField("container");
                    container.setAccessible(true);

                    Field modifiersField = Field.class.getDeclaredField("modifiers");
                    modifiersField.setAccessible(true);
                    modifiersField.setInt(container, container.getModifiers() & ~Modifier.FINAL);

                    Object containerAnvil = container.get(anvil);
                    Field maximumRepairCost = ContainerAnvil.getField("maximumRepairCost");
                    maximumRepairCost.set(containerAnvil, Integer.MAX_VALUE);
                } catch (NoSuchFieldException | IllegalAccessException ex) {
                    ex.printStackTrace();
                }
            }
            Optional<PermissionAttachmentInfo> info = player.getEffectivePermissions().stream().filter(p -> p.getPermission().contains("maxrepaircost.limit.")).findFirst();
            int max = GeneralPresets.DEFAULT_MAX_COST;
            if (info.isPresent())
                max = Integer.parseInt(info.get().getPermission().replace("maxrepaircost.limit.", ""));
            if (inventory.getRepairCost() > max)
                inventory.setRepairCost(max);

            ItemStack first = inventory.getItem(0);
            ItemStack second = inventory.getItem(1);
            ItemStack result = inventory.getItem(2);
            if (first != null && second != null && result == null)
                player.updateInventory();

            int cost = inventory.getRepairCost();
            if (first != null) {
                ModeCheckManager.registerNewPlayerModeCheck(player); //Begin to check player for when they close the inventory
                if (player.getLevel() < cost) {
                    PlaceholderItem item = new PlaceholderItem(player);
                    e.setResult(item); //sets the result
                    inventory.setItem(2, item); //ensures the visual appearance of the placeholder
                    player.updateInventory();
                }

            }
        }
    }

    /**
     * Ensures Placeholder Item can't be stolen in inventory
     *
     * @param e
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        Inventory clicked = e.getClickedInventory();
        if (clicked == null)
            return;
        if (clicked.getType() == InventoryType.ANVIL) {
            if (PlaceholderItem.equals(e.getCurrentItem())) {
                e.setCancelled(true);
                AnvilInventory inventory = (AnvilInventory) clicked;
                inventory.setItem(e.getSlot(), e.getCurrentItem()); //ensures the visual appearance of the placeholder
                player.updateInventory();
                if (e.getCursor() != null)
                    e.setCursor(null);
            }
        }
    }

}
