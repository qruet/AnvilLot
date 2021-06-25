package dev.qruet.anvillot.nms;

import dev.qruet.anvillot.util.ReflectionUtils;
import net.minecraft.network.chat.ChatMessage;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.ITileInventory;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class VersionHandlerNMS extends VersionHandler {

    private static String VERSION;

    private static Class<?> CraftPlayer;

    private static Class<?> AnvilLotTileInventory;

    private static Method getHandle;

    static {
        VERSION = ReflectionUtils.getVersion();
        
        CraftPlayer = ReflectionUtils.getCraftBukkitClass("entity.CraftPlayer");

        try {
            AnvilLotTileInventory = getLocalNMSClass("AnvilLotTileInventory");
        } catch (ClassNotFoundException e) {
        }

        getHandle = ReflectionUtils.getMethod(CraftPlayer, "getHandle");
    }

    public static Class<?> getLocalNMSClass(String className) throws ClassNotFoundException {
        Class<?> clazz = Class.forName(VersionHandlerNMS.class.getPackage().getName() + "." + VERSION + "." + className);
        return clazz;
    }

    public void openContainer(Player player) {
        if (AnvilLotTileInventory == null)
            throw new UnsupportedOperationException("VersionHandler has not yet been initialized.");
        try {
            EntityPlayer eplayer = (EntityPlayer) getHandle.invoke(CraftPlayer.cast(player));
            ITileInventory tileInventory = (ITileInventory) AnvilLotTileInventory.getConstructor(IChatBaseComponent.class).newInstance(new ChatMessage("Repair & Name"));
            eplayer.openContainer(tileInventory);
        } catch (IllegalAccessException | NoSuchMethodException | InstantiationException | InvocationTargetException ex) {
            ex.printStackTrace();
        }
    }


}
