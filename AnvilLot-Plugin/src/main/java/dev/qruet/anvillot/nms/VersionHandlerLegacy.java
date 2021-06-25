package dev.qruet.anvillot.nms;

import dev.qruet.anvillot.util.ReflectionUtils;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class VersionHandlerLegacy extends VersionHandler {

    private static String VERSION;

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

    static {
        VERSION = ReflectionUtils.getVersion();


        EntityPlayer = ReflectionUtils.getNMSClass("EntityPlayer");
        CraftPlayer = ReflectionUtils.getCraftBukkitClass("entity.CraftPlayer");
        ITileInventory = ReflectionUtils.getNMSClass("ITileInventory");
        ChatMessage = ReflectionUtils.getNMSClass("ChatMessage");
        IChatBaseComponent = ReflectionUtils.getNMSClass("IChatBaseComponent");
        ITileEntityContainer = ReflectionUtils.getNMSClass("ITileEntityContainer");

        try {
            AnvilLotTileInventory = getLocalNMSClass("AnvilLotTileInventory");
        } catch (ClassNotFoundException e) {
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
        }
    }

    public static Class<?> getLocalNMSClass(String className) throws ClassNotFoundException {
        Class<?> clazz = Class.forName(VersionHandlerLegacy.class.getPackage().getName() + "." + VERSION + "." + className);
        return clazz;
    }

    public void openContainer(Player player) {
        if (AnvilLotTileInventory == null)
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
