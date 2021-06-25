package dev.qruet.anvillot.bar.v1_17_R1;

import dev.qruet.anvillot.AnvilLot;
import dev.qruet.anvillot.config.GeneralPresets;
import dev.qruet.anvillot.util.text.T;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarFlag;
import org.bukkit.craftbukkit.v1_17_R1.boss.CraftBossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.plugin.java.JavaPlugin;


public class ExperienceBar extends CraftBossBar {

    private final Listener listener;
    private Player player;

    public ExperienceBar(Player player, BarFlag... flags) {
        super("",
                GeneralPresets.ExperienceBarPresets.COLOR,
                GeneralPresets.ExperienceBarPresets.STYLE,
                flags);
        this.player = player;

        update();

        listener = new Listener() {
            @EventHandler
            public void onExpChange(PlayerExpChangeEvent e) {
                if (!e.getPlayer().getUniqueId().equals(player.getUniqueId()))
                    return;
                update();
            }
        };

        Bukkit.getPluginManager().registerEvents(listener, JavaPlugin.getPlugin(AnvilLot.class));
    }

    public void enable() {
        super.addPlayer(player);
    }

    public void disable() {
        super.removePlayer(player);
    }

    /**
     * @param player
     * @throws UnsupportedOperationException
     */
    @Deprecated
    @Override
    public void addPlayer(Player player) {
        throw new UnsupportedOperationException();
    }

    public void update() {
        super.setProgress(player.getExp());
        super.setTitle(T.C(GeneralPresets.ExperienceBarPresets.TITLE
                .replaceAll("%level", "" +
                        (player.getLevel() == 0 ? "" : player.getLevel()))));
    }

    public void destroy() {
        disable();
        HandlerList.unregisterAll(listener);
    }

}
