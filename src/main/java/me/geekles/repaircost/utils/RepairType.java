package me.geekles.repaircost.utils;

import me.geekles.repaircost.MaxRepairCost;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public enum RepairType {
    //TODO Add ability to control cost increments for each item in anvil
    /*
    WOOD(1, 1.015, Material.WOODEN_AXE, Material.WOODEN_HOE, Material.WOODEN_SHOVEL, Material.WOODEN_PICKAXE, Material.WOODEN_SWORD),
    STONE(2, 1.025, Material.STONE_AXE, Material.STONE_HOE, Material.STONE_SHOVEL, Material.STONE_PICKAXE, Material.STONE_SWORD),
    GOLD(2, 1.005, Material.GOLDEN_AXE, Material.GOLDEN_HOE, Material.GOLDEN_SHOVEL, Material.GOLDEN_PICKAXE, Material.GOLDEN_SWORD),
    IRON(3, 1.025, Material.IRON_AXE, Material.IRON_HOE, Material.IRON_SHOVEL, Material.IRON_PICKAXE, Material.IRON_SWORD),
    DIAMOND(3, 1.05, Material.DIAMOND_AXE, Material.DIAMOND_HOE, Material.DIAMOND_SHOVEL, Material.DIAMOND_PICKAXE, Material.DIAMOND_SWORD),
    OTHER(2, 1.025, null);

    private Material[] materials;
    private double base_exp_cost;
    private double cost_exp_increment;
    private static int max;

    RepairType(int cost, double cost_exp, Material... materials) {
        this.base_exp_cost = cost;
        this.cost_exp_increment = cost_exp;
        this.materials = materials;
    }

    public static void setup(MaxRepairCost main){
        max = main.getMaxRepairCost();
    }

    public double getBaseCostExp() {
        return base_exp_cost;
    }


    public List<Material> getMaterials() {
        return Arrays.asList(materials);
    }

    public static RepairType getRepairType(Material material){
        for(RepairType type : RepairType.values()){
            if(type != RepairType.OTHER) {
                if (type.getMaterials().contains(material)) {
                    return type;
                }
            }
        }
        return RepairType.OTHER;
    }
    */
}
