package me.geekles.repaircost;

import me.geekles.repaircost.commands.MainCMD;
import me.geekles.repaircost.listeners.AnvilManagementListener;
import me.geekles.repaircost.checks.ModeCheckManager;
import me.geekles.repaircost.utils.ListenerUtils;
import me.geekles.repaircost.utils.ReflectionUtils;
import me.geekles.repaircost.utils.config.ConfigDeserialization;
import me.geekles.repaircost.utils.text.LanguageLibrary;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public final class MaxRepairCost extends JavaPlugin {

    public static String PLUGIN_VERSION;

    public void onEnable() {
        PLUGIN_VERSION = getDescription().getVersion();
        getLogger().info("" + LanguageLibrary.START_HEADER);
        getLogger().info(LanguageLibrary.PREFIX + "" + LanguageLibrary.CHECK_VERSION);
        if (checkVersion()) {
            getLogger().info(LanguageLibrary.PREFIX + "" + LanguageLibrary.HOOK_VERSION);
            getLogger().info(LanguageLibrary.PREFIX + "" + LanguageLibrary.LOADING_CONFIG);
            if (loadConfig()) {
                getLogger().info(LanguageLibrary.PREFIX + "" + LanguageLibrary.SUCCESS);
                getLogger().info(LanguageLibrary.PREFIX + "" + LanguageLibrary.INITIALIZATION);
                ModeCheckManager.init(this);
                MainCMD.init(this);
                ListenerUtils.init(this);
                ListenerUtils.registerListener(new AnvilManagementListener());
                getLogger().info(LanguageLibrary.PREFIX + "" + LanguageLibrary.SUCCESS);
                getLogger().info(LanguageLibrary.PREFIX + "" + LanguageLibrary.THANKYOU);
                getLogger().info("" + LanguageLibrary.SUCCESS_FINAL);
            }
            return;
        }
        getLogger().severe(LanguageLibrary.PREFIX + "" + LanguageLibrary.UNSUPPORTED_VERSION);
        CriticalShutdownManuevers();
        return;
    }

    private void CriticalShutdownManuevers() {
        getLogger().severe(LanguageLibrary.PREFIX + "" + LanguageLibrary.SHUTDOWN);
        this.getPluginLoader().disablePlugin(this);
    }

    public void onDisable() {
        //Make sure to properly disable any checks
        ModeCheckManager.cleanup();
    }

    private boolean checkVersion() {
        try {
            Class.forName("org.bukkit.event.inventory.PrepareAnvilEvent");
            Class<?> AnvilInventory = Class.forName("org.bukkit.inventory.AnvilInventory");
            Class<?> ContainerAnvil = ReflectionUtils.getNMSClass("ContainerAnvil");
            Field maximumcost = ContainerAnvil.getField("maximumRepairCost");
            AnvilInventory.getDeclaredMethod("getRepairCost");
            return true;
        } catch (ClassNotFoundException | NoSuchFieldException | NoSuchMethodException e) {
            return false;
        }
    }

    //setup config and retrieve values from config here
    public boolean loadConfig() {
        boolean flag;
        try {
            flag = ConfigDeserialization.init(this);
        } catch (Exception e) {
            getLogger().severe(LanguageLibrary.PREFIX + "" + LanguageLibrary.CONFIG_ERROR);
            return false;
        }
        return flag;
    }

}

