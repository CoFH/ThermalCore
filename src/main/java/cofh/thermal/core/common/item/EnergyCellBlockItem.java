package cofh.thermal.core.common.item;

import cofh.core.util.helpers.AugmentDataHelper;
import cofh.lib.api.item.IEnergyContainerItem;
import cofh.lib.common.energy.EnergyStorageCoFH;
import cofh.thermal.core.common.block.entity.storage.EnergyCellBlockEntity;
import cofh.thermal.lib.common.item.BlockItemAugmentable;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;
import java.util.List;

import static cofh.core.util.helpers.AugmentableHelper.getPropertyWithDefault;
import static cofh.core.util.helpers.AugmentableHelper.setAttributeFromAugmentMax;
import static cofh.lib.api.ContainerType.ENERGY;
import static cofh.lib.util.constants.NBTTags.*;
import static cofh.lib.util.helpers.StringHelper.*;

public class EnergyCellBlockItem extends BlockItemAugmentable implements IEnergyContainerItem {

    public EnergyCellBlockItem(Block blockIn, Properties builder) {

        super(blockIn, builder);

        setEnchantability(5);
    }

    @Override
    protected void tooltipDelegate(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {

        boolean creative = isCreative(stack, ENERGY);
        if (getMaxEnergyStored(stack) > 0) {
            tooltip.add(creative
                    ? getTextComponent(localize("info.cofh.energy") + ": ").append(getTextComponent("info.cofh.infinite").withStyle(ChatFormatting.LIGHT_PURPLE).withStyle(ChatFormatting.ITALIC))
                    : getTextComponent(localize("info.cofh.energy") + ": " + getScaledNumber(getEnergyStored(stack)) + " / " + getScaledNumber(getMaxEnergyStored(stack)) + " " + localize("info.cofh.unit_rf")));
        }
        addEnergyTooltip(stack, worldIn, tooltip, flagIn, getExtract(stack), getReceive(stack), creative);
    }

    protected void setAttributesFromAugment(ItemStack container, CompoundTag augmentData) {

        CompoundTag subTag = container.getTagElement(TAG_PROPERTIES);
        if (subTag == null) {
            return;
        }
        setAttributeFromAugmentMax(subTag, augmentData, TAG_AUGMENT_BASE_MOD);
        setAttributeFromAugmentMax(subTag, augmentData, TAG_AUGMENT_RF_STORAGE);
        setAttributeFromAugmentMax(subTag, augmentData, TAG_AUGMENT_RF_XFER);
        setAttributeFromAugmentMax(subTag, augmentData, TAG_AUGMENT_RF_CREATIVE);
    }

    //    @Override
    //    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
    //
    //        return new EnergyContainerItemWrapper(stack, this);
    //    }

    // region IEnergyContainerItem
    @Override
    public Capability<? extends IEnergyStorage> getEnergyCapability() {

        return ForgeCapabilities.ENERGY;
    }

    @Override
    public CompoundTag getOrCreateEnergyTag(ItemStack container) {

        CompoundTag blockTag = container.getOrCreateTagElement(TAG_BLOCK_ENTITY);
        if (!blockTag.contains(TAG_ENERGY_MAX)) {
            new EnergyStorageCoFH(EnergyCellBlockEntity.BASE_CAPACITY, EnergyCellBlockEntity.BASE_RECV, EnergyCellBlockEntity.BASE_SEND).writeWithParams(blockTag);
        }
        return container.getOrCreateTagElement(TAG_BLOCK_ENTITY);
    }

    @Override
    public int getExtract(ItemStack container) {

        CompoundTag tag = getOrCreateEnergyTag(container);
        return Math.round(tag.getInt(TAG_ENERGY_SEND));
    }

    @Override
    public int getReceive(ItemStack container) {

        CompoundTag tag = getOrCreateEnergyTag(container);
        return Math.round(tag.getInt(TAG_ENERGY_RECV));
    }

    @Override
    public int getMaxEnergyStored(ItemStack container) {

        CompoundTag tag = getOrCreateEnergyTag(container);
        float base = getPropertyWithDefault(container, TAG_AUGMENT_BASE_MOD, 1.0F);
        float mod = getPropertyWithDefault(container, TAG_AUGMENT_RF_STORAGE, 1.0F);
        return getMaxStored(container, Math.round(tag.getInt(TAG_ENERGY_MAX) * mod * base));
    }
    // endregion

    // region IAugmentableItem
    @Override
    public void updateAugmentState(ItemStack container, List<ItemStack> augments) {

        container.getOrCreateTag().put(TAG_PROPERTIES, new CompoundTag());
        for (ItemStack augment : augments) {
            CompoundTag augmentData = AugmentDataHelper.getAugmentData(augment);
            if (augmentData == null) {
                continue;
            }
            setAttributesFromAugment(container, augmentData);
        }
        int energyExcess = getEnergyStored(container) - getMaxEnergyStored(container);
        if (energyExcess > 0) {
            setEnergyStored(container, getMaxEnergyStored(container));
        }
    }
    // endregion
}
