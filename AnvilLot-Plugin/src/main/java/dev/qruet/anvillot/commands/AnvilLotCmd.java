package dev.qruet.anvillot.commands;

import dev.qruet.anvillot.AnvilLot;
import dev.qruet.anvillot.config.GeneralPresets;
import dev.qruet.anvillot.utils.text.LanguageLibrary;
import dev.qruet.anvillot.utils.text.T;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class AnvilLotCmd implements CommandExecutor {

    public static void init() {
        PluginCommand command = JavaPlugin.getPlugin(AnvilLot.class).getCommand("anvillot");
        command.setAliases(Arrays.asList("alot", "al"));
        command.setExecutor(new AnvilLotCmd());
    }

    private AnvilLotCmd() {
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (!player.hasPermission("anvillot.admin.reload"))
                    return false;
            }
            AnvilLot pl = JavaPlugin.getPlugin(AnvilLot.class);
            pl.reloadConfig();
            pl.loadConfig();
            sender.sendMessage("" + LanguageLibrary.COMMAND_RELOAD);
            return true;
        } else if (args.length == 0) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("" + LanguageLibrary.COMMAND_INVALID_ENTITY);
                return false;
            }
            Player player = (Player) sender;
            if (!player.hasPermission("anvillot.help"))
                return false;
            player.sendMessage(T.C("" + LanguageLibrary.START_HEADER));
            player.sendMessage(T.C("  &f&lDetails:"));
            player.sendMessage(T.C("    &7&oCapped Repair Cost: &a&o" + GeneralPresets.DEFAULT_MAX_COST + "xp"));
            player.sendMessage(T.C("  &f&lCommands:"));
            player.sendMessage(T.C("    &7&o/&a&oanvillot &8- &7Information"));
            player.sendMessage(T.C("    &7&o/&a&oanvillot reload &8- &7Reload config"));
            player.sendMessage(T.C("&8&m-------------------------------------------------"));
        }
        return false;
    }
}
