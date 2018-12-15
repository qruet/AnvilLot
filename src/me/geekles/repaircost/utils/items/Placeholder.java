package me.geekles.repaircost.utils.items;

import me.geekles.repaircost.utils.text.T;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Placeholder {

    private static ItemStack placeholder;

    @Deprecated
    public static void setup(String version, FileConfiguration config) {
        if (version.contains("1.13")) {
            placeholder = new ItemStack(Material.valueOf(config.getString("Material")), 1);
        } else {
            String[] item = config.getString("Material").split(":");
            placeholder = new ItemStack(Material.valueOf(item[0].toUpperCase()), 1, Byte.valueOf(item[1]).byteValue());
        }
        if (config.contains("ItemMeta")) {
            ItemMeta meta = placeholder.getItemMeta();
            meta.setDisplayName(config.getString("ItemMeta.Displayname"));
            meta.setLore(config.getStringList("ItemMeta.Lore"));
            placeholder.setItemMeta(meta);
            if (config.getBoolean("ItemMeta.Enchanted")) {
                placeholder.addUnsafeEnchantment(Enchantment.DURABILITY, 0);
            }
        }
    }

    /**
     * @return Returns predefined placeholder item that is placed into the anvil to notify the player that they can not afford the item that they are trying to forge.
     */

    public static ItemStack getItem(Player player) {
        ItemStack placeholder = Placeholder.placeholder.clone();
        if (placeholder.hasItemMeta()) {
            ItemMeta meta = placeholder.getItemMeta();
            meta.setDisplayName(T.C(player, meta.getDisplayName()));
            meta.setLore(T.LC(player, meta.getLore()));
            placeholder.setItemMeta(meta);
        }
        return placeholder;
    }

    /**
     * @param item Check if item is the placeholder put into the anvil to notify them that they can not afford the item that they want.
     * @return True or False
     */
    public static boolean equals(Player player, ItemStack item) {
        ItemStack placeholder = getItem(player);
        boolean equal = false;
        if (item == null) {
            return false;
        }

        equal = item.getType() == placeholder.getType();
        equal = item.hasItemMeta() == placeholder.hasItemMeta();
        if (item.hasItemMeta() && placeholder.hasItemMeta()) {
            equal = (item.getEnchantments().keySet().containsAll(placeholder.getEnchantments().keySet()) &&
                    item.getEnchantments().values().containsAll(placeholder.getEnchantments().values())) &&
                    (placeholder.getItemMeta().getLore().equals(item.getItemMeta().getLore())) &&
                    (placeholder.getItemMeta().getDisplayName().equals(item.getItemMeta().getDisplayName()));
        }
        return equal;
    }

}
