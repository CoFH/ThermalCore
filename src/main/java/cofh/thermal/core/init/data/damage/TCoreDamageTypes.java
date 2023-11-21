package cofh.thermal.core.init.data.damage;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DeathMessageType;

import static cofh.lib.util.constants.ModIds.ID_THERMAL;
import static net.minecraft.world.damagesource.DamageEffects.*;

public class TCoreDamageTypes {

    public static final ResourceKey<DamageType> BLITZ_PROJECTILE = createKey("blitz_projectile");
    public static final ResourceKey<DamageType> BLIZZ_PROJECTILE = createKey("blizz_projectile");
    public static final ResourceKey<DamageType> BASALZ_PROJECTILE = createKey("basalz_projectile");

    public static void init(BootstapContext<DamageType> context) {

        register(context, BLITZ_PROJECTILE, 0.1F);
        register(context, BLIZZ_PROJECTILE, 0.1F, FREEZING);
        register(context, BASALZ_PROJECTILE, 0.1F);
    }

    protected static Holder.Reference<DamageType> register(BootstapContext<DamageType> context, ResourceKey<DamageType> key, DamageScaling scaling, float exhaustion, DamageEffects effects, DeathMessageType message) {

        return context.register(key, new DamageType(key.location().getPath(), scaling, exhaustion, effects, message));
    }

    protected static Holder.Reference<DamageType> register(BootstapContext<DamageType> context, ResourceKey<DamageType> key, DamageScaling scaling, float exhaustion, DamageEffects effects) {

        return context.register(key, new DamageType(key.location().getPath(), scaling, exhaustion, effects));
    }

    protected static Holder.Reference<DamageType> register(BootstapContext<DamageType> context, ResourceKey<DamageType> key, float exhaustion, DamageEffects effects) {

        return context.register(key, new DamageType(key.location().getPath(), exhaustion, effects));
    }

    protected static Holder.Reference<DamageType> register(BootstapContext<DamageType> context, ResourceKey<DamageType> key, float exhaustion) {

        return context.register(key, new DamageType(key.location().getPath(), exhaustion));
    }

    protected static ResourceKey<DamageType> createKey(String id) {

        return ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(ID_THERMAL, id));
    }
}
