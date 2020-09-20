package dev.qruet.anvillot.nms;

import dev.qruet.anvillot.AnvilLot;
import dev.qruet.anvillot.utils.ReflectionUtils;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class VersionHandler {

    private static String VERSION;

    private static Class<?> ContainerAnvil;
    private static Class<?> CraftInventoryAnvil;
    private static Class<?> EntityPlayer;
    private static Class<?> CraftPlayer;
    private static Class<?> ITileInventory;
    private static Class<?> ChatMessage;
    private static Class<?> IChatBaseComponent;
    private static Class<?> ITileEntityContainer;

    private static Class<?> AnvilLotTileInventory;

    private static Method getHandle;
    private static Method openContainer;
    private static Method openTileEntity;

    private static Object container_title;

    public static boolean init() {
        if(AnvilLotTileInventory != null)
            throw new UnsupportedOperationException("VersionHandler has already been initialized.");

        VERSION = ReflectionUtils.getVersion();

        ContainerAnvil = ReflectionUtils.getNMSClass("ContainerAnvil");
        CraftInventoryAnvil = ReflectionUtils.getCraftBukkitClass("inventory.CraftInventoryAnvil");
        EntityPlayer = ReflectionUtils.getNMSClass("EntityPlayer");
        CraftPlayer = ReflectionUtils.getCraftBukkitClass("entity.CraftPlayer");
        ITileInventory = ReflectionUtils.getNMSClass("ITileInventory");
        ChatMessage = ReflectionUtils.getNMSClass("ChatMessage");
        IChatBaseComponent = ReflectionUtils.getNMSClass("IChatBaseComponent");
        ITileEntityContainer = ReflectionUtils.getNMSClass("ITileEntityContainer");

        try {
            AnvilLotTileInventory = Class.forName(VersionHandler.class.getPackage().getName() + "." + VERSION + ".AnvilLotTileInventory");
        } catch (ClassNotFoundException e) {
            AnvilLot.shutdown(AnvilLot.ShutdownReason.UNSUPPORTED_VERSION);
            return false;
        }


        getHandle = ReflectionUtils.getMethod(CraftPlayer, "getHandle");
        openContainer = ReflectionUtils.getMethod(EntityPlayer, "openContainer", ITileInventory);

        if (VERSION.startsWith("v1_13"))
            openTileEntity = ReflectionUtils.getMethod(EntityPlayer, "openTileEntity", ITileEntityContainer);
        else
            openTileEntity = null;

        try {
            container_title = ChatMessage.getConstructor(String.class, Object[].class).newInstance("Repair & Name", new Object[]{});
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
            AnvilLot.shutdown(AnvilLot.ShutdownReason.UNSUPPORTED_VERSION);
            return false;
        }
        return true;
    }

    public static void openContainer(Player player) {
        if(AnvilLotTileInventory == null)
            throw new UnsupportedOperationException("VersionHandler has not yet been initialized.");
        try {
            Object eplayer = getHandle.invoke(CraftPlayer.cast(player));
            Object tileInventory = AnvilLotTileInventory.getConstructor(IChatBaseComponent).newInstance(container_title);
            if (openTileEntity != null)
                openTileEntity.invoke(eplayer, tileInventory);
            else
                openContainer.invoke(eplayer, tileInventory);
        } catch (IllegalAccessException | NoSuchMethodException | InstantiationException | InvocationTargetException ex) {
            ex.printStackTrace();
        }
    }


}
