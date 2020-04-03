package me.geekles.anvillot.bar;

import me.geekles.anvillot.config.GeneralPresets;
import me.geekles.anvillot.nms.BetterContainerAnvil;
import me.geekles.anvillot.utils.text.T;
import org.bukkit.boss.BarFlag;
import org.bukkit.craftbukkit.v1_15_R1.boss.CraftBossBar;

public class TooExpensiveBar extends CraftBossBar {

    private final BetterContainerAnvil anvil;
    private boolean enabled;

    public TooExpensiveBar(BetterContainerAnvil anvil, BarFlag... flags) {
        super("",
                GeneralPresets.TooExpensiveBarPresets.COLOR,
                GeneralPresets.TooExpensiveBarPresets.STYLE,
                flags);
        this.anvil = anvil;
        this.enabled = false;
        update();
    }

    /**
     * Displays bar to assigned player
     */
    public void enable() {
        if (enabled)
            return;
        update();
        super.addPlayer(anvil.getOwner());
        enabled = true;
    }

    /**
     * Removes bar from assigned player
     */
    public void disable() {
        if (!enabled)
            return;
        super.removePlayer(anvil.getOwner());
        enabled = false;
    }

    /**
     * Check if bar is currently enabled
     *
     * @return is enabled
     */
    public boolean isEnabled() {
        return enabled;
    }

    public void update() {
        super.setProgress(Math.min(1f, (float) anvil.getOwner().getLevel() / (float) anvil.getCost()));
        super.setTitle(T.C(GeneralPresets.TooExpensiveBarPresets.TITLE.replaceAll("%cost", "" + anvil.getCost())));
    }

    public void destroy() {
        disable();
    }

}
