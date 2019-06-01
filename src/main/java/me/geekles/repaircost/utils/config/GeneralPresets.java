package me.geekles.repaircost.utils.config;

import me.geekles.repaircost.utils.config.items.PlaceholderItemPreset;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Arrays;

public class GeneralPresets {

    public static int DEFAULT_MAX_COST;

    public static void init(FileConfiguration config) {
        int maxCost = config.getInt("" + ConfigurationPathLibrary.MAX_REPAIR_COST);
        DEFAULT_MAX_COST = maxCost == -1 ? Integer.MAX_VALUE : (maxCost < 0 ? 0 : maxCost);
    }
}
