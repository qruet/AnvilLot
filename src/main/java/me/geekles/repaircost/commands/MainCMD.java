package me.geekles.repaircost.commands;

import me.geekles.repaircost.MaxRepairCost;
import me.geekles.repaircost.utils.config.GeneralPresets;
import me.geekles.repaircost.utils.text.LanguageLibrary;
import me.geekles.repaircost.utils.text.T;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MainCMD implements CommandExecutor {

    private static MaxRepairCost mrc;

    public static void init(MaxRepairCost mrc) {
        MainCMD.mrc = mrc;
        mrc.getCommand("maxrepaircost").setExecutor(new MainCMD());
    }

    private MainCMD() { }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (!player.hasPermission("maxrepaircost.admin.reload"))
                    return false;
            }
            mrc.reloadConfig();
            mrc.loadConfig();
            sender.sendMessage("" + LanguageLibrary.COMMAND_RELOAD);
            return true;
        } else if (args.length == 0) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("" + LanguageLibrary.COMMAND_INVALID_ENTITY);
                return false;
            }
            Player player = (Player) sender;
            if (!player.hasPermission("maxrepaircost.help"))
                return false;
            player.sendMessage(T.C("&8" + LanguageLibrary.START_HEADER));
            player.sendMessage(T.C("  &f&l&nDetails:"));
            player.sendMessage(T.C(" &7&oCapped Max Repair Cost:&a&o " + (GeneralPresets.DEFAULT_MAX_COST >= 999999 ? -1 : GeneralPresets.DEFAULT_MAX_COST) + "xp"));
            player.sendMessage(T.C("  &f&l&nCommands:"));
            player.sendMessage(T.C(" &7&o/&a&omaxrepaircost &8- &7Information"));
            player.sendMessage(T.C(" &7&o/&a&omaxrepaircost reload &8- &7Reload config"));
            player.sendMessage(T.C("&8&m------------------------------------------"));
        }
        return false;
    }
}
