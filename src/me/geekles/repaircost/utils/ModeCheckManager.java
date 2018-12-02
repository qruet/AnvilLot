package me.geekles.repaircost.utils;

import me.geekles.repaircost.MaxRepairCost;
import me.geekles.repaircost.utils.v1_13.GameModeCheck_R2;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Manages the functionality and returns the correct CheckManager version corresponding to the server version the plugin is being run on
 */
public abstract class ModeCheckManager {

    protected static Map<UUID, ModeCheckManager> GamemodeChecks = new HashMap();

    private static Map<Class<? extends ModeCheckManager>, String[]> VersionHandler = new HashMap<>();

    private static Class<? extends ModeCheckManager> check = null;

    private static MaxRepairCost main;

    private static loop updater = null;

    static {
        VersionHandler.put(me.geekles.repaircost.utils.v1_11.GameModeCheck_R1.class, new String[]{"1.11", "1.11.1", "1.11.2"});
        VersionHandler.put(me.geekles.repaircost.utils.v1_12.GameModeCheck_R1.class, new String[]{"1.12", "1.12.1", "1.12.2"});
        VersionHandler.put(me.geekles.repaircost.utils.v1_13.GameModeCheck_R1.class, new String[]{"1.13"});
        VersionHandler.put(me.geekles.repaircost.utils.v1_13.GameModeCheck_R2.class, new String[]{"1.13.1", "1.13.2"});
    }

    public static boolean isEmpty() {
        return GamemodeChecks.isEmpty();
    }

    public static ModeCheckManager getCheckFromUUID(UUID id) {
        if (GamemodeChecks.containsKey(id)) {
            return GamemodeChecks.get(id);
        }
        return null;
    }

    public static boolean containsPlayer(Player player){
        return GamemodeChecks.containsKey(player.getUniqueId());
    }

    public static ArrayList<ModeCheckManager> retrieveChecks() {
        if (!GamemodeChecks.isEmpty())
            return new ArrayList(Arrays.asList(GamemodeChecks.values().toArray(new ModeCheckManager[GamemodeChecks.values().size()])));
        return null;
    }

    public static void setup(MaxRepairCost main) {
        ModeCheckManager.main = main;
        for (Class<? extends ModeCheckManager> checker : VersionHandler.keySet()) {
            String[] supported_versions = VersionHandler.get(checker);
            if (Arrays.asList(supported_versions).contains(main.getVersion())) {
                check = checker;
            }
        }
        updater = new loop();
        updater.runTaskTimerAsynchronously(main, 0L, 1L);
    }

    public static ModeCheckManager addPlayerCheck(Player player) {
        ModeCheckManager c = null;
        try {
            c = check.getConstructor(Player.class).newInstance(player);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            //TODO Handle exception
            e.printStackTrace();
        }
        GamemodeChecks.put(player.getUniqueId(), c);
        return c;
    }

    public static void removePlayerCheck(Player player) {
        getCheckFromUUID(player.getUniqueId()).revertCreativeClientEffect(player);
        GamemodeChecks.remove(player.getUniqueId());
    }

    public abstract void revertCreativeClientEffect(Player player);

    public abstract void update();

    public abstract void terminate();

    public abstract void terminate(boolean close);

    private static class loop extends BukkitRunnable {
        public void run() {
            ModeCheckManager.run();
        }
    }

    private static void run() {
        if(!isEmpty()) {
            for (ModeCheckManager check : retrieveChecks()) {
                check.update();
            }
        }
    }

    public static void cancel() {
        for (ModeCheckManager check : GamemodeChecks.values()) {
            check.terminate();
        }
    }
}
