package dev.qruet.anvillot;

import dev.qruet.anvillot.commands.AnvilLotCmd;
import dev.qruet.anvillot.config.ConfigDeserialization;
import dev.qruet.anvillot.listeners.AnvilInteractHandler;
import dev.qruet.anvillot.utils.L;
import dev.qruet.anvillot.utils.ReflectionUtils;
import dev.qruet.anvillot.utils.Tasky;
import dev.qruet.anvillot.utils.text.LanguageLibrary;
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

        getLogger().warning("You are using a build in early beta, meaning critical bugs may be present." +
                " Please report any issues that you encounter.");

        if (!checkVersion()) {
            shutdown(ShutdownReason.UNSUPPORTED_VERSION);
            return;
        }

        getLogger().info(LanguageLibrary.PREFIX + "" + LanguageLibrary.HOOK_VERSION);
        getLogger().info(LanguageLibrary.PREFIX + "" + LanguageLibrary.LOADING_CONFIG);
        Tasky.setPlugin(this);

        if (!loadConfig()) {
            shutdown(ShutdownReason.CONFIG);
            return;
        }

        getLogger().info(LanguageLibrary.PREFIX + "" + LanguageLibrary.SUCCESS);
        getLogger().info(LanguageLibrary.PREFIX + "" + LanguageLibrary.INITIALIZATION);

        AnvilLotCmd.init();
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
                plugin.getLogger().severe(LanguageLibrary.PREFIX + "" + LanguageLibrary.UNSUPPORTED_VERSION);
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

    private boolean checkVersion() {
        try {
            Class.forName("org.bukkit.event.inventory.PrepareAnvilEvent");
            Class<?> AnvilInventory = Class.forName("org.bukkit.inventory.AnvilInventory");
            Class<?> ContainerAnvil = ReflectionUtils.getNMSClass("ContainerAnvil");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
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

