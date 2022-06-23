package cofh.thermal.core.config;

import cofh.core.config.IBaseConfig;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.function.Supplier;

import static cofh.lib.util.Constants.TRUE_SUP;

public class ThermalClientConfig implements IBaseConfig {

    @Override
    public void apply(ForgeConfigSpec.Builder builder) {

        builder.push("Sounds");

        blockAmbientSounds = builder
                .comment("If TRUE, some 'Thermal Series' Blocks will have ambient sounds.")
                .define("Ambient Block Sounds", true);

        mobAmbientSounds = builder
                .comment("If TRUE, some 'Thermal Series' Mobs will have ambient sounds.")
                .define("Ambient Mob Sounds", true);

        builder.pop();
    }

    // region VARIABLES
    public static Supplier<Boolean> jeiBucketTanks = TRUE_SUP;

    public static Supplier<Boolean> blockAmbientSounds = TRUE_SUP;
    public static Supplier<Boolean> mobAmbientSounds = TRUE_SUP;
    // endregion
}
