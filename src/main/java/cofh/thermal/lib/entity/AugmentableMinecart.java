package cofh.thermal.lib.entity;

import cofh.core.entity.AbstractMinecartCoFH;
import cofh.core.item.IAugmentableItem;
import cofh.core.util.filter.EmptyFilter;
import cofh.core.util.filter.IFilter;
import cofh.core.util.filter.IFilterable;
import cofh.core.util.helpers.AugmentDataHelper;
import cofh.core.util.helpers.FilterHelper;
import cofh.core.util.helpers.FluidHelper;
import cofh.lib.api.IStorageCallback;
import cofh.lib.inventory.ItemStorageCoFH;
import cofh.lib.inventory.SimpleItemInv;
import cofh.lib.util.Utils;
import cofh.thermal.core.config.ThermalCoreConfig;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static cofh.core.init.CoreEntityDataSerializers.FLUID_STACK_DATA_SERIALIZER;
import static cofh.core.util.helpers.AugmentableHelper.*;
import static cofh.core.util.helpers.ItemHelper.cloneStack;
import static cofh.lib.util.constants.NBTTags.*;
import static net.minecraft.nbt.Tag.TAG_COMPOUND;

public abstract class AugmentableMinecart extends AbstractMinecartCoFH implements IStorageCallback, IFilterable {

    protected static final EntityDataAccessor<Integer> ENERGY_STORED = SynchedEntityData.defineId(AugmentableMinecart.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Integer> FLUID_STORED = SynchedEntityData.defineId(AugmentableMinecart.class, EntityDataSerializers.INT);
    protected static EntityDataAccessor<FluidStack> FLUID_STACK_STORED;

    public static void setup() {

        FLUID_STACK_STORED = SynchedEntityData.defineId(AugmentableMinecart.class, FLUID_STACK_DATA_SERIALIZER.get());
    }

    protected SimpleItemInv inventory = new SimpleItemInv(this);
    protected List<ItemStorageCoFH> augments = Collections.emptyList();
    protected IFilter filter = EmptyFilter.INSTANCE;

    protected AugmentableMinecart(EntityType<?> type, Level worldIn) {

        super(type, worldIn);
    }

    protected AugmentableMinecart(EntityType<?> type, Level worldIn, double posX, double posY, double posZ) {

        super(type, worldIn, posX, posY, posZ);
    }

    protected boolean attemptFluidHandlerInteraction(Player player, InteractionHand hand) {

        return getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY).map(handler -> FluidHelper.interactWithHandler(player.getItemInHand(hand), handler, player, hand)).orElse(false);
    }

    @Override
    public AugmentableMinecart onPlaced(ItemStack stack) {

        super.onPlaced(stack);

        CompoundTag nbt = stack.getTag();
        if (nbt != null) {
            if (nbt.contains(TAG_AUGMENTS)) {
                inventory.readSlotsUnordered(nbt.getList(TAG_AUGMENTS, TAG_COMPOUND), invSize() - augSize());
            }
        }
        updateAugmentState();

        return this;
    }

