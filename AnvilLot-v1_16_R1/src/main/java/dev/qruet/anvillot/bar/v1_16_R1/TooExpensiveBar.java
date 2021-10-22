package dev.qruet.anvillot.bar.v1_16_R1;

import dev.qruet.anvillot.config.GeneralPresets;
import dev.qruet.anvillot.nms.IContainerAnvilLot;
import dev.qruet.anvillot.util.text.T;
import org.bukkit.boss.BarFlag;
import org.bukkit.craftbukkit.v1_16_R1.boss.CraftBossBar;

@Deprecated
public class TooExpensiveBar extends CraftBossBar {

    private final IContainerAnvilLot anvil;
    private boolean enabled;

    public TooExpensiveBar(IContainerAnvilLot anvil, BarFlag... flags) {
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
        float progress = anvil.getOwner().getLevel() / (float) anvil.getCost();
        progress = Float.isNaN(progress) ? 0 : progress;
        super.setProgress(Math.min(1f, progress));
        super.setTitle(T.C(GeneralPresets.TooExpensiveBarPresets.TITLE.replaceAll("%cost", "" + anvil.getCost())));
    }

    public void destroy() {
        disable();
    }

}
