package me.geekles.repaircost.checks;

import me.geekles.repaircost.utils.ReflectionUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Manages the functionality and returns the correct CheckManager version corresponding to the server version the plugin is being run on
 */
public class ModeCheckManager {

    private static JavaPlugin pl;

    protected static List<ModeChecker> RegisteredModeChecks = new ArrayList();
    private static CheckRunnable updater;

    private static final Class<?> EntityPlayer = ReflectionUtils.getNMSClass("EntityPlayer");
    private static final Class<?> CraftPlayer = ReflectionUtils.getCraftBukkitClass("entity.CraftPlayer");
    private static final Class<?> PacketPlayOutGameStateChange = ReflectionUtils.getNMSClass("PacketPlayOutGameStateChange");
    private static final Class<?> PlayerConnection = ReflectionUtils.getNMSClass("PlayerConnection");
    private static final Class<?> Packet = ReflectionUtils.getNMSClass("Packet");

    public static void init(JavaPlugin pl) {
        ModeCheckManager.pl = pl;
        updater = new CheckRunnable();
        updater.runTaskTimerAsynchronously(pl, 0L, 1L);
    }

    public static ModeChecker registerNewPlayerModeCheck(Player player) {
        ModeChecker checker = getPlayerModeCheck(player.getUniqueId());
        if(checker != null)
            return checker;
        ModeCheck check = new ModeCheck(pl, player);
        RegisteredModeChecks.add(check);
        return check;
    }

    public enum Mode {
        CREATIVE, SURVIVAL
    }

    public static void changeClientGamemode(Mode mode, Player player){
        if(mode == Mode.SURVIVAL && !(RegisteredModeChecks.stream().filter(mc -> mc.getUUID().equals(player.getUniqueId())).findFirst().isPresent())){
            return;
        }
        try {
            Method getHandle = CraftPlayer.getDeclaredMethod("getHandle");
            Object entityPlayer = getHandle.invoke(CraftPlayer.cast(player));
            Object packet = PacketPlayOutGameStateChange.getConstructor(int.class, float.class).newInstance(3, (mode == Mode.CREATIVE ? 1 : 0));
            Field playerConnection = EntityPlayer.getDeclaredField("playerConnection");
            Object connection = playerConnection.get(entityPlayer);
            Method sendPacket = PlayerConnection.getDeclaredMethod("sendPacket", Packet);
            sendPacket.invoke(connection, packet); //sends packet to player's client to make their client think they are in client
        } catch (NoSuchMethodException | NoSuchFieldException | InstantiationException |  IllegalAccessException | InvocationTargetException e){
            e.printStackTrace();
        }
    }

    public static void unregisterPlayerModeCheck(UUID id){
        Player player = Bukkit.getPlayer(id);
        if(player != null)
            changeClientGamemode(Mode.SURVIVAL, player);
        RegisteredModeChecks.remove(getPlayerModeCheck(id));
    }

    public static void unregisterPlayerModeCheck(Player player) {
        changeClientGamemode(Mode.SURVIVAL, player);
        RegisteredModeChecks.remove(getPlayerModeCheck(player.getUniqueId()));
    }

    public static ModeChecker getPlayerModeCheck(UUID id){
        try {
            return RegisteredModeChecks.stream().filter(mc -> mc.getUUID().equals(id)).findFirst().get();
        } catch(NoSuchElementException e){
            return null;
        }
    }

    private static class CheckRunnable extends BukkitRunnable {
        public void run() {
            new ArrayList<>(RegisteredModeChecks).stream().forEach(check -> check.update());
        }
    }

    public static void cleanup() {
        for (ModeChecker check : RegisteredModeChecks)
            check.terminate();
        RegisteredModeChecks = null;
        if(updater != null)
            updater.cancel();
        updater = null;

    }

}
