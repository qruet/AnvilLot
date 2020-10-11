package dev.qruet.anvillot.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class ConfigDeserialization {

    private static JavaPlugin plugin;

    public static boolean init(JavaPlugin pl) {
        plugin = pl;

        if (!new File(pl.getDataFolder(), "config.yml").exists())
            pl.saveDefaultConfig(); // Retrieves and creates config.yml stored in plugin

        deserialize();
        return true;
    }

    public static void deserialize() {
        if (plugin == null)
            throw new UnsupportedOperationException(ConfigDeserialization.class.getName() + " not yet initialized.");

        FileConfiguration config = plugin.getConfig();
        for (ConfigData dataField : ConfigData.values()) {
            if(dataField.getType() == ConfigData.RootPath.class)
                continue;
            if (dataField.getType() == Integer.class) {
                dataField.set(config.getInt("" + dataField));
            } else if (dataField.getType() == Boolean.class) {
                dataField.set(config.getBoolean("" + dataField));
            } else if (dataField.getType() == String.class) {
                dataField.set(config.getString("" + dataField));
            } else if (dataField.getType() == Float.class) {
                dataField.set((float) config.getDouble("" + dataField));
            } else {
                dataField.set(config.get("" + dataField));
            }
        }
        GeneralPresets.init();
    }
}
