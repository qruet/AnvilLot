package dev.qruet.anvillot.nms.v1_17_R1;

import dev.qruet.anvillot.bar.v1_17_R1.ExperienceBar;
import dev.qruet.anvillot.bar.v1_17_R1.HardLimitBar;
import dev.qruet.anvillot.bar.v1_17_R1.TooExpensiveBar;
import dev.qruet.anvillot.config.GeneralPresets;
import dev.qruet.anvillot.config.assets.SoundMeta;
import dev.qruet.anvillot.nms.IContainerAnvilLot;
import dev.qruet.anvillot.util.L;
import dev.qruet.anvillot.util.java.LiveReflector;
import dev.qruet.anvillot.util.num.Int;
import net.minecraft.network.protocol.game.PacketPlayOutGameStateChange;
import net.minecraft.network.protocol.game.PacketPlayOutSetSlot;
import net.minecraft.network.protocol.game.PacketPlayOutWindowData;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.tags.TagsBlock;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.entity.player.PlayerInventory;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.BlockAnvil;
import net.minecraft.world.level.block.state.IBlockData;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarFlag;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * A rewritten form of the anvil container class
 *
 * @author Qruet
 * @version 3.4.0-Beta-SNAPSHOT
 */
public class ContainerAnvilLot extends ContainerAnvil implements IContainerAnvilLot {

    private LiveReflector<Integer> u;

    private final EntityPlayer owner;

    private ExperienceBar expBar;
    private TooExpensiveBar errBar;
    private HardLimitBar hlmBar;

    private int maxCost = -1;
    private int repairCost;

    private final PacketPlayOutGameStateChange defaultMode;

