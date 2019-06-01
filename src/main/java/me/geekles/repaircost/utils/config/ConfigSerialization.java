package me.geekles.repaircost.utils.config;

import me.geekles.repaircost.utils.MaterialLibrary;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigSerialization {

    public static void init(JavaPlugin pl) {
        FileConfiguration config = pl.getConfig();
        if(config.getString("" + ConfigurationPathLibrary.PLACEHOLDER_MATERIAL) == null)
            config.set("" + ConfigurationPathLibrary.PLACEHOLDER_MATERIAL, MaterialLibrary.LIGHT_GRAY_STAINED_GLASS_PANE.parseItem());
        if (!MaterialLibrary.isNewVersion()) {
            if(!config.contains("" + ConfigurationPathLibrary.PLACEHOLDER_IDENTIFIER))
                pl.getConfig().set("" + ConfigurationPathLibrary.PLACEHOLDER_IDENTIFIER, 8);
        }
        pl.saveConfig(); //update config
    }

}
