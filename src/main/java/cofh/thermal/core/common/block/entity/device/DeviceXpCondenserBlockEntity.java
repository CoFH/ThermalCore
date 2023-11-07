package cofh.thermal.core.common.block.entity.device;

import cofh.core.common.block.entity.ITileXpHandler;
import cofh.core.util.helpers.AugmentDataHelper;
import cofh.lib.api.block.entity.IAreaEffectTile;
import cofh.lib.api.block.entity.ITickableTile;
import cofh.lib.common.fluid.FluidStorageCoFH;
import cofh.thermal.core.common.config.ThermalCoreConfig;
import cofh.thermal.core.common.inventory.device.DeviceXpCondenserMenu;
import cofh.thermal.lib.common.block.entity.DeviceBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static cofh.core.init.CoreFluids.EXPERIENCE_FLUID;
import static cofh.core.util.helpers.AugmentableHelper.getAttributeMod;
import static cofh.lib.api.StorageGroup.OUTPUT;
import static cofh.lib.util.Constants.MB_PER_XP;
import static cofh.lib.util.Constants.TANK_MEDIUM;
import static cofh.lib.util.constants.NBTTags.*;
import static cofh.thermal.core.init.registries.TCoreBlockEntities.DEVICE_XP_CONDENSER_TILE;
import static cofh.thermal.lib.util.ThermalAugmentRules.createAllowValidator;

public class DeviceXpCondenserBlockEntity extends DeviceBlockEntity implements ITickableTile.IServerTickable, IAreaEffectTile {

    public static final BiPredicate<ItemStack, List<ItemStack>> AUG_VALIDATOR = createAllowValidator(TAG_AUGMENT_TYPE_UPGRADE, TAG_AUGMENT_TYPE_FLUID, TAG_AUGMENT_TYPE_AREA_EFFECT);

    protected static final int TIME_CONSTANT = 400;
    protected static final Supplier<FluidStack> XP = () -> new FluidStack(EXPERIENCE_FLUID.get(), 0);

    protected static final int RADIUS = 2;
    public int radius = RADIUS;
    protected AABB area;

    protected FluidStorageCoFH tank = new FluidStorageCoFH(TANK_MEDIUM * 4, e -> false).setEmptyFluid(XP).setEnabled(() -> isActive);

    protected int process = 1;

    public DeviceXpCondenserBlockEntity(BlockPos pos, BlockState state) {

        super(DEVICE_XP_CONDENSER_TILE.get(), pos, state);

        tankInv.addTank(tank, OUTPUT);

        addAugmentSlots(ThermalCoreConfig.deviceAugments);
        initHandlers();
    }

    @Override
    protected void updateActiveState(boolean prevActive) {

        if (!prevActive && isActive) {
            process = 1;
        }
        super.updateActiveState(prevActive);
    }

    @Override
    public void tickServer() {

        updateActiveState();

        if (!isActive) {
            return;
        }
        --process;
        if (process > 0) {
            return;
        }
        process = getTimeConstant();
        if (tank.isFull()) {
            return;
        }
        BlockPos.betweenClosedStream(worldPosition.offset(-radius, -1, -radius), worldPosition.offset(radius, 1, radius))
                .forEach(this::condenseXp);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {

        return new DeviceXpCondenserMenu(i, level, worldPosition, inventory, player);
    }

    // region HELPERS
    public int getRadius() {

        return radius;
    }

    public int getTimeConstant() {

        return TIME_CONSTANT;
    }

    protected void condenseXp(BlockPos pos) {

        if (tank.isFull()) {
            return;
        }
        BlockEntity tile = level.getBlockEntity(pos);
        if (tile instanceof ITileXpHandler xpHandler) {
            int storedXp = xpHandler.getXpStorage().getXpStored();
            if (storedXp > 0) {
                int toExtract = Math.min(storedXp, tank.getSpace() / MB_PER_XP);
                tank.modify(xpHandler.getXpStorage().extractXp(toExtract, false) * MB_PER_XP);
            }
        }
    }
    // endregion

    // region AUGMENTS
    @Override
    protected Predicate<ItemStack> augValidator() {

        return item -> AugmentDataHelper.hasAugmentData(item) && AUG_VALIDATOR.test(item, getAugmentsAsList());
    }

    @Override
    protected void resetAttributes() {

        super.resetAttributes();

        radius = RADIUS;
    }

    @Override
    protected void setAttributesFromAugment(CompoundTag augmentData) {

        super.setAttributesFromAugment(augmentData);

        radius += getAttributeMod(augmentData, TAG_AUGMENT_RADIUS);
    }

    @Override
    protected void finalizeAttributes(Map<Enchantment, Integer> enchantmentMap) {

        super.finalizeAttributes(enchantmentMap);

        area = null;
    }
    // endregion

    // region IAreaEffectTile
    @Override
    public AABB getArea() {

        if (area == null) {
            area = new AABB(worldPosition.offset(-radius, -radius, -radius), worldPosition.offset(1 + radius, 1 + radius, 1 + radius));
        }
        return area;
    }

    @Override
    public int getColor() {

        return isActive ? 0x86D828 : 0x555555;
    }
    // endregion
}
