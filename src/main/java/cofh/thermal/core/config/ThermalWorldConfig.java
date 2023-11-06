package cofh.thermal.core.config;

import cofh.core.config.IBaseConfig;
import cofh.core.config.world.FeatureConfig;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Map;

public class ThermalWorldConfig implements IBaseConfig {

    protected static final Map<String, FeatureConfig> FEATURE_CONFIGS = new Object2ObjectOpenHashMap<>();

    public static void addFeatureConfig(String name, FeatureConfig config) {

        FEATURE_CONFIGS.put(name, config);
    }

    public static FeatureConfig getFeatureConfig(String name) {

        return FEATURE_CONFIGS.getOrDefault(name, FeatureConfig.EMPTY_CONFIG);
    }

    @Override
    public void apply(ForgeConfigSpec.Builder builder) {

        builder.push("World");

        builder.push("Features");
        for (IBaseConfig config : FEATURE_CONFIGS.values()) {
            config.apply(builder);
        }
        builder.pop();

        builder.pop();
    }

    @Override
    public void refresh() {

        for (IBaseConfig config : FEATURE_CONFIGS.values()) {
            config.refresh();
        }
    }

}
