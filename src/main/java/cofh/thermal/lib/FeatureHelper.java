//package cofh.thermal.lib;
//
//import cofh.thermal.core.world.ConfigPlacementFilter;
//import net.minecraft.core.Holder;
//import net.minecraft.core.HolderSet;
//import net.minecraft.core.Registry;
//import net.minecraft.data.worldgen.placement.PlacementUtils;
//import net.minecraft.resources.ResourceKey;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.tags.BlockTags;
//import net.minecraft.world.entity.EntityType;
//import net.minecraft.world.level.biome.Biome;
//import net.minecraft.world.level.biome.MobSpawnSettings;
//import net.minecraft.world.level.block.Block;
//import net.minecraft.world.level.block.Blocks;
//import net.minecraft.world.level.levelgen.GenerationStep;
//import net.minecraft.world.level.levelgen.VerticalAnchor;
//import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
//import net.minecraft.world.level.levelgen.feature.Feature;
//import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
//import net.minecraft.world.level.levelgen.placement.*;
//import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
//import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
//import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
//import net.minecraftforge.common.world.ForgeBiomeModifiers;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//import static cofh.lib.util.constants.ModIds.ID_THERMAL;
//import static cofh.thermal.core.ThermalCore.BLOCKS;
//import static cofh.thermal.core.util.RegistrationHelper.deepslate;
//import static cofh.thermal.core.util.RegistrationHelper.netherrack;
//import static net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING_NO_LEAVES;
//
//public final class FeatureHelper {
//
//    private FeatureHelper() {
//
//    }
//
//    private static final RuleTest BASE_STONE_OVERWORLD_RULE = new TagMatchTest(BlockTags.BASE_STONE_OVERWORLD);
//    private static final RuleTest BASE_STONE_NETHER_RULE = new TagMatchTest(BlockTags.BASE_STONE_NETHER);
//
//    private static final RuleTest STONE_ORE_REPLACEABLE_RULE = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
//    private static final RuleTest DEEPSLATE_ORE_REPLACEABLE_RULE = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);
//    private static final RuleTest NETHERRACK_RULE = new BlockMatchTest(Blocks.NETHERRACK);
//
//    public static void createOreFeature(Map<ResourceLocation, PlacedFeature> featureMap, String name, int count, int minY, int maxY, int size) {
//
//        createOreFeature(featureMap, getOreReplacements(name), name, count, minY, maxY, size);
//    }
//
//    public static void createOreFeature(Map<ResourceLocation, PlacedFeature> featureMap, List<OreConfiguration.TargetBlockState> oreReplacements, String name, int count, int minY, int maxY, int size) {
//
//        featureMap.put(new ResourceLocation(ID_THERMAL, name), createOreFeature(oreReplacements, name, count, minY, maxY, size));
//    }
//
//    public static PlacedFeature createOreFeature(List<OreConfiguration.TargetBlockState> oreReplacements, String name, int count, int minY, int maxY, int size) {
//
//        return new PlacedFeature(Holder.direct(new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(oreReplacements, size))), List.of(
//                new ConfigPlacementFilter(name),
//                CountPlacement.of(count),
//                InSquarePlacement.spread(),
//                BiomeFilter.biome(),
//                HeightRangePlacement.triangle(VerticalAnchor.absolute(minY), VerticalAnchor.absolute(maxY))
//        ));
//    }
//
//    public static List<OreConfiguration.TargetBlockState> getOreReplacements(String oreName) {
//
//        final List<OreConfiguration.TargetBlockState> oreReplacements = new ArrayList<>();
//
//        Block ore = BLOCKS.get(oreName);
//        if (ore != null) {
//            oreReplacements.add(OreConfiguration.target(STONE_ORE_REPLACEABLE_RULE, ore.defaultBlockState()));
//        }
//
//        Block deepslateOre = BLOCKS.get(deepslate(oreName));
//        if (deepslateOre != null) {
//            oreReplacements.add(OreConfiguration.target(DEEPSLATE_ORE_REPLACEABLE_RULE, deepslateOre.defaultBlockState()));
//        }
//
//        Block netherrackOre = BLOCKS.get(netherrack(oreName));
//        if (netherrackOre != null) {
//            oreReplacements.add(OreConfiguration.target(NETHERRACK_RULE, netherrackOre.defaultBlockState()));
//        }
//
//        return oreReplacements;
//    }
//
//    public static void createTreeFeature(Map<ResourceLocation, PlacedFeature> featureMap, Registry<ConfiguredFeature<?, ?>> configuredRegistry, String name, Block sapling, int rarity) {
//
//        featureMap.put(new ResourceLocation(ID_THERMAL, name), createTreeFeature(Holder.Reference.createStandAlone(configuredRegistry, ResourceKey.create(Registry.CONFIGURED_FEATURE_REGISTRY, new ResourceLocation(ID_THERMAL, name))), name, sapling, rarity));
//    }
//
//    public static PlacedFeature createTreeFeature(Holder<ConfiguredFeature<?, ?>> tree, String name, Block sapling, int rarity) {
//
//        return new PlacedFeature(tree, List.of(
//                //new ConfigPlacementFilter(name), // TODO
//                RarityFilter.onAverageOnceEvery(rarity),
//                InSquarePlacement.spread(),
//                HeightmapPlacement.onHeightmap(MOTION_BLOCKING_NO_LEAVES),
//                BiomeFilter.biome(),
//                PlacementUtils.filteredByBlockSurvival(sapling)
//        ));
//    }
//
//    public static ForgeBiomeModifiers.AddSpawnsBiomeModifier addMobToBiomes(HolderSet<Biome> biomes, EntityType<?> type, int weight, int minCount, int maxCount) {
//
//        return new ForgeBiomeModifiers.AddSpawnsBiomeModifier(biomes, List.of(new MobSpawnSettings.SpawnerData(type, weight, minCount, maxCount)));
//    }
//
//    public static ForgeBiomeModifiers.AddFeaturesBiomeModifier addFeatureToBiomes(String name, HolderSet<Biome> biomes, Registry<PlacedFeature> placedFeatureRegistry, GenerationStep.Decoration decStep) {
//
//        return new ForgeBiomeModifiers.AddFeaturesBiomeModifier(biomes, HolderSet.direct(Holder.Reference.createStandAlone(placedFeatureRegistry, ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, new ResourceLocation(ID_THERMAL, name)))), decStep);
//    }
//
//    public static ForgeBiomeModifiers.AddFeaturesBiomeModifier addFeaturesToBiomes(List<String> names, HolderSet<Biome> biomes, Registry<PlacedFeature> placedFeatureRegistry, GenerationStep.Decoration decStep) {
//
//        return new ForgeBiomeModifiers.AddFeaturesBiomeModifier(biomes, HolderSet.direct(name -> Holder.Reference.createStandAlone(placedFeatureRegistry, ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, new ResourceLocation(ID_THERMAL, name))), names), decStep);
//    }
//
//}
