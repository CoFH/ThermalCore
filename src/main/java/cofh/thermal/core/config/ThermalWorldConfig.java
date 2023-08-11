package cofh.thermal.core.config;

import cofh.core.config.IBaseConfig;
import cofh.core.config.world.FeatureConfig;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Map;

public class ThermalWorldConfig implements IBaseConfig {

    protected static final Map<String, FeatureConfig> ORE_CONFIGS = new Object2ObjectOpenHashMap<>();

    public static void addOreConfig(String name, FeatureConfig config) {

        ORE_CONFIGS.put(name, config);
    }

    public static FeatureConfig getOreConfig(String name) {

        return ORE_CONFIGS.getOrDefault(name, FeatureConfig.EMPTY_CONFIG);
    }

    @Override
    public void apply(ForgeConfigSpec.Builder builder) {

        builder.push("World");

        builder.push("Ores");
        for (IBaseConfig config : ORE_CONFIGS.values()) {
            config.apply(builder);
        }
        builder.pop();

        builder.pop();
    }

    @Override
    public void refresh() {

        for (IBaseConfig config : ORE_CONFIGS.values()) {
            config.refresh();
        }
    }

}
