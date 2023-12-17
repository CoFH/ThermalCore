package cofh.thermal.core.common.entity.projectile;

import cofh.thermal.core.init.data.damage.TCoreDamageTypes;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceKey;
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

import static cofh.core.init.CoreMobEffects.SUNDERED;
import static cofh.thermal.core.init.registries.TCoreEntities.BASALZ_PROJECTILE;

public class BasalzProjectile extends ElementalProjectile {

    public static float knockbackStrength = 1.2F;

    public BasalzProjectile(EntityType<? extends AbstractHurtingProjectile> type, Level world) {

        super(type, world);
    }

    public BasalzProjectile(LivingEntity shooter, double accelX, double accelY, double accelZ, Level world) {

        super(BASALZ_PROJECTILE.get(), shooter, accelX, accelY, accelZ, world);
    }

    public BasalzProjectile(double x, double y, double z, double accelX, double accelY, double accelZ, Level world) {

        super(BASALZ_PROJECTILE.get(), x, y, z, accelX, accelY, accelZ, world);
    }

    @Override
    protected ParticleOptions getTrailParticle() {

        return ParticleTypes.FALLING_LAVA;
    }

    @Override
    public void onHit(HitResult result) {

        super.onHit(result);
        this.discard();
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {

        Entity entity = result.getEntity();
        if (entity.hurt(this.damageSource(), getDamage(entity)) && entity instanceof LivingEntity living) {
            living.addEffect(new MobEffectInstance(SUNDERED.get(), getEffectDuration(entity), getEffectAmplifier(entity), false, false));
            Vec3 velocity = this.getDeltaMovement();
            if (velocity.lengthSqr() > 0.01) {
                living.knockback(knockbackStrength, -velocity.x, -velocity.z);
            }
        }
    }

    // region HELPERS
    @Override
    protected ResourceKey<DamageType> getDamageType() {

        return TCoreDamageTypes.BASALZ_PROJECTILE;
    }

    @Override
    protected float getDamage(Entity target) {

        return 6.0F;
    }
    // endregion
}
