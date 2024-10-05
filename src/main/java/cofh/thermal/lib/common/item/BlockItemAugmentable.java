package cofh.thermal.lib.common.item;

import cofh.core.common.item.BlockItemCoFH;
import cofh.core.common.item.IAugmentableItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.IntSupplier;

import static cofh.thermal.lib.util.ThermalAugmentRules.FILTER_VALIDATOR;
import static cofh.thermal.lib.util.ThermalAugmentRules.UPGRADE_VALIDATOR;

public class BlockItemAugmentable extends BlockItemCoFH implements IAugmentableItem {

    protected IntSupplier numSlots = () -> 0;
    protected BiPredicate<ItemStack, List<ItemStack>> augValidator = (e, f) -> true;
    protected boolean hasUpgradeSlot = true;
    protected boolean hasFilterSlot = true;

    public BlockItemAugmentable(Block blockIn, Properties builder) {

        super(blockIn, builder);
    }

    public BlockItemAugmentable setNumSlots(IntSupplier numSlots) {

        this.numSlots = numSlots;
        return this;
    }

    public BlockItemAugmentable setSpecialSlots(boolean hasUpgradeSlot, boolean hasFilterSlot) {

        this.hasUpgradeSlot = hasUpgradeSlot;
        this.hasFilterSlot = hasFilterSlot;

        return this;
    }

    public BlockItemAugmentable setAugValidator(BiPredicate<ItemStack, List<ItemStack>> augValidator) {

        this.augValidator = augValidator;
        return this;
    }

    // region IAugmentableItem
    @Override
    public int getAugmentSlots(ItemStack augmentable) {

        return numSlots.getAsInt();
    }

    @Override
    public boolean hasUpgradeSlot() {

        return hasUpgradeSlot;
    }

    @Override
    public boolean hasFilterSlot() {

        return hasFilterSlot;
    }

    @Override
    public boolean validAugment(int index, ItemStack augmentable, ItemStack augment, List<ItemStack> augments) {

        if (index == 0) {
            if (hasUpgradeSlot) {
                return UPGRADE_VALIDATOR.test(augment, augments);
            } else if (hasFilterSlot) {
                return FILTER_VALIDATOR.test(augment, augments);
            }
        } else if (index == 1) {
            if (hasUpgradeSlot && hasFilterSlot) {
                return FILTER_VALIDATOR.test(augment, augments);
            }
        }
        return augValidator.test(augment, augments);
    }

    @Override
    public void updateAugmentState(ItemStack augmentable, List<ItemStack> augments) {

    }
    // endregion
}
