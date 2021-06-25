package dev.qruet.anvillot.nms;

import dev.qruet.anvillot.util.ReflectionUtils;
import org.bukkit.entity.Player;

public abstract class VersionHandler {

    public static VersionHandler getHandlerInstance() {
        try {
            if (ReflectionUtils.getIntVersion() < 1170) {
                return new VersionHandlerLegacy();
            } else {
                return new VersionHandlerNMS();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public abstract void openContainer(Player player);

}
