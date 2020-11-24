package dev.qruet.anvillot.commands;

import dev.qruet.anvillot.config.GeneralPresets;
import dev.qruet.anvillot.util.text.LanguageLibrary;
import dev.qruet.anvillot.util.text.T;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author qruet
 * @version 1.9_01
 */
public class HelpCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            player.sendMessage(T.C("" + LanguageLibrary.START_HEADER));
            player.sendMessage(T.C(T.center("&e&ocreated by qruet")));
            player.sendMessage(T.C(" &6resource: &e&o" + LanguageLibrary.RESOURCE_LINK));
            player.sendMessage(T.C(" &6support: &e&o" + LanguageLibrary.DISCORD_LINK));
            player.sendMessage(T.C(" &6donate: &e&o" + LanguageLibrary.DONATE_LINK));
            player.sendMessage(T.C("  &8&m-----&r"));
            player.sendMessage(T.C(" &7&oI work hard to ensure I provide quality support"));
            player.sendMessage(T.C(" &7&ofree of charge for everyone. Consider donating?"));
            player.sendMessage(T.C("  &8&m-----&r"));
            player.sendMessage(T.C("  &f&lDetails:"));
            player.sendMessage("");

            player.sendMessage(T.C("    &fCapped Repair Cost: "));

            player.sendMessage(T.C(T.center("&a&o" + GeneralPresets.DEFAULT_MAX_COST + "xp")));
            player.sendMessage(T.C("      &e* &7Defined maximum cost for repairs. Repairs will never be"));
            player.sendMessage(T.C("        &7more than " + GeneralPresets.DEFAULT_MAX_COST + "xp."));
            player.sendMessage("");

            player.sendMessage(T.C("    &fHard Limit Enabled: "));

            player.sendMessage(T.C(T.center("&a&o" + GeneralPresets.HARD_LIMIT_BAR_ENABLED)));
            player.sendMessage(T.C("      &e* &7Cap limit behaviour similar to vanilla's"));
            player.sendMessage("");

            player.sendMessage(T.C("    &fRepair Cost Calculation Equation:"));

            player.sendMessage(T.C(T.center("&a&o" + GeneralPresets.REPAIR_COST_EQUATION)));
            player.sendMessage(T.C("      &e* &7Equation responsible for calculating the total repair"));
            player.sendMessage(T.C("        &7cost."));

            player.sendMessage(T.C("      &e* &7first_item, second_item are variables that represent"));
            player.sendMessage(T.C("        &7the first and second items' repair costs in the anvil."));
            player.sendMessage(T.C("      &e* &7rename_fee is the fee added to the total repair cost if"));
            player.sendMessage(T.C("        &7the name of the item is changed."));
            player.sendMessage("");

            player.sendMessage(T.C("    &fRepair Cost Progression Equation:"));

            player.sendMessage(T.C(T.center("&a&o" + GeneralPresets.REPAIR_PROGRESSION_EQUATION)));
            player.sendMessage(T.C("      &e* &7The cost of the final item after a repair increases at"));
            player.sendMessage(T.C("        &7the rate provided."));
            player.sendMessage("");

            player.sendMessage(T.C("  &f&lCommands:"));
            player.sendMessage("");
            player.sendMessage(T.C("    &7&o/&a&oanvillot help &8- &7Information"));
            player.sendMessage(T.C("    &7&o/&a&oanvillot reload &8- &7Reload config"));
            player.sendMessage(T.C("" + LanguageLibrary.FOOTER));
            return true;
        }
        sender.sendMessage(T.C("" + LanguageLibrary.COMMAND_INVALID_ENTITY));
        return true;
    }

}
