package dev.qruet.anvillot.utils;

import dev.qruet.anvillot.AnvilLot;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Listener registration utility class
 *
 * @author Qruet
 */
public class L {
    public static Listener R(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, JavaPlugin.getPlugin(AnvilLot.class));
        return listener;
    }

}
