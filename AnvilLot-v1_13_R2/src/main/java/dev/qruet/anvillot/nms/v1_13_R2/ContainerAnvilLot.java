package dev.qruet.anvillot.nms.v1_13_R2;

import dev.qruet.anvillot.bar.v1_13_R2.ExperienceBar;
import dev.qruet.anvillot.bar.v1_13_R2.HardLimitBar;
import dev.qruet.anvillot.bar.v1_13_R2.TooExpensiveBar;
import dev.qruet.anvillot.config.GeneralPresets;
import dev.qruet.anvillot.config.assets.SoundMeta;
import dev.qruet.anvillot.nms.IContainerAnvilLot;
import dev.qruet.anvillot.util.L;
import dev.qruet.anvillot.util.ReflectionUtils;
import dev.qruet.anvillot.util.num.Int;
import net.minecraft.server.v1_13_R2.*;
import org.bukkit.boss.BarFlag;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * A rewritten form of the anvil container class
 *
 * @author Qruet
 * @version 3.1.0-Beta-SNAPSHOT
 */
public class ContainerAnvilLot extends ContainerAnvil implements IContainerAnvilLot {

    private final IInventory repairInventory;
    private final IInventory resultInventory;

    private int h;

    private final EntityPlayer owner;

    private ExperienceBar expBar;
    private TooExpensiveBar errBar;
    private HardLimitBar hlmBar;

    private int maxCost;
    private int repairCost;

    private final PacketPlayOutGameStateChange defaultMode;

    public ContainerAnvilLot(PlayerInventory playerinventory, World world, final BlockPosition position, EntityHuman human) {
        super(playerinventory, world, position, human);

        Object rpI = null;
        Object rlI = null;
        try {
            Field repairInventory = null;
            try {
                repairInventory = ContainerAnvil.class.getDeclaredField("repairInventory");
            } catch (NoSuchFieldException e) {
                repairInventory = ContainerAnvil.class.getDeclaredField("h");
            }
            ReflectionUtils.makeNonFinal(repairInventory);
            repairInventory.setAccessible(true);

            rpI = repairInventory.get(this);

            Field resultInventory = null;
            try {
                resultInventory = ContainerAnvil.class.getDeclaredField("resultInventory");
            } catch (NoSuchFieldException e) {
                resultInventory = ContainerAnvil.class.getDeclaredField("g");
            }
            ReflectionUtils.makeNonFinal(resultInventory);
            resultInventory.setAccessible(true);

            rlI = resultInventory.get(this);

            Field h = ContainerAnvil.class.getDeclaredField("k");
            h.setAccessible(true);
            this.h = (int) h.get(this);
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
                if (h > 0) {
                    ItemStack itemstack1 = repairInventory.getItem(1);
                    if (!itemstack1.isEmpty() && itemstack1.getCount() > h) {
                        itemstack1.subtract(h);
                        repairInventory.setItem(1, itemstack1);
                    } else {
                        repairInventory.setItem(1, ItemStack.a);
                    }
                } else {
                    repairInventory.setItem(1, ItemStack.a);
                }

                updateCost(0);

                IBlockData iblockdata = world.getType(position);
                if (!world.isClientSide) {
                    if (!entityhuman.abilities.canInstantlyBuild && iblockdata.a(TagsBlock.ANVIL) && entityhuman.getRandom().nextFloat() < 0.12F) {
                        IBlockData iblockdata1 = BlockAnvil.a_(iblockdata);
                        if (iblockdata1 == null) {
                            world.setAir(position);
                            world.triggerEffect(1029, position, 0);
                        } else {
                            world.setTypeAndData(position, iblockdata1, 2);
                            world.triggerEffect(1030, position, 0);
                        }
                    } else {
                        world.triggerEffect(1030, position, 0);
                    }
                }
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

        this.repairCost = levelCost;
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
        d();
        return super.a(i, j, inventoryclicktype, entityhuman);
    }

    @Override
    public void d() {
        super.d();

        ItemStack first = repairInventory.getItem(0);
        ItemStack second = repairInventory.getItem(1);
        ItemStack result = resultInventory.getItem(0);

        if (repairCost >= 40) {
            PacketPlayOutGameStateChange packet = new PacketPlayOutGameStateChange(3, 1);
            owner.playerConnection.sendPacket(packet);
        } else {
            owner.playerConnection.sendPacket(defaultMode);
        }

        IContainerAnvilLot.super.e(new ItemStackWrapper(first), new ItemStackWrapper(second), new ItemStackWrapper(result), levelCost);

        PacketPlayOutSetSlot spack = new PacketPlayOutSetSlot(windowId, 2, result);
        owner.playerConnection.sendPacket(spack);
    }

    public void updateCost(int val) {
        repairCost = val;
        levelCost = val;
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

            levelCost = 40;

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

    @Override
    public int getMaximumCost() {
        return maxCost;
    }

    @Override
    public String getRenameText() {
        return renameText;
    }

    public Player getOwner() {
        return owner.getBukkitEntity();
    }


    private void reset() {
        PacketPlayOutGameStateChange packet = new PacketPlayOutGameStateChange(3, owner.playerInteractManager.getGameMode().getId());
        owner.playerConnection.sendPacket(packet);
        owner.updateAbilities();
    }


}
