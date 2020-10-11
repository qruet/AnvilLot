package dev.qruet.anvillot;

import dev.qruet.anvillot.commands.CommandManager;
import dev.qruet.anvillot.config.ConfigDeserialization;
import dev.qruet.anvillot.listeners.AnvilInteractHandler;
import dev.qruet.anvillot.nms.VersionHandler;
import dev.qruet.anvillot.util.L;
import dev.qruet.anvillot.util.Tasky;
import dev.qruet.anvillot.util.text.LanguageLibrary;
import dev.qruet.anvillot.util.text.P;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main class responsible for the execution of the plugin
 *
 * @author Qruet
 * @version 3.0.0-Beta-SNAPSHOT
 */
public final class AnvilLot extends JavaPlugin {

    public static String PLUGIN_VERSION;

    public void onEnable() {
        PLUGIN_VERSION = getDescription().getVersion();

        getLogger().info("" + LanguageLibrary.START_HEADER);
        getLogger().info(LanguageLibrary.PREFIX + "" + LanguageLibrary.CHECK_VERSION);

        if (!VersionHandler.init()) {
            shutdown(AnvilLot.ShutdownReason.UNSUPPORTED_VERSION);
            return;
        }

        getLogger().info(P.R(LanguageLibrary.PREFIX + "" + LanguageLibrary.HOOK_VERSION));
        getLogger().info(LanguageLibrary.PREFIX + "" + LanguageLibrary.LOADING_CONFIG);

        Tasky.setPlugin(this);

        if (!loadConfig()) {
            shutdown(ShutdownReason.CONFIG);
            return;
        }

        getLogger().info(LanguageLibrary.PREFIX + "" + LanguageLibrary.SUCCESS);
        getLogger().info(LanguageLibrary.PREFIX + "" + LanguageLibrary.INITIALIZATION);

        CommandManager.init();

        L.R(new AnvilInteractHandler());

        getLogger().info(LanguageLibrary.PREFIX + "" + LanguageLibrary.SUCCESS);
        getLogger().info(LanguageLibrary.PREFIX + "" + LanguageLibrary.THANKYOU);
        getLogger().info("" + LanguageLibrary.SUCCESS_FINAL);
    }

    public enum ShutdownReason {
        NORMAL, CONFIG, UNSUPPORTED_VERSION, CRITICAL
    }

    /**
     * Handles the appropriate shutdown procedures for the plugin
     *
     * @param reason Reason/Cause for shutdown
     */
    public static void shutdown(ShutdownReason reason) {
        JavaPlugin plugin = JavaPlugin.getPlugin(AnvilLot.class);
        switch (reason) {
            case NORMAL:
                plugin.getLogger().info(LanguageLibrary.PREFIX + "" + LanguageLibrary.SHUTDOWN);
                Bukkit.getPluginManager().disablePlugin(plugin);
                break;
            case CRITICAL:
                plugin.getLogger().warning(LanguageLibrary.PREFIX + "" + LanguageLibrary.CRITICAL_ERROR);
                Bukkit.getPluginManager().disablePlugin(plugin);
                break;
            case CONFIG:
                plugin.getLogger().severe(LanguageLibrary.PREFIX + "" + LanguageLibrary.CONFIG_ERROR);
                shutdown(ShutdownReason.NORMAL);
                break;
            case UNSUPPORTED_VERSION:
                plugin.getLogger().severe(P.R(LanguageLibrary.PREFIX + "" + LanguageLibrary.UNSUPPORTED_VERSION));
                shutdown(ShutdownReason.NORMAL);
                break;
        }
    }

    public static void shutdown(String message) {
        JavaPlugin plugin = JavaPlugin.getPlugin(AnvilLot.class);
        plugin.getLogger().warning(message);
        shutdown(ShutdownReason.CRITICAL);
    }

    public void onDisable() {
        //Make sure to properly unregister any listeners belonging to this plugin
        HandlerList.unregisterAll(this);
    }

    public static void reload() {
        JavaPlugin.getPlugin(AnvilLot.class).reloadConfig();
        ConfigDeserialization.deserialize();
    }

    public boolean loadConfig() {
        boolean flag;
        try {
            flag = ConfigDeserialization.init(this);
        } catch (IllegalArgumentException e) {
            getLogger().severe(LanguageLibrary.PREFIX + "" + e.getMessage());
            return false;
        } catch (Exception e) {
            getLogger().severe(LanguageLibrary.PREFIX + "" + LanguageLibrary.CONFIG_ERROR);
            return false;
        }
        return flag;
    }
}