    @Override
    public ItemStack createItemStackTag(ItemStack stack) {

        CompoundTag nbt = stack.getOrCreateTag();

        if (ThermalCoreConfig.keepAugments.get() && augSize() > 0) {
            getItemInv().writeSlotsToNBTUnordered(nbt, TAG_AUGMENTS, invSize() - augSize());
            if (stack.getItem() instanceof IAugmentableItem augmentableItem) {
                List<ItemStack> items = getAugmentsAsList();
                augmentableItem.updateAugmentState(stack, items);
            }
            filter.write(nbt);
        }
        return super.createItemStackTag(stack);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {

        super.readAdditionalSaveData(compound);

        inventory.read(compound);

        if (compound.contains(TAG_AUGMENTS)) {
            inventory.readSlotsUnordered(compound.getList(TAG_AUGMENTS, TAG_COMPOUND), invSize() - augSize());
        }
        updateAugmentState();
        filter.read(compound);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {

        super.addAdditionalSaveData(compound);

        compound.put(TAG_ENCHANTMENTS, enchantments);

        inventory.write(compound);
        filter.write(compound);
    }

    // region HELPERS
    public int invSize() {

        return inventory.getSlots();
    }

    public int augSize() {

        return augments.size();
    }

    public SimpleItemInv getItemInv() {

        return inventory;
    }
    // endregion

    // region AUGMENTS
    protected boolean creativeEnergy = false;
    protected boolean creativeTanks = false;
    // protected boolean creativeSlots = false;

    // This is CLEARED after augments are finalized.
    protected CompoundTag augmentNBT;

    protected final boolean attemptAugmentInstall(ItemStack stack) {

        for (ItemStorageCoFH augSlot : augments) {
            if (augSlot.isEmpty() && augSlot.isItemValid(stack)) {
                augSlot.setItemStack(cloneStack(stack, 1));
                updateAugmentState();
                return true;
            }
        }
        return false;
    }

    /**
     * This should be called AFTER all other slots have been added.
     *
     * @param numAugments Number of augment slots to add.
     */
    protected final void addAugmentSlots(int numAugments) {

        augments = new ArrayList<>(numAugments);
        for (int i = 0; i < numAugments; ++i) {
            ItemStorageCoFH slot = new ItemStorageCoFH(1, augValidator());
            augments.add(slot);
            inventory.addSlot(slot);
        }
        ((ArrayList<ItemStorageCoFH>) augments).trimToSize();
    }

    protected final void updateAugmentState() {

        resetAttributes();
        for (ItemStorageCoFH slot : augments) {
            ItemStack augment = slot.getItemStack();
            CompoundTag augmentData = AugmentDataHelper.getAugmentData(augment);
            if (augmentData == null) {
                continue;
            }
            setAttributesFromAugment(augmentData);
        }
        finalizeAttributes(EnchantmentHelper.deserializeEnchantments(enchantments));
        augmentNBT = null;
    }

    protected final List<ItemStack> getAugmentsAsList() {

        return augments.stream().map(ItemStorageCoFH::getItemStack).collect(Collectors.toList());
    }

    protected Predicate<ItemStack> augValidator() {

        return AugmentDataHelper::hasAugmentData;
    }

    protected void resetAttributes() {

        augmentNBT = new CompoundTag();

        creativeEnergy = false;
        creativeTanks = false;
        // creativeSlots = false;
    }

    protected void setAttributesFromAugment(CompoundTag augmentData) {

        setAttributeFromAugmentMax(augmentNBT, augmentData, TAG_AUGMENT_BASE_MOD);
        setAttributeFromAugmentMax(augmentNBT, augmentData, TAG_AUGMENT_RF_XFER);
        setAttributeFromAugmentMax(augmentNBT, augmentData, TAG_AUGMENT_RF_STORAGE);
        setAttributeFromAugmentMax(augmentNBT, augmentData, TAG_AUGMENT_FLUID_STORAGE);
        setAttributeFromAugmentMax(augmentNBT, augmentData, TAG_AUGMENT_ITEM_STORAGE);

        setAttributeFromAugmentString(augmentNBT, augmentData, TAG_FILTER_TYPE);

        creativeEnergy |= getAttributeMod(augmentData, TAG_AUGMENT_RF_CREATIVE) > 0;
        creativeTanks |= getAttributeMod(augmentData, TAG_AUGMENT_FLUID_CREATIVE) > 0;
        // creativeSlots |= getAttributeMod(augmentData, TAG_AUGMENT_ITEM_CREATIVE) > 0;
    }

    protected abstract void finalizeAttributes(Map<Enchantment, Integer> enchantmentMap);
    // endregion

    // region IStorageCallback
    @Override
    public void onInventoryChanged(int slot) {

        /* Implicit requirement here that augments always come LAST in slot order.
        This isn't a bad assumption/rule though, as it's a solid way to handle it.*/
        if (slot >= invSize() - augSize()) {
            updateAugmentState();
        }
    }
    // endregion

    // region IFilterable
    @Override
    public IFilter getFilter() {

        return filter;
    }

    @Override
    public void onFilterChanged() {

    }

    @Override
    public boolean hasGui() {

        return this instanceof MenuProvider;
    }

    @Override
    public boolean openGui(ServerPlayer player) {

        if (this instanceof MenuProvider) {
            Utils.openEntityScreen(player, (MenuProvider) this, this);
            return true;
        }
        return false;
    }

    @Override
    public boolean openFilterGui(ServerPlayer player) {

        if (filter instanceof MenuProvider provider) {
            FilterHelper.openEntityScreen(player, provider, this.getId());
            return true;
        }
        return false;
    }
    // endregion
}
