package me.geekles.repaircost;

import me.geekles.repaircost.commands.MainCMD;
import me.geekles.repaircost.listeners.AnvilPreparationListener;
import me.geekles.repaircost.checks.ModeCheckManager;
import me.geekles.repaircost.utils.items.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;


public final class MaxRepairCost extends JavaPlugin {

    private final String[] supported_versions = {"1.11", "1.11.1", "1.11.2", "1.12", "1.12.1", "1.12.2", "1.13", "1.13.1", "1.13.2"}; //Versions that this plugin can support

    private String server_version; //initialized on start

    private int MAX_REPAIR_COST = 0; //initialized from config - Maximum experience cost for anvils

    private ItemStack placeholder = null;

    private boolean criticalshutdown = false;

    public void onEnable() {
        getLogger().info("[========= MaxRepairCost " + getDescription().getVersion() + " =========]");
        getLogger().info("[●o●] ~(Checking server version...)");
        if (!checkVersion()) {
            getLogger().severe("[●▂◉] ~(The version your server is on is not supported by this plugin! Please check that you are on one of the following: " + Arrays.asList(supported_versions) + ")");
            CriticalShutdownManuevers();
            return;
        }
        getLogger().info("[●ω●] ~(Hooking to version, " + Bukkit.getServer().getVersion() + ")");
        getLogger().info("[●o●] ~(Loading config data...)");
        loadConfig();
        if(!criticalshutdown) {
            try {
                getLogger().info("[●ω●] ~(Success!)");
                getLogger().info("[●▂●] ~(Setting up a few additional things...)");
                ModeCheckManager.setup(this);
                this.getCommand("maxrepaircost").setExecutor(new MainCMD(this));
                registerListeners(new AnvilPreparationListener(this));
                getLogger().info("[●ω●] ~(Success!)");
                getLogger().info("[●‿●] ~(Thank you for installing me! If you're experiencing any issues please report it to my creator (see config for details))");
                getLogger().info("[========= Successfully Loaded =========]");
            } catch (NoClassDefFoundError e) {
                getLogger().severe("[●▂◉] ~(Owch! A critical error just occurred, please report the following error report to my creator as soon as possibe!)");
                e.printStackTrace();
                CriticalShutdownManuevers();
                return;
            }
        }
    }

    private void CriticalShutdownManuevers() {
        getLogger().severe("[●⌒●] ~(Shutting dow-)");
        this.criticalshutdown = true;
        this.getPluginLoader().disablePlugin(this);
        return;
    }

    public void onDisable() {
        if(!criticalshutdown)
            getLogger().info("[●‿●] ~(I hope I performed satisfactory! Be sure to leave a review or report any issues you're having with me to my creator, geekles!)");
        //Make sure to properly disable any checks
        ModeCheckManager.cancel();
    }

    private String getServerVersion() {
        String raw_version = Bukkit.getServer().getVersion();
        return raw_version.substring(raw_version.indexOf("MC: ") + 4).replace(")", "");
    }

    private boolean checkVersion() {
        for (int v = 0; v < supported_versions.length; v++) {
            if (getServerVersion().equals(supported_versions[v])) {
                this.server_version = supported_versions[v];
                return true;
            }
        }
        return false;
    }

    //register listeners (as method header suggests)
    private void registerListeners(Listener... listeners) {
        for (Listener listener : listeners) {
            Bukkit.getPluginManager().registerEvents(listener, this);
        }
    }

    //setup config and retrieve values from config here
    @Deprecated
    public void loadConfig() {
        if (!new File(this.getDataFolder(), "config.yml").exists()) {
            this.getLogger().warning("[●▂●] ~(I couldn't find the config so I'm going to create a new one.)");
            this.saveDefaultConfig(); // Retrieves and creates config.yml stored in plugin

            String value = "";
            if (server_version.contains("1.13")) {
                value = "LIGHT_GRAY_STAINED_GLASS_PANE";
                getConfig().set("Material", value);
            } else {
                value = "STAINED_GLASS_PANE:8";
                getConfig().set("Material", value);
            }
            saveConfig(); //update config
        }
        //Initialize private variables
        this.MAX_REPAIR_COST = getConfig().getInt("Default Max Repair Cost");

        //Setup placeholder item
        try {
            Placeholder.setup(getVersion(), getConfig());
        } catch (Exception e) {
            getLogger().severe("[●▂◉] ~(Oops, something went wrong! Please check to be sure all the values are correctly set in the config...)");
            CriticalShutdownManuevers();
        }
    }

    public int getMaxRepairCost() {
        return this.MAX_REPAIR_COST;
    }

    public String getVersion() {
        return this.server_version;
    }

}

