package cofh.thermal.core.init;

import cofh.thermal.core.entity.monster.Basalz;
import cofh.thermal.core.entity.monster.Blitz;
import cofh.thermal.core.entity.monster.Blizz;
import cofh.thermal.core.entity.projectile.BasalzProjectile;
import cofh.thermal.core.entity.projectile.BlitzProjectile;
import cofh.thermal.core.entity.projectile.BlizzProjectile;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.registries.RegistryObject;

import static cofh.thermal.core.ThermalCore.ENTITIES;
import static cofh.thermal.lib.common.ThermalIDs.*;

public class TCoreEntities {

    private TCoreEntities() {

    }

    public static void register() {

    }

    public static void setup() {

        SpawnPlacements.register(BASALZ.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Basalz::canSpawn);
        SpawnPlacements.register(BLITZ.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Blitz::canSpawn);
        SpawnPlacements.register(BLIZZ.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Blizz::canSpawn);
    }

    public static final RegistryObject<EntityType<Basalz>> BASALZ = ENTITIES.register(ID_BASALZ, () -> EntityType.Builder.of(Basalz::new, MobCategory.MONSTER).sized(0.6F, 1.8F).fireImmune().build(ID_BASALZ));
    public static final RegistryObject<EntityType<Blizz>> BLIZZ = ENTITIES.register(ID_BLIZZ, () -> EntityType.Builder.of(Blizz::new, MobCategory.MONSTER).sized(0.6F, 1.8F).build(ID_BLIZZ));
    public static final RegistryObject<EntityType<Blitz>> BLITZ = ENTITIES.register(ID_BLITZ, () -> EntityType.Builder.of(Blitz::new, MobCategory.MONSTER).sized(0.6F, 1.8F).build(ID_BLITZ));

    public static final RegistryObject<EntityType<BasalzProjectile>> BASALZ_PROJECTILE = ENTITIES.register(ID_BASALZ_PROJECTILE, () -> EntityType.Builder.<BasalzProjectile>of(BasalzProjectile::new, MobCategory.MISC).sized(0.3125F, 0.3125F).build(ID_BASALZ_PROJECTILE));
    public static final RegistryObject<EntityType<BlizzProjectile>> BLIZZ_PROJECTILE = ENTITIES.register(ID_BLIZZ_PROJECTILE, () -> EntityType.Builder.<BlizzProjectile>of(BlizzProjectile::new, MobCategory.MISC).sized(0.3125F, 0.3125F).build(ID_BLIZZ_PROJECTILE));
    public static final RegistryObject<EntityType<BlitzProjectile>> BLITZ_PROJECTILE = ENTITIES.register(ID_BLITZ_PROJECTILE, () -> EntityType.Builder.<BlitzProjectile>of(BlitzProjectile::new, MobCategory.MISC).sized(0.3125F, 0.3125F).build(ID_BLITZ_PROJECTILE));

}
