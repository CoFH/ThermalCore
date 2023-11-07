package cofh.thermal.core.common.item;

import cofh.core.common.item.ItemCoFH;
import net.minecraft.world.item.ItemStack;

public class SlotSealItem extends ItemCoFH {

    public SlotSealItem(Properties builder) {

        super(builder);
    }

    @Override
    public int hashCode() {

        return 0;
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack stack) {

        return new ItemStack(this);
    }

    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {

        return true;
    }

}
