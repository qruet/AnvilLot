package dev.qruet.anvillot.util.text;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Text utility class for cleaner text formatting solutions
 */
public class T {

    /**
     *
     * @param message General color coded message ('&')
     * @return Returns colorized version of message
     */
    public static String C(String message){
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String C(Player player, String message){
        return C(P(player, message));
    }

    public static String P(Player player, String message){
        message = message.replace("%player%", player.getName());
        message = message.replace("%exp%", "" + player.getLevel());
        return message;
    }

    public static List<String> LC(List<String> list) {
        List<String> cList = new ArrayList<>();
        for(String msg : list){
            cList.add(T.C(msg));
        }
        return cList;
    }

    public static List<String> LC(Player player, List<String> list) {
        List<String> cList = new ArrayList<>();
        for(String msg : list){
            cList.add(T.C(player, msg));
        }
        return cList;
    }

    public static String center(String message) {
        return MessageManager.getCenteredMessage(message);
    }

}
