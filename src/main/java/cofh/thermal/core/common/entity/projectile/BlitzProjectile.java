package cofh.thermal.core.common.entity.projectile;

import cofh.core.client.particle.options.BiColorParticleOptions;
import cofh.core.init.CoreParticles;
import cofh.lib.util.Utils;
import cofh.thermal.core.common.config.ThermalCoreConfig;
import cofh.thermal.core.init.data.damage.TCoreDamageTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import static cofh.core.init.CoreMobEffects.SHOCKED;
import static cofh.thermal.core.init.registries.TCoreEntities.BLITZ_PROJECTILE;

public class BlitzProjectile extends ElementalProjectile {

    public BlitzProjectile(EntityType<? extends AbstractHurtingProjectile> type, Level world) {

        super(type, world);
    }

    public BlitzProjectile(LivingEntity shooter, double accelX, double accelY, double accelZ, Level world) {

        super(BLITZ_PROJECTILE.get(), shooter, accelX, accelY, accelZ, world);
    }

    public BlitzProjectile(double x, double y, double z, double accelX, double accelY, double accelZ, Level world) {

        super(BLITZ_PROJECTILE.get(), x, y, z, accelX, accelY, accelZ, world);
    }

    @Override
    protected ParticleOptions getTrailParticle() {

        return ParticleTypes.INSTANT_EFFECT;
    }

    @Override
    protected void onHit(HitResult result) {

        super.onHit(result);
        this.discard();
        if (Utils.isServerWorld(level)) {
            Vec3 loc = result.getLocation();
            if (ThermalCoreConfig.mobBlitzLightning.get()) {
                BlockPos pos = BlockPos.containing(loc);
                if (level.canSeeSky(pos) && random.nextFloat() < (level.isRainingAt(pos) ? (level.isThundering() ? 0.2F : 0.1F) : 0.04F)) {
                    Utils.spawnLightningBolt(level, pos);
                    return;
                }
            }
            ServerLevel serverLevel = (ServerLevel) level;
            serverLevel.sendParticles(new BiColorParticleOptions(CoreParticles.STRAIGHT_ARC.get(),
                    0.3F, 8, 0, 0xFFFFFFFF, 0xFFFC52A4), loc.x, loc.y + 4.9, loc.z, 0, loc.x, loc.y, loc.z, 1.0F);
            serverLevel.sendParticles(CoreParticles.PLASMA.get(), loc.x, loc.y + 5.0, loc.z, 0, 0, 0, 0, 1.0F);
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {

        Entity entity = result.getEntity();
        if (entity.hurt(this.damageSource(), getDamage(entity)) && entity instanceof LivingEntity living) {
            living.addEffect(new MobEffectInstance(SHOCKED.get(), getEffectDuration(entity), getEffectAmplifier(entity), false, false));
        }
    }

    @Override
    protected float getInertia() {

        return 1.0F;
    }

    // region HELPERS
    @Override
    protected ResourceKey<DamageType> getDamageType() {

        return TCoreDamageTypes.BLITZ_PROJECTILE;
    }

    @Override
    protected float getDamage(Entity target) {

        return target.isInWaterOrRain() ? 8.0F : 5.0F;
    }
    // endregion
}
