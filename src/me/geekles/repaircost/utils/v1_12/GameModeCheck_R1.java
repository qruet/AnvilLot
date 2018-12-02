package me.geekles.repaircost.utils.v1_12;

import me.geekles.repaircost.utils.ModeCheckManager;
import net.minecraft.server.v1_12_R1.EntityHuman;
import net.minecraft.server.v1_12_R1.PacketPlayOutGameStateChange;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

public class GameModeCheck_R1 extends ModeCheckManager {

    private Player player = null; //player that is being checked

    public GameModeCheck_R1(Player player) {
        this.player = player;

        EntityHuman entityHuman = ((CraftHumanEntity) player).getHandle();
        PacketPlayOutGameStateChange packet = new PacketPlayOutGameStateChange(3/*Gamemode Change Mode*/, 1/*Gamemode*/);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet); //sends packet to player's client to make their client think they are in client
        entityHuman.abilities.canInstantlyBuild = true; //Tricks some of the functions in spigot into thinking that the player is in creative so that no limits are placed onto the player while repairing the anvil
    }

    /**
     *
     * @param close Should the inventory be closed upon terminating this check? (you should probably set this to true)
     */
    @Override
    public void terminate(boolean close) {
        if (close)
            player.closeInventory(); //a good idea
        //remove check
        removePlayerCheck(player); //what kind of weirdo checks to see if the player has the anvil inventory still open when we literally just closed it
    }

    /**
     * Terminates class
     */
    @Override
    public void terminate() {
        terminate(true);
    }

    /**
     * Method that checks to see if the player still has the anvil open
     */
    @Override
    public void update() {
        if(player == null){
            terminate();
            return;
        }
        if(player.getOpenInventory().getTopInventory().getType() != InventoryType.ANVIL){
            terminate();
        }
    }

    public void revertCreativeClientEffect(Player player) {
        if (GamemodeChecks.containsKey(player.getUniqueId())) {
            PacketPlayOutGameStateChange packet = new PacketPlayOutGameStateChange(3/*Gamemode Change Mode*/, 0/*survival*/);
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
            ((CraftHumanEntity) player).getHandle().abilities.canInstantlyBuild = false;
        }
    }

}
