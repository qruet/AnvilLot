package me.geekles.repaircost.utils;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class ListenerUtils {

    private static JavaPlugin pl;

    public static void init(JavaPlugin pl) {
        ListenerUtils.pl = pl;
    }

    public static Listener registerListener(Listener listener){
        Bukkit.getPluginManager().registerEvents(listener, pl);
        return listener;
    }

}
