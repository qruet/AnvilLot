package dev.qruet.anvillot.bar.v1_15_R1;

import dev.qruet.anvillot.config.GeneralPresets;
import dev.qruet.anvillot.nms.IContainerAnvilLot;
import dev.qruet.anvillot.util.text.T;
import org.bukkit.boss.BarFlag;
import org.bukkit.craftbukkit.v1_15_R1.boss.CraftBossBar;

public class HardLimitBar extends CraftBossBar {

    private final IContainerAnvilLot anvil;
    private boolean enabled;

    public HardLimitBar(IContainerAnvilLot anvil, BarFlag... flags) {
        super("",
                GeneralPresets.TooExpensiveBarPresets.COLOR,
                GeneralPresets.TooExpensiveBarPresets.STYLE,
                flags);
        this.anvil = anvil;
        this.enabled = false;
        super.setTitle(T.C(GeneralPresets.HardLimitBarPresets.TITLE));
    }

    /**
     * Displays bar to assigned player
     */
    public void enable() {
        if (enabled)
            return;
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

    public void destroy() {
        disable();
    }

}

