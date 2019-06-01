package me.geekles.repaircost.utils.config.items;

import me.geekles.repaircost.utils.MaterialLibrary;
import me.geekles.repaircost.utils.config.ConfigurationPathLibrary;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class PlaceholderItemPreset {
    public static final String KEY = "PLACEHOLDER_ITEM_PRESET";
    public static final Material DEFAULT_MATERIAL = MaterialLibrary.GRAY_STAINED_GLASS_PANE.parseMaterial();

    private static Material material = DEFAULT_MATERIAL;
    private static byte identifier = 0;
    private static ItemMeta meta = null;

    public static void init(FileConfiguration config) {
        String material = config.getString("" + ConfigurationPathLibrary.PLACEHOLDER_MATERIAL);
        int id = config.getInt("" + ConfigurationPathLibrary.PLACEHOLDER_IDENTIFIER);
        String displayname = config.getString("" + ConfigurationPathLibrary.PLACEHOLDER_DISPLAYNAME);
        List<String> lore = config.getStringList("" + ConfigurationPathLibrary.PLACEHOLDER_LORE);
        boolean enchanted = config.getBoolean("" + ConfigurationPathLibrary.PLACEHOLDER_ENCHANTED);

        if (material != null && !material.isEmpty())
            PlaceholderItemPreset.material = MaterialLibrary.getMaterial(material);
        if (!MaterialLibrary.isNewVersion() && id >= 0)
            PlaceholderItemPreset.identifier = (byte) id;
        ItemMeta meta = new ItemStack(PlaceholderItemPreset.material).getItemMeta();
        if (displayname != null)
            meta.setDisplayName(displayname);
        if (lore != null && !lore.isEmpty())
            meta.setLore(lore);
        if (enchanted) {
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            meta.addEnchant(Enchantment.DURABILITY, -1, true);
        }
        PlaceholderItemPreset.meta = meta;
    }

    public static Material getMaterial() {
        return material;
    }

    public static ItemMeta getItemMeta() {
        return meta;
    }

    public static byte getIdentifier() {
        return identifier;
    }
}
