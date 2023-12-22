package cofh.thermal.core.common.item;

import cofh.core.common.item.FluidContainerItem;
import cofh.core.util.ProxyUtils;
import cofh.core.util.helpers.FluidHelper;
import cofh.lib.util.helpers.MathHelper;
import cofh.lib.util.helpers.StringHelper;
import cofh.thermal.core.common.entity.projectile.ThrownFlorb;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.AbstractProjectileDispenseBehavior;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static cofh.core.util.helpers.FluidHelper.addPotionTooltip;
import static cofh.core.util.helpers.ItemHelper.cloneStack;
import static cofh.lib.util.helpers.StringHelper.*;

public class FlorbItem extends FluidContainerItem {

    protected static int cooldown = 0;

    public FlorbItem(Properties builder, int fluidCapacity, Predicate<FluidStack> validator) {

        super(builder, fluidCapacity, validator);

        ProxyUtils.registerColorable(this);

        DispenserBlock.registerBehavior(this, DISPENSER_BEHAVIOR);
    }

    @Override
    protected void tooltipDelegate(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {

        FluidStack fluid = getFluid(stack);
        if (!fluid.isEmpty()) {
            tooltip.add(StringHelper.getFluidName(fluid));
        }
        if (FluidHelper.hasPotionTag(fluid)) {
            tooltip.add(getEmptyLine());
            tooltip.add(getTextComponent(localize("info.cofh.effects") + ":"));
            addPotionTooltip(fluid, tooltip);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {

        List<Component> additionalTooltips = new ArrayList<>();
        tooltipDelegate(stack, worldIn, additionalTooltips, flagIn);
        tooltip.addAll(additionalTooltips);

        //        if (SecurityHelper.isItemClaimable(stack)) {
        //            tooltip.add(getTextComponent("info.cofh.claimable").withStyle(GREEN).withStyle(ITALIC));
        //        }
        //        if (!additionalTooltips.isEmpty()) {
        //            if (Screen.hasShiftDown() || CoreClientConfig.alwaysShowDetails.get()) {
        //                tooltip.addAll(additionalTooltips);
        //            } else if (CoreClientConfig.holdShiftForDetails.get()) {
        //                tooltip.add(getTextComponent("info.cofh.hold_shift_for_details").withStyle(GRAY));
        //            }
        //        }
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {

        return false;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {

        ItemStack stack = playerIn.getItemInHand(handIn);
        if (getFluid(stack).isEmpty()) {
            return InteractionResultHolder.pass(playerIn.getItemInHand(handIn));
        }
        worldIn.playSound(null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), SoundEvents.SNOWBALL_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (MathHelper.RANDOM.nextFloat() * 0.4F + 0.8F));
        if (cooldown > 0) {
            playerIn.getCooldowns().addCooldown(this, cooldown);
        }
        if (!worldIn.isClientSide) {
            createFlorb(stack, worldIn, playerIn);
        }
        playerIn.awardStat(Stats.ITEM_USED.get(this));
        if (!playerIn.getAbilities().instabuild) {
            stack.shrink(1);
        }
        return InteractionResultHolder.sidedSuccess(stack, worldIn.isClientSide());
    }

    protected void createFlorb(ItemStack stack, Level world, Player player) {

        ThrownFlorb florb = new ThrownFlorb(world, player);
        ItemStack throwStack = cloneStack(stack, 1);
        throwStack.setDamageValue(1);
        florb.setItem(throwStack);
        florb.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 0.5F);
        world.addFreshEntity(florb);
    }

    // region IFluidContainerItem
    @Override
    public int fill(ItemStack container, FluidStack resource, FluidAction action) {

        if (!getFluid(container).isEmpty() || resource.getAmount() < getCapacity(container)) {
            return 0;
        }
        return super.fill(container, resource, action);
    }

    @Override
    public FluidStack drain(ItemStack container, int maxDrain, FluidAction action) {

        if (container.getDamageValue() <= 0) {
            return FluidStack.EMPTY;
        }
        return super.drain(container, maxDrain, action);
    }
    // endregion

    // region DISPENSER BEHAVIOR
    private static final AbstractProjectileDispenseBehavior DISPENSER_BEHAVIOR = new AbstractProjectileDispenseBehavior() {

        @Override
        public Projectile getProjectile(Level worldIn, Position position, ItemStack stackIn) {

            ThrownFlorb florb = new ThrownFlorb(worldIn, position.x(), position.y(), position.z());
            ItemStack throwStack = cloneStack(stackIn, 1);
            throwStack.setDamageValue(1);
            florb.setItem(throwStack);
            return florb;
        }

        @Override
        protected float getUncertainty() {

            return 3.0F;
        }
    };
    // endregion
}
