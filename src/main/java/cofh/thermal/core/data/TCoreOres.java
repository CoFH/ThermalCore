package cofh.thermal.core.data;

import cofh.core.config.world.OreConfig;
import cofh.thermal.core.ThermalCore;
import cofh.thermal.lib.common.ThermalIDs;
import com.google.gson.JsonElement;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.JsonCodecProvider;

import java.util.*;
import java.util.function.Supplier;

import static cofh.thermal.core.data.TCoreDatapackHelper.datapackProvider;
import static cofh.thermal.core.data.TCoreDatapackHelper.id;
import static cofh.thermal.core.util.RegistrationHelper.deepslate;
import static cofh.thermal.core.util.RegistrationHelper.netherrack;

public final class TCoreOres {
    public static final RuleTest SAND = new BlockMatchTest(Blocks.SAND);
    public static final RuleTest RED_SAND = new BlockMatchTest(Blocks.RED_SAND);

    public static JsonCodecProvider<PlacedFeature> dataGenFeatures(DataGenerator gen, ExistingFileHelper exFileHelper, RegistryOps<JsonElement> regOps) {
        return datapackProvider(gen, exFileHelper, regOps, Registry.PLACED_FEATURE_REGISTRY, generateFeatures(regOps.registryAccess.registryOrThrow(Registry.PLACED_FEATURE_REGISTRY)));
    }

    private static Map<ResourceLocation, PlacedFeature> generateFeatures(Registry<PlacedFeature> featureRegistry) {
        Map<ResourceLocation, PlacedFeature> featureMap = new HashMap<>();

        generateOres(featureMap);

        return featureMap;
    }

    private static void generateOres(Map<ResourceLocation, PlacedFeature> featureMap) {
        List<ResourceKey<Level>> defaultDimensions = Collections.singletonList(Level.OVERWORLD);
        Supplier<Boolean> yes = () -> true;

        //FIXME Enable when ores are added
        // createOre(featureMap, ThermalIDs.ID_NITER_ORE, 2, -16, 64, 7, defaultDimensions, yes);
        // createOre(featureMap, ThermalIDs.ID_SULFUR_ORE, 2, -16, 32, 7, defaultDimensions, yes);
        // createOre(featureMap, ThermalIDs.ID_TIN_ORE, 6, -20, 60, 9, defaultDimensions, yes);
        // createOre(featureMap, ThermalIDs.ID_LEAD_ORE, 6, -60, 40, 8, defaultDimensions, yes);
        // createOre(featureMap, ThermalIDs.ID_SILVER_ORE, 4, -60, 40, 8, defaultDimensions, yes);
        // createOre(featureMap, ThermalIDs.ID_NICKEL_ORE, 4, -40, 120, 8, defaultDimensions, yes);
        // createOre(featureMap, ThermalIDs.ID_APATITE_ORE, 4, -16, 96, 9, defaultDimensions, yes);
        // createOre(featureMap, ThermalIDs.ID_CINNABAR_ORE, 1, -16, 48, 5, defaultDimensions, yes);
        // createOre(featureMap, List.of(
        //         OreConfiguration.target(new BlockMatchTest(Blocks.SAND), ThermalCore.BLOCKS.get("oil_sand").defaultBlockState()),
        //         OreConfiguration.target(new BlockMatchTest(Blocks.RED_SAND), ThermalCore.BLOCKS.get("oil_red_sand").defaultBlockState())
        // ), ThermalIDs.ID_OIL_SAND, new OreConfig(ThermalIDs.ID_OIL_SAND, 2, 40, 80, 24, defaultDimensions, yes));

        createOre(featureMap, List.of(
                OreConfiguration.target(new BlockMatchTest(Blocks.SAND), Blocks.BIRCH_WOOD.defaultBlockState()),
                OreConfiguration.target(new BlockMatchTest(Blocks.RED_SAND), Blocks.ACACIA_WOOD.defaultBlockState())
        ), ThermalIDs.ID_OIL_SAND, new OreConfig(ThermalIDs.ID_OIL_SAND, 2, 40, 80, 24, defaultDimensions, yes));
    }

    private static void createOre(Map<ResourceLocation, PlacedFeature> featureMap, String name, int count, int minY, int maxY, int size, List<ResourceKey<Level>> dimensions, Supplier<Boolean> enable) {
        //FIXME ThermalWorldConfig.getOreConfig is always returning its default EMPTY_CONFIG object
        // featureMap.put(id(name), createOre(name, ThermalWorldConfig.getOreConfig(name)));
        createOre(featureMap, getOreReplacements(name), name, new OreConfig(name, count, minY, maxY, size, dimensions, enable));
    }

    private static void createOre(Map<ResourceLocation, PlacedFeature> featureMap, List<OreConfiguration.TargetBlockState> oreReplacements, String name, OreConfig config) {
        featureMap.put(id(name), createOre(oreReplacements, config));
    }

    private static PlacedFeature createOre(List<OreConfiguration.TargetBlockState> oreReplacements, OreConfig config) {
        return new PlacedFeature(Holder.direct(new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(oreReplacements, config.getSize()))), List.of(
                CountPlacement.of(config.getCount()),
                InSquarePlacement.spread(),
                BiomeFilter.biome(),
                HeightRangePlacement.triangle(VerticalAnchor.absolute(config.getMinY()), VerticalAnchor.absolute(config.getMaxY()))
        ));
    }

    public static List<OreConfiguration.TargetBlockState> getOreReplacements(String oreName) {
        final List<OreConfiguration.TargetBlockState> oreReplacements = new ArrayList<>();

        Block ore = ThermalCore.BLOCKS.get(oreName);
        if (ore != null) {
            oreReplacements.add(OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, ore.defaultBlockState()));
        }

        Block deepslateOre = ThermalCore.BLOCKS.get(deepslate(oreName));
        if (deepslateOre != null) {
            oreReplacements.add(OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, deepslateOre.defaultBlockState()));
        }

        Block netherrackOre = ThermalCore.BLOCKS.get(netherrack(oreName));
        if (netherrackOre != null) {
            oreReplacements.add(OreConfiguration.target(OreFeatures.NETHERRACK, netherrackOre.defaultBlockState()));
        }

        return oreReplacements;
    }
}
