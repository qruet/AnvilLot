package dev.qruet.anvillot.nms;

import dev.qruet.anvillot.AnvilLot;
import dev.qruet.anvillot.utils.ReflectionUtils;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class VersionHandler {

    private static final String VERSION;

    private static final Class<?> ContainerAnvil;
    private static final Class<?> CraftInventoryAnvil;
    private static final Class<?> EntityPlayer;
    private static final Class<?> CraftPlayer;
    private static final Class<?> ITileInventory;
    private static final Class<?> ChatMessage;
    private static final Class<?> IChatBaseComponent;

    private static Class<?> AnvilLotTileInventory;

    private static final Method getHandle;
    private static final Method openContainer;

    private static Object container_title;

    static {
        VERSION = ReflectionUtils.getVersion();

        ContainerAnvil = ReflectionUtils.getNMSClass("ContainerAnvil");
        CraftInventoryAnvil = ReflectionUtils.getCraftBukkitClass("inventory.CraftInventoryAnvil");
        EntityPlayer = ReflectionUtils.getNMSClass("EntityPlayer");
        CraftPlayer = ReflectionUtils.getCraftBukkitClass("entity.CraftPlayer");
        ITileInventory = ReflectionUtils.getNMSClass("ITileInventory");
        ChatMessage = ReflectionUtils.getNMSClass("ChatMessage");
        IChatBaseComponent = ReflectionUtils.getNMSClass("IChatBaseComponent");

        try {
            AnvilLotTileInventory = Class.forName(VersionHandler.class.getPackage().getName() + "." + VERSION + ".AnvilLotTileInventory");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            AnvilLot.shutdown(AnvilLot.ShutdownReason.UNSUPPORTED_VERSION);
        }

        getHandle = ReflectionUtils.getMethod(CraftPlayer, "getHandle");
        openContainer = ReflectionUtils.getMethod(EntityPlayer, "openContainer", ITileInventory);

        try {
            container_title = ChatMessage.getConstructor(String.class, Object[].class).newInstance("Repair & Name", new Object[]{});
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
            AnvilLot.shutdown(AnvilLot.ShutdownReason.UNSUPPORTED_VERSION);
        }
    }

    public static void openContainer(Player player) {
        try {
            Object eplayer = getHandle.invoke(CraftPlayer.cast(player));
            Object tileInventory = AnvilLotTileInventory.getConstructor(IChatBaseComponent).newInstance(container_title);
            openContainer.invoke(eplayer, tileInventory);
        } catch (IllegalAccessException | NoSuchMethodException | InstantiationException | InvocationTargetException ex) {
            ex.printStackTrace();
        }
    }


}
