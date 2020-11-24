package dev.qruet.anvillot.nms;

import dev.qruet.anvillot.config.GeneralPresets;
import dev.qruet.anvillot.util.RepairCostCalculator;
import dev.qruet.anvillot.util.java.Pair;
import dev.qruet.anvillot.util.math.Equation;
import org.bukkit.entity.Player;

/**
 * @author qruet
 */
public interface IContainerAnvilLot {

    /**
     * Retrieve owner of inventory (current viewer)
     *
     * @return Player
     */
    Player getOwner();

    /**
     * Retrieve assigned repair cost
     *
     * @return Cost
     */
    int getCost();

    /**
     * Maximum possible repair cost
     *
     * @return Cost
     */
    int getMaximumCost();

    /**
     * Get updated name of result item
     *
     * @return Result item's name
     */
    String getRenameText();

    /**
     * Update's repair cost both locally and the player's client
     *
     * @param val New cost
     */
    void updateCost(int val);

    /**
     * Handle anvil specific cost calculations
     *
     * @param first     First slot item
     * @param second    Second slot item
     * @param result    Result slot item
     * @param levelCost Original repair cost
     */
    default void e(IItemStackWrapper first, IItemStackWrapper second, IItemStackWrapper result, int levelCost) {
        if (getMaximumCost() != -1 && levelCost > getMaximumCost()) {
            if(GeneralPresets.HARD_LIMIT) {
                updateCost(-1);
                return;
            }
            updateCost(getMaximumCost());
        } else {
            int rPa = first.getRepairCost();
            int rPb = RepairCostCalculator.calculateCost(second.getBukkitCopy());

            int bonus = 0;
            if (!getRenameText().isEmpty()) {
                bonus++;
            }

            int cost = (int) Equation.evaluate(GeneralPresets.REPAIR_COST_EQUATION,
                    new Pair<>("first_item", (double) rPa),
                    new Pair<>("second_item", (double) rPb),
                    new Pair<>("rename_fee", (double) bonus));

            if(getMaximumCost() != -1 && levelCost > getMaximumCost()) {
                if(GeneralPresets.HARD_LIMIT) {
                    updateCost(-1);
                    return;
                }
            }

            updateCost(cost);

            if (!result.isEmpty()) {
                result.setRepairCost((int) Equation.evaluate(GeneralPresets.REPAIR_PROGRESSION_EQUATION,
                        new Pair<>("first_item", (double) rPa),
                        new Pair<>("second_item", (double) rPb))); //increment result item's repair cost
            }
        }
    }

}
