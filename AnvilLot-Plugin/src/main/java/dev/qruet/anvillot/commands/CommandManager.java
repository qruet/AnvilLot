package dev.qruet.anvillot.commands;

import dev.qruet.anvillot.AnvilLot;
import dev.qruet.anvillot.util.text.LanguageLibrary;
import dev.qruet.anvillot.util.text.T;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.HashMap;

/**
 * Manages core commands
 * @author qruet
 */
public class CommandManager {

    private static HashMap<String, CommandExecutor> COMMANDS = new HashMap<String, CommandExecutor>() {{
        put("help", new HelpCmd());
        put("reload", new ReloadCmd());
    }};

    public static void init() {
        PluginCommand command = JavaPlugin.getPlugin(AnvilLot.class).getCommand("anvillot");
        command.setAliases(Arrays.asList("alot", "al"));
        command.setExecutor(new AnvilLotExecutor());
    }

    private CommandManager() {
    }

    private static class AnvilLotExecutor implements CommandExecutor {

        @Override
        public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
            if (args.length == 0) {
                COMMANDS.get("help").onCommand(sender, command, alias, args);
                return true;
            }

            CommandExecutor exec = COMMANDS.get(args[0]);
            if (exec != null) {
                if(!exec.onCommand(sender, command, alias, args))
                    sender.sendMessage(T.C("" + LanguageLibrary.COMMAND_NO_PERMISSION));
                return true;
            }

            sender.sendMessage(T.C(" &4&l| &cThat command does not exist. Did you mean &o/anvillot help&c?"));
            return false;
        }
    }
}
