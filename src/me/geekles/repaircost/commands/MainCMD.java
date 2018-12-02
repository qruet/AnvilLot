package me.geekles.repaircost.commands;

import me.geekles.repaircost.MaxRepairCost;
import me.geekles.repaircost.utils.T;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MainCMD implements CommandExecutor {

    private MaxRepairCost main;

    public MainCMD(MaxRepairCost main){
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
        if(args.length == 1){
            if(args[0].equalsIgnoreCase("reload")){
                main.reloadConfig();
                main.loadConfig();
                sender.sendMessage(T.C("&8[&e&l●‿●&8] &a&oSuccessfully reloaded config!"));
                return true;
            }
        }
        else {
            if(sender instanceof Player){
                Player player = (Player) sender;
                player.sendMessage(T.C("&8&m------------------------------------------"));
                player.sendMessage(T.C("  &f&l&nDetails:"));
                player.sendMessage(T.C(" &7&oVersion:&a&o " + main.getDescription().getVersion()));
                player.sendMessage(T.C(" &7&oCapped Repair Cost:&a&o " + main.getMaxRepairCost() + "xp"));
                player.sendMessage(T.C("  &f&l&nCommands:"));
                player.sendMessage(T.C(" &7&o/&a&omaxrepaircost reload &8- &7Reload config"));
                player.sendMessage(T.C("&8&m------------------------------------------"));
            }
            else{
                sender.sendMessage(T.C("[●⌒●] You must be a player to run this command!"));
            }
            return true;
        }

        return false;
    }
}
