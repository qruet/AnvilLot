package dev.qruet.anvillot.nms.v1_15_R1;

import dev.qruet.anvillot.bar.v1_15_R1.ExperienceBar;
import dev.qruet.anvillot.bar.v1_15_R1.HardLimitBar;
import dev.qruet.anvillot.bar.v1_15_R1.TooExpensiveBar;
import dev.qruet.anvillot.config.GeneralPresets;
import dev.qruet.anvillot.config.assets.SoundMeta;
import dev.qruet.anvillot.nms.IContainerAnvilLot;
import dev.qruet.anvillot.util.L;
import dev.qruet.anvillot.util.java.LiveReflector;
import dev.qruet.anvillot.util.num.Int;
import net.minecraft.server.v1_15_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarFlag;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
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

    private final IInventory repairInventory;
    private final IInventory resultInventory;

    private LiveReflector<Integer> h;

    private final EntityPlayer owner;

    private ExperienceBar expBar;
    private TooExpensiveBar errBar;
    private HardLimitBar hlmBar;

    private int maxCost;
    private int repairCost;

    private final PacketPlayOutGameStateChange defaultMode;

    public ContainerAnvilLot(int i, PlayerInventory playerinventory, final ContainerAccess containeraccess) {
        super(i, playerinventory, containeraccess);

        Object rpI = null;
        Object rlI = null;
        try {
            Field repairInventory = ContainerAnvil.class.getDeclaredField("repairInventory");
            
            repairInventory.setAccessible(true);

            rpI = repairInventory.get(this);

            Field resultInventory = ContainerAnvil.class.getDeclaredField("resultInventory");
            
            resultInventory.setAccessible(true);

            rlI = resultInventory.get(this);

            Field h = ContainerAnvil.class.getDeclaredField("h");
            this.h = new LiveReflector<>(this, h);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }

        super.maximumRepairCost = Integer.MAX_VALUE;

        repairInventory = (IInventory) rpI;
        resultInventory = (IInventory) rlI;

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

        super.slots.set(2, new Slot(this.resultInventory, 2, 134, 47) {
            public boolean isAllowed(ItemStack itemstack) {
                return false;
            }

            public boolean isAllowed(EntityHuman entityhuman) {
                return (entityhuman.abilities.canInstantlyBuild || entityhuman.expLevel >= repairCost) && this.hasItem();
            }

            public ItemStack a(EntityHuman entityhuman, ItemStack itemstack) {
                if (!entityhuman.abilities.canInstantlyBuild) {
                    entityhuman.levelDown(-repairCost);
                    if (expBar != null)
                        expBar.update();
                }

                PacketPlayOutSetSlot packet = new PacketPlayOutSetSlot(-1, -1, playerinventory.getCarried());
                owner.playerConnection.sendPacket(packet);

                repairInventory.setItem(0, ItemStack.a);
                if (h.get() > 0) {
                    ItemStack itemstack1 = repairInventory.getItem(1);
                    if (!itemstack1.isEmpty() && itemstack1.getCount() > h.get()) {
                        itemstack1.subtract(h.get());
                        repairInventory.setItem(1, itemstack1);
                    } else {
                        repairInventory.setItem(1, ItemStack.a);
                    }
                } else {
                    repairInventory.setItem(1, ItemStack.a);
                }

                setRepairCost(0);
                containeraccess.a((world, blockposition) -> {
                    IBlockData iblockdata = world.getType(blockposition);
                    if (!entityhuman.abilities.canInstantlyBuild && iblockdata.a(TagsBlock.ANVIL) && entityhuman.getRandom().nextFloat() < 0.12F) {
                        IBlockData iblockdata1 = BlockAnvil.e(iblockdata);
                        if (iblockdata1 == null) {
                            world.a(blockposition, false);
                            world.triggerEffect(1029, blockposition, 0);
                        } else {
                            world.setTypeAndData(blockposition, iblockdata1, 2);
                            world.triggerEffect(1030, blockposition, 0);
                        }
                    } else {
                        world.triggerEffect(1030, blockposition, 0);
                    }

                });
                return itemstack;
            }
        });

        L.R(new Listener() {
            @EventHandler
            public void onDamage(EntityDamageEvent e) {
                if (!(e.getEntity() instanceof Player))
                    return;
                Player player = (Player) e.getEntity();
                if (player.getUniqueId().equals(getOwner().getUniqueId())) {
                    player.closeInventory();
                    HandlerList.unregisterAll(this);
                }
            }
        });

        maxCost = GeneralPresets.DEFAULT_MAX_COST;

        getOwner().getEffectivePermissions().stream().forEach(pI -> {
            String permission = pI.getPermission();
            if (!permission.startsWith("anvillot.limit."))
                return;
            this.maxCost = Int.P(permission.substring("anvillot.limit.".length()));
        });

        if (maxCost == -1) {
            maxCost = Integer.MAX_VALUE;
        }

        defaultMode = new PacketPlayOutGameStateChange(3, 3);
        owner.playerConnection.sendPacket(defaultMode);

        owner.updateAbilities();

        this.repairCost = levelCost.get();
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
        return true;
    }

    @Override
    public ItemStack a(int i, int j, InventoryClickType inventoryclicktype, EntityHuman entityhuman) {
        if (!(i == 0 || i == 1) && inventoryclicktype == InventoryClickType.PICKUP) {
            if (getOwner().getLevel() < repairCost)
                return super.a(i, j, inventoryclicktype, entityhuman);
        }
        e();
        return super.a(i, j, inventoryclicktype, entityhuman);
    }

    @Override
    public void e() {
        super.e();

        ItemStack first = repairInventory.getItem(0);
        ItemStack second = repairInventory.getItem(1);
        ItemStack result = resultInventory.getItem(0);

        PrepareAnvilEvent event = new PrepareAnvilEvent(getBukkitView(),
                CraftItemStack.asCraftMirror(result).clone());
        Bukkit.getPluginManager().callEvent(event);
        result = CraftItemStack.asNMSCopy(event.getResult());
        resultInventory.setItem(0, result);

        if (repairCost >= 40) {
            PacketPlayOutGameStateChange packet = new PacketPlayOutGameStateChange(3, 1);
            owner.playerConnection.sendPacket(packet);
        } else {
            owner.playerConnection.sendPacket(defaultMode);
        }

        IContainerAnvilLot.super.calculate(new ItemStackWrapper(first), new ItemStackWrapper(second), new ItemStackWrapper(result), levelCost.get());

        PacketPlayOutSetSlot spack = new PacketPlayOutSetSlot(windowId, 2, resultInventory.getItem(0));
        owner.playerConnection.sendPacket(spack);
    }

    public void setRepairCost(int val) {
        repairCost = val;
        levelCost.set(val);
        if(expBar != null)
            expBar.update();
        if (repairCost == -1 && GeneralPresets.HARD_LIMIT) {
            if (hlmBar != null) {
                if (!hlmBar.isEnabled())
                    hlmBar.enable();
            }

            resultInventory.setItem(0, ItemStack.a);

            SoundMeta sM = GeneralPresets.HARD_LIMIT_ALERT;
            if (sM != null) {
                getOwner().playSound(
                        getOwner().getLocation(),
                        sM.getSound(),
                        sM.getVolume(),
                        sM.getPitch());
            }
            return;
        }
        if (getOwner().getLevel() < repairCost) {
            PacketPlayOutGameStateChange packet = new PacketPlayOutGameStateChange(3, 3);
            owner.playerConnection.sendPacket(packet);

            levelCost.set(40);

            if (errBar != null) {
                if (!errBar.isEnabled())
                    errBar.enable();
                else
                    errBar.update();
            }

            resultInventory.setItem(0, ItemStack.a);


            SoundMeta sM = GeneralPresets.TOO_EXPENSIVE_ALERT;
            if (sM != null) {
                getOwner().playSound(
                        getOwner().getLocation(),
                        sM.getSound(),
                        sM.getVolume(),
                        sM.getPitch());
            }

            return;
        }
        if (hlmBar != null && hlmBar.isEnabled())
            hlmBar.disable();
        if (errBar != null && errBar.isEnabled())
            errBar.disable();
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
        return renameText;
    }

    private void reset() {
        PacketPlayOutGameStateChange packet = new PacketPlayOutGameStateChange(3, owner.playerInteractManager.getGameMode().getId());
        owner.playerConnection.sendPacket(packet);
        owner.updateAbilities();
    }


}