    public ContainerAnvilLot(int i, PlayerInventory playerinventory, final ContainerAccess containeraccess) {
        super(i, playerinventory, containeraccess);

        try {
            Field u = ContainerAnvil.class.getDeclaredField("u");
            this.u = new LiveReflector<>(this, u);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        super.maximumRepairCost = Integer.MAX_VALUE;

        owner = ((CraftPlayer) playerinventory.getOwner()).getHandle();

        if (GeneralPresets.EXPERIENCE_BAR_ENABLED) {
            List<BarFlag> expFlagList = new ArrayList<>();
            if (GeneralPresets.ExperienceBarPresets.FOG)
                expFlagList.add(BarFlag.CREATE_FOG);
            if (GeneralPresets.ExperienceBarPresets.DARK_SKY)
                expFlagList.add(BarFlag.DARKEN_SKY);

            expBar = new ExperienceBar(getOwner(), expFlagList.toArray(new BarFlag[0]));
            expBar.enable();
        }

        if (GeneralPresets.TOO_EXPENSIVE_BAR_ENABLED) {
            List<BarFlag> errFlagList = new ArrayList<>();
            if (GeneralPresets.TooExpensiveBarPresets.FOG)
                errFlagList.add(BarFlag.CREATE_FOG);
            if (GeneralPresets.TooExpensiveBarPresets.DARK_SKY)
                errFlagList.add(BarFlag.DARKEN_SKY);
            errBar = new TooExpensiveBar(this, errFlagList.toArray(new BarFlag[0]));
        }

        if (GeneralPresets.HARD_LIMIT_BAR_ENABLED) {
            List<BarFlag> errFlagList = new ArrayList<>();
            if (GeneralPresets.HardLimitBarPresets.FOG)
                errFlagList.add(BarFlag.CREATE_FOG);
            if (GeneralPresets.HardLimitBarPresets.DARK_SKY)
                errFlagList.add(BarFlag.DARKEN_SKY);
            hlmBar = new HardLimitBar(this, errFlagList.toArray(new BarFlag[0]));
        }

        maxCost = GeneralPresets.DEFAULT_MAX_COST;

        getOwner().getEffectivePermissions().stream().forEach(pI -> {
            String permission = pI.getPermission();
            if (!permission.startsWith("anvillot.limit."))
                return;
            this.maxCost = Int.P(permission.substring("anvillot.limit.".length()));
        });

        maxCost = maxCost == -1 ? Integer.MAX_VALUE : maxCost;

        defaultMode = new PacketPlayOutGameStateChange(PacketPlayOutGameStateChange.d, 3);
        owner.b.sendPacket(defaultMode);
        owner.updateAbilities();

        this.repairCost = w.get();
    }

    @Override
    public void a(EntityHuman entityhuman, ItemStack itemstack) {
        if (!entityhuman.getAbilities().d) {
            entityhuman.levelDown(-repairCost);
            if (expBar != null)
                expBar.update();
        }


        sendSlotUpdate(-1, new ItemStackWrapper(entityhuman.getInventory().l.bV.getCarried()), -1);

        p.setItem(0, ItemStack.b);
        if (u.get() > 0) {
            ItemStack itemstack1 = p.getItem(1);
            if (!itemstack1.isEmpty() && itemstack1.getCount() > u.get()) {
                itemstack1.subtract(u.get());
                p.setItem(1, itemstack1);
            } else {
                p.setItem(1, ItemStack.b);
            }
        } else {
            p.setItem(1, ItemStack.b);
        }

        setRepairCost(0);
        this.q.a((world, blockposition) -> {
            IBlockData iblockdata = world.getType(blockposition);
            if (!entityhuman.getAbilities().d && iblockdata.a(TagsBlock.G) && entityhuman.getRandom().nextFloat() < 0.12F) {
                IBlockData iblockdata1 = BlockAnvil.e(iblockdata);
                if (iblockdata1 == null) {
                    world.a(blockposition, false);
                    world.triggerEffect(1029, blockposition, 0);
                    entityhuman.getInventory().f(itemstack);
                } else {
                    world.setTypeAndData(blockposition, iblockdata1, 2);
                    world.triggerEffect(1030, blockposition, 0);
                }
            } else {
                world.triggerEffect(1030, blockposition, 0);
            }

        });
    }

    @Override
    public void transferTo(Container other, CraftHumanEntity player) {
        if (expBar != null)
            expBar.destroy();
        if (errBar != null)
            errBar.destroy();
        if (hlmBar != null)
            hlmBar.destroy();
        reset();
    }

    @Override
    public boolean canUse(EntityHuman entityhuman) {
        return !this.checkReachable || (Boolean) this.q.a((world, blockposition) -> {
            return !this.a(world.getType(blockposition)) ? false : entityhuman.h((double) blockposition.getX() + 0.5D, (double) blockposition.getY() + 0.5D, (double) blockposition.getZ() + 0.5D) <= 64.0D;
        }, true);
    }

    @Override
    public void a(int i, int j, InventoryClickType inventoryclicktype, EntityHuman entityhuman) {
        super.a(i, j, inventoryclicktype, entityhuman);
    }

    @Override
    public void i() {
        super.i();

        ItemStack first = p.getItem(0); // retrieve item from index 0 in repair inventory
        ItemStack second = p.getItem(1); // retrieve item from index 1 in repair inventory
        ItemStack result = o.getItem(0); // retrieve item from index 0 in result inventory

        PrepareAnvilEvent event = new PrepareAnvilEvent(getBukkitView(),
                CraftItemStack.asCraftMirror(result).clone());
        Bukkit.getPluginManager().callEvent(event);

        result = CraftItemStack.asNMSCopy(event.getResult());
        o.setItem(0, result);

        if (repairCost >= 40) {
            PacketPlayOutGameStateChange packet = new PacketPlayOutGameStateChange(PacketPlayOutGameStateChange.d, 1);
            owner.b.sendPacket(packet);
        } else {
            owner.b.sendPacket(defaultMode);
        }

        IContainerAnvilLot.super.calculate(new ItemStackWrapper(first), new ItemStackWrapper(second), new ItemStackWrapper(result), w.get());

        sendSlotUpdate(2, new ItemStackWrapper(o.getItem(0)), j);
    }

    public void setRepairCost(int val) {
        repairCost = val;
        w.set(val);
        if (expBar != null)
            expBar.update();

        if (repairCost == -1 && GeneralPresets.HARD_LIMIT) {
            if (hlmBar != null) {
                if (!hlmBar.isEnabled())
                    hlmBar.enable();
            }

            o.setItem(0, ItemStack.b);

            SoundMeta sM = GeneralPresets.HARD_LIMIT_ALERT;
            if (sM != null) {
                getOwner().playSound(
                        getOwner().getLocation(),
                        sM.getSound(),
                        sM.getVolume(),
                        sM.getPitch());
            }
        } else if (getOwner().getLevel() < repairCost) {
            PacketPlayOutGameStateChange packet = new PacketPlayOutGameStateChange(PacketPlayOutGameStateChange.d, 3);
            owner.b.sendPacket(packet);

            w.set(40);

            if (errBar != null) {
                if (!errBar.isEnabled())
                    errBar.enable();
                else
                    errBar.update();
            }

            o.setItem(0, ItemStack.b);

            SoundMeta sM = GeneralPresets.TOO_EXPENSIVE_ALERT;
            if (sM != null) {
                getOwner().playSound(
                        getOwner().getLocation(),
                        sM.getSound(),
                        sM.getVolume(),
                        sM.getPitch());
            }
        } else {
            if (hlmBar != null && hlmBar.isEnabled())
                hlmBar.disable();

            if (errBar != null && errBar.isEnabled())
                errBar.disable();
        }

        super.d();
    }

    public int getCost() {
        return repairCost;
    }

    public Player getOwner() {
        return owner.getBukkitEntity();
    }

    @Override
    public int getMaximumCost() {
        return maxCost;
    }

    @Override
    public String getRenameText() {
        return v;
    }

    private void reset() {
        owner.b.sendPacket(new PacketPlayOutGameStateChange(PacketPlayOutGameStateChange.d, owner.d.getGameMode().getId()));
        owner.updateAbilities();
    }


}
