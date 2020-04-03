package me.geekles.anvillot;

import me.geekles.anvillot.commands.AnvilLotCmd;
import me.geekles.anvillot.config.ConfigDeserialization;
import me.geekles.anvillot.listeners.AnvilManagementListener;
import me.geekles.anvillot.utils.L;
import me.geekles.anvillot.utils.ReflectionUtils;
import me.geekles.anvillot.utils.Tasky;
import me.geekles.anvillot.utils.text.LanguageLibrary;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;

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
        L.R(new AnvilManagementListener());

        getLogger().info(LanguageLibrary.PREFIX + "" + LanguageLibrary.SUCCESS);
        getLogger().info(LanguageLibrary.PREFIX + "" + LanguageLibrary.THANKYOU);
        getLogger().info("" + LanguageLibrary.SUCCESS_FINAL);

    }

    public enum ShutdownReason {
        NORMAL, CONFIG, UNSUPPORTED_VERSION
    }

    public static void shutdown(ShutdownReason reason) {
        JavaPlugin plugin = JavaPlugin.getPlugin(AnvilLot.class);
        switch (reason) {
            case NORMAL:
                plugin.getLogger().info(LanguageLibrary.PREFIX + "" + LanguageLibrary.SHUTDOWN);
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

    public void onDisable() {
        //Make sure to properly disable any checks\
        HandlerList.unregisterAll(this);
    }

    private boolean checkVersion() {
        try {
            Class.forName("org.bukkit.event.inventory.PrepareAnvilEvent");
            Class<?> AnvilInventory = Class.forName("org.bukkit.inventory.AnvilInventory");
            Class<?> ContainerAnvil = ReflectionUtils.getNMSClass("ContainerAnvil");
            Field maximumcost = ContainerAnvil.getField("maximumRepairCost");
            Field levelCost = ContainerAnvil.getField("levelCost");
            AnvilInventory.getDeclaredMethod("getRepairCost");
            return true;
        } catch (ClassNotFoundException | NoSuchFieldException | NoSuchMethodException e) {
            return false;
        }
    }

    //TODO setup config and retrieve values from config here
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

