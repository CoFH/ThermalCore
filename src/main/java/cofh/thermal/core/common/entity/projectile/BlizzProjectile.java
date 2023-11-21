package cofh.thermal.core.common.entity.projectile;

import cofh.core.util.AreaUtils;
import cofh.lib.util.Utils;
import cofh.thermal.core.init.data.damage.TCoreDamageTypes;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.List;

import static cofh.core.init.CoreMobEffects.CHILLED;
import static cofh.core.init.CoreParticles.FROST;
import static cofh.thermal.core.init.registries.TCoreEntities.BLIZZ_PROJECTILE;

public class BlizzProjectile extends ElementalProjectile {

    private static final int CLOUD_DURATION = 20;
    public static float defaultDamage = 5.0F;
    public static int effectAmplifier = 0;
    public static int effectDuration = 100;
    public static int effectRadius = 2;

    public BlizzProjectile(EntityType<? extends AbstractHurtingProjectile> type, Level world) {

        super(type, world);
    }

    public BlizzProjectile(LivingEntity shooter, double accelX, double accelY, double accelZ, Level world) {

        super(BLIZZ_PROJECTILE.get(), shooter, accelX, accelY, accelZ, world);
    }

    public BlizzProjectile(double x, double y, double z, double accelX, double accelY, double accelZ, Level world) {

        super(BLIZZ_PROJECTILE.get(), x, y, z, accelX, accelY, accelZ, world);
    }

    @Override
    protected ParticleOptions getTrailParticle() {

        return ParticleTypes.ITEM_SNOWBALL;
    }

    @Override
    protected void onHit(HitResult result) {

        if (result.getType() == HitResult.Type.ENTITY) {
            Entity entity = ((EntityHitResult) result).getEntity();
            if (entity.isOnFire()) {
                entity.clearFire();
            }
            if (entity.hurt(this.damageSource(), getDamage(entity)) && entity instanceof LivingEntity living) {
                living.addEffect(new MobEffectInstance(CHILLED.get(), getEffectDuration(entity), getEffectAmplifier(entity), false, false));
            }
        } else if (result.getType() == HitResult.Type.BLOCK) {
            List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, new AABB(result.location, result.location.add(1, 1, 1)).inflate(effectRadius), EntitySelector.ENTITY_STILL_ALIVE);
            for (var living : entities) {
                if (living.isOnFire()) {
                    living.clearFire();
                }
                living.addEffect(new MobEffectInstance(CHILLED.get(), getEffectDuration(living), Math.min(getEffectAmplifier(living) - 1, 0), false, false));
            }
        }
        if (Utils.isServerWorld(level)) {
            if (effectRadius > 0) {
                AreaUtils.freezeNearbyGround(this, level, this.blockPosition(), effectRadius);
                AreaUtils.freezeSurfaceWater(this, level, this.blockPosition(), effectRadius, false);
                AreaUtils.freezeSurfaceLava(this, level, this.blockPosition(), effectRadius, false);
                makeAreaOfEffectCloud();
            }
            this.level.broadcastEntityEvent(this, (byte) 3);
            this.discard();
        }
    }

    private void makeAreaOfEffectCloud() {

        AreaEffectCloud cloud = new AreaEffectCloud(level, getX(), getY(), getZ());
        cloud.setRadius(1);
        cloud.setParticle(FROST.get());
        cloud.setDuration(CLOUD_DURATION);
        cloud.setWaitTime(0);
        cloud.setRadiusPerTick((effectRadius - cloud.getRadius()) / (float) cloud.getDuration());

        level.addFreshEntity(cloud);
    }

    protected DamageSource damageSource() {

        Entity owner = getOwner();
        return this.level.damageSources().source(TCoreDamageTypes.BLIZZ_PROJECTILE, this, owner == null ? this : owner);
    }

    // region HELPERS
    @Override
    public float getDamage(Entity target) {

        return target.fireImmune() ? defaultDamage + 3.0F : defaultDamage;
    }

    @Override
    public int getEffectAmplifier(Entity target) {

        return effectAmplifier;
    }

    @Override
    public int getEffectDuration(Entity target) {

        return effectDuration;
    }
    // endregion
}
