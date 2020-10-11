package dev.qruet.anvillot.commands;

import dev.qruet.anvillot.AnvilLot;
import dev.qruet.anvillot.config.ConfigData;
import dev.qruet.anvillot.util.text.LanguageLibrary;
import dev.qruet.anvillot.util.text.T;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * @author qruet
 * @version 1.9_01
 */
public class ReloadCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.hasPermission("solidfix.admin.reload"))
                return false;
        }
        long start = System.currentTimeMillis();
        LinkedList<Object> o_data = new LinkedList<>();
        Arrays.stream(ConfigData.values()).forEach(d -> o_data.add(d.get()));
        AnvilLot.reload();
        ConfigData[] n_data = ConfigData.values();
        long end = System.currentTimeMillis();
        sender.sendMessage(T.C("" + LanguageLibrary.START_HEADER));
        for (int i = 0; i < n_data.length; i++) {
            if (n_data[i].getType() == ConfigData.RootPath.class)
                continue;
            sender.sendMessage(T.C(" &e* &6updated " + n_data[i].toString() + " &c&o") +
                    o_data.get(i) + " -> " + n_data[i].get());
        }
        sender.sendMessage(T.C(LanguageLibrary.COMMAND_RELOAD.toString().replaceAll("%t", "" + (end - start))));
        sender.sendMessage(T.C("" + LanguageLibrary.FOOTER));
        return true;
    }
}
