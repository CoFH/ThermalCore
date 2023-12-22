package cofh.thermal.core.common.entity.projectile;

import cofh.core.util.helpers.FluidHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.network.NetworkHooks;

import static cofh.lib.util.Constants.BUCKET_VOLUME;
import static cofh.lib.util.constants.NBTTags.TAG_FLUID;
import static cofh.thermal.core.ThermalCore.ITEMS;
import static cofh.thermal.core.init.registries.TCoreEntities.THROWN_FLORB;
import static cofh.thermal.lib.util.ThermalIDs.ID_FLORB;
import static net.minecraft.core.Direction.UP;
import static net.minecraft.world.InteractionHand.MAIN_HAND;

public class ThrownFlorb extends ThrowableItemProjectile {

    protected float gravity = 0.03F;

    public ThrownFlorb(EntityType<? extends ThrowableItemProjectile> pEntityType, Level pLevel) {

        super(pEntityType, pLevel);
    }

    public ThrownFlorb(Level pLevel, LivingEntity pShooter) {

        super(THROWN_FLORB.get(), pShooter, pLevel);
    }

    public ThrownFlorb(Level pLevel, double pX, double pY, double pZ) {

        super(THROWN_FLORB.get(), pX, pY, pZ, pLevel);
    }

    public void setItem(ItemStack pStack) {

        super.setItem(pStack);
        setGravity();
    }

    protected void setGravity() {

        this.gravity = FluidHelper.density(getFluid(getItem())) <= 0 ? 0.01F : 0.03F;
    }

    @Override
    protected Item getDefaultItem() {

        return ITEMS.get(ID_FLORB);
    }

    @Override
    protected void onHit(HitResult result) {

        //        System.out.println(result.location);
        //        System.out.println(BlockPos.containing(result.location));

        if (!level.isClientSide) {
            FluidStack fluid = getFluid(getItem());
            if (!fluid.isEmpty()) {
                BlockPos hitPos = blockPosition();
                Direction hitDir = UP;
                if (result instanceof BlockHitResult blockHitResult) {
                    hitPos = blockHitResult.getBlockPos();
                    hitDir = blockHitResult.getDirection();
                } else if (result instanceof EntityHitResult entityHitResult) {
                    hitPos = entityHitResult.getEntity().getOnPos();
                }
                FluidActionResult actionResult = FluidUtil.tryPlaceFluid(getOwner() instanceof Player player ? player : null, this.level, MAIN_HAND, hitPos.relative(hitDir), getItem(), new FluidStack(getFluid(getItem()), BUCKET_VOLUME));
            }
            this.level.broadcastEntityEvent(this, (byte) 3);
            this.discard();
        }
    }

    @Override
    public void handleEntityEvent(byte event) {

        if (event == 3) {
            // level.addParticle(new CylindricalParticleOptions(BLAST_WAVE.get(), radius * 2.0F, radius * 3.0F, 2.5F), this.getX(), this.getY(), this.getZ(), 0, 0, 0);
            level.addParticle(ParticleTypes.BUBBLE_POP, this.getX(), this.getY(), this.getZ(), 1.0D, 0.0D, 0.0D);
            level.playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.GENERIC_SPLASH, SoundSource.BLOCKS, 0.5F, (1.0F + (this.level.random.nextFloat() - this.level.random.nextFloat()) * 0.2F) * 0.7F, false);
        } else {
            super.handleEntityEvent(event);
        }
    }

    protected boolean tryPlaceFluid(Level level, BlockPos pos) {

        return false;
    }

    @Override
    protected float getGravity() {

        return gravity;
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {

        return NetworkHooks.getEntitySpawningPacket(this);
    }

    // region HELPERS
    public static FluidStack getFluid(ItemStack container) {

        CompoundTag tag = container.getOrCreateTag();
        if (!tag.contains(TAG_FLUID)) {
            return FluidStack.EMPTY;
        }
        return FluidStack.loadFluidStackFromNBT(tag.getCompound(TAG_FLUID));
    }
    // endregion
}
