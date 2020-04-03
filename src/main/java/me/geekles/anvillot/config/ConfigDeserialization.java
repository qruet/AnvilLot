package me.geekles.anvillot.config;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class ConfigDeserialization {

    public static boolean init(JavaPlugin pl) {
        if (!new File(pl.getDataFolder(), "config.yml").exists())
            pl.saveDefaultConfig(); // Retrieves and creates config.yml stored in plugin
        ConfigSerialization.init(pl);
        GeneralPresets.init(pl.getConfig());
        return true;
    }

}
