package cofh.thermal.core.data;

import cofh.thermal.core.init.TCoreEntities;
import cofh.thermal.lib.common.ThermalIDs;
import com.google.gson.JsonElement;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.JsonCodecProvider;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cofh.thermal.core.data.TCoreDatapackHelper.datapackProvider;
import static cofh.thermal.core.data.TCoreDatapackHelper.tagsOr;
import static cofh.thermal.core.data.TCoreDatapackHelper.holderSetIntersection;
import static cofh.thermal.core.data.TCoreDatapackHelper.holderSetUnion;
import static cofh.thermal.core.data.TCoreDatapackHelper.id;

public final class TCoreBiomeModifiers {
    public static JsonCodecProvider<BiomeModifier> dataGenBiomeModifiers(DataGenerator gen, ExistingFileHelper exFileHelper, RegistryOps<JsonElement> regOps) {
        return datapackProvider(gen, exFileHelper, regOps, ForgeRegistries.Keys.BIOME_MODIFIERS, generateBiomeModifiers(regOps.registryAccess));
    }

    private static Map<ResourceLocation, BiomeModifier> generateBiomeModifiers(RegistryAccess registryAccess) {
        Map<ResourceLocation, BiomeModifier> biomeModifierMap = new HashMap<>();

        generateBiomeMobSpawns(registryAccess.registryOrThrow(Registry.BIOME_REGISTRY), biomeModifierMap);
        generateBiomeOres(registryAccess.registryOrThrow(Registry.BIOME_REGISTRY), registryAccess.registryOrThrow(Registry.PLACED_FEATURE_REGISTRY), biomeModifierMap);

        return biomeModifierMap;
    }

    private static void generateBiomeMobSpawns(Registry<Biome> biomeRegistry, Map<ResourceLocation, BiomeModifier> map) {
        HolderSet<Biome> basalzBiomeSpawns = holderSetUnion(
                holderSetIntersection(
                        biomeRegistry.getOrCreateTag(BiomeTags.IS_OVERWORLD),
                        tagsOr(biomeRegistry, BiomeTags.IS_MOUNTAIN, Tags.Biomes.IS_MOUNTAIN, BiomeTags.IS_BADLANDS)
                ),
                holderSetIntersection(
                        biomeRegistry.getOrCreateTag(BiomeTags.IS_NETHER),
                        HolderSet.direct(Holder.Reference.createStandAlone(biomeRegistry, Biomes.BASALT_DELTAS))
                )
        );

        HolderSet<Biome> blitzBiomeSpawns = holderSetIntersection(
                biomeRegistry.getOrCreateTag(BiomeTags.IS_OVERWORLD),
                holderSetUnion(
                        HolderSet.direct(Holder.Reference.createStandAlone(biomeRegistry, Biomes.DESERT)),
                        tagsOr(biomeRegistry, BiomeTags.IS_BADLANDS, BiomeTags.IS_SAVANNA)
                )
        );

        HolderSet<Biome> blizzBiomeSpawns = biomeRegistry.getOrCreateTag(Tags.Biomes.IS_SNOWY);

        map.put(id("basalz_biome_spawns"), modifyMobIntoBiomeSpawns(basalzBiomeSpawns, TCoreEntities.BASALZ.get(), 10, 1, 3));
        map.put(id("blitz_biome_spawns"), modifyMobIntoBiomeSpawns(blitzBiomeSpawns, TCoreEntities.BLITZ.get(), 10, 1, 3));
        map.put(id("blizz_biome_spawns"), modifyMobIntoBiomeSpawns(blizzBiomeSpawns, TCoreEntities.BLIZZ.get(), 10, 1, 3));
    }

    private static ForgeBiomeModifiers.AddSpawnsBiomeModifier modifyMobIntoBiomeSpawns(HolderSet<Biome> biomes, EntityType<?> type, int weight, int minCount, int maxCount) {
        return new ForgeBiomeModifiers.AddSpawnsBiomeModifier(biomes, List.of(new MobSpawnSettings.SpawnerData(type, weight, minCount, maxCount)));
    }

    private static void generateBiomeOres(Registry<Biome> biomeRegistry, Registry<PlacedFeature> placedFeatureRegistry, Map<ResourceLocation, BiomeModifier> map) {
        HolderSet<Biome> oilSandsBiomes = holderSetIntersection(
                biomeRegistry.getOrCreateTag(BiomeTags.IS_OVERWORLD),
                holderSetUnion(
                        HolderSet.direct(Holder.Reference.createStandAlone(biomeRegistry, Biomes.DESERT)),
                        tagsOr(biomeRegistry, BiomeTags.IS_BADLANDS)
                )
        );

        modifyOreIntoBiomeGen(map, ThermalIDs.ID_OIL_SAND, oilSandsBiomes, placedFeatureRegistry);
    }

    private static void modifyOreIntoBiomeGen(Map<ResourceLocation, BiomeModifier> map, String name, HolderSet<Biome> biomes, Registry<PlacedFeature> placedFeatureRegistry) {
        map.put(id(name + "_biome_spawns"), new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                biomes,
                HolderSet.direct(Holder.Reference.createStandAlone(placedFeatureRegistry, ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, id(name)))),
                GenerationStep.Decoration.UNDERGROUND_ORES
        ));
    }
}
