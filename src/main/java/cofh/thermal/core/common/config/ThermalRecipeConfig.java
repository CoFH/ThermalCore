package cofh.thermal.core.common.config;

import cofh.core.common.config.IBaseConfig;
import cofh.thermal.core.util.managers.machine.*;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.function.Supplier;

import static cofh.thermal.lib.util.ThermalFlags.getFlag;
import static cofh.thermal.lib.util.ThermalIDs.*;

public class ThermalRecipeConfig implements IBaseConfig {

    @Override
    public void apply(ForgeConfigSpec.Builder builder) {

        builder.push("Recipes");

        float defaultScale = 1.0F;
        float minScale = 0.0625F;
        float maxScale = 16.0F;

        if (getFlag(ID_MACHINE_FURNACE).get()) {
            builder.push("Furnace");

            furnaceEnergyScale = builder
                    .comment("This sets the recipe energy multiplier for the Redstone Furnace. This scales all recipe energy requirements.")
                    .defineInRange("Energy Multiplier", defaultScale, minScale, maxScale);

            builder.pop();
        }
        if (getFlag(ID_MACHINE_SAWMILL).get()) {
            builder.push("Sawmill");

            sawmillEnergyScale = builder
                    .comment("This sets the recipe energy multiplier for the Sawmill. This scales all recipe energy requirements.")
                    .defineInRange("Energy Multiplier", defaultScale, minScale, maxScale);

            builder.pop();
        }
        if (getFlag(ID_MACHINE_PULVERIZER).get()) {
            builder.push("Pulverizer");

            pulverizerEnergyScale = builder
                    .comment("This sets the recipe energy multiplier for the Pulverizer. This scales all recipe energy requirements.")
                    .defineInRange("Energy Multiplier", defaultScale, minScale, maxScale);

            builder.pop();
        }
        if (getFlag(ID_MACHINE_SMELTER).get()) {
            builder.push("Smelter");

            smelterEnergyScale = builder
                    .comment("This sets the recipe energy multiplier for the Induction Smelter. This scales all recipe energy requirements.")
                    .defineInRange("Energy Multiplier", defaultScale, minScale, maxScale);

            builder.pop();
        }
        if (getFlag(ID_MACHINE_INSOLATOR).get()) {
            builder.push("Insolator");

            insolatorEnergyScale = builder
                    .comment("This sets the recipe energy multiplier for the Phytogenic Insolator. This scales all recipe energy requirements.")
                    .defineInRange("Energy Multiplier", defaultScale, minScale, maxScale);

            builder.pop();
        }
        if (getFlag(ID_MACHINE_CENTRIFUGE).get()) {
            builder.push("Centrifuge");

            centrifugeEnergyScale = builder
                    .comment("This sets the recipe energy multiplier for the Centrifugal Seperator. This scales all recipe energy requirements.")
                    .defineInRange("Energy Multiplier", defaultScale, minScale, maxScale);

            builder.pop();
        }
        if (getFlag(ID_MACHINE_PRESS).get()) {
            builder.push("Press");

            pressEnergyScale = builder
                    .comment("This sets the recipe energy multiplier for the Multiservo Press. This scales all recipe energy requirements.")
                    .defineInRange("Energy Multiplier", defaultScale, minScale, maxScale);

            builder.pop();
        }
        if (getFlag(ID_MACHINE_CRUCIBLE).get()) {
            builder.push("Crucible");

            crucibleEnergyScale = builder
                    .comment("This sets the recipe energy multiplier for the Magma Crucible. This scales all recipe energy requirements.")
                    .defineInRange("Energy Multiplier", defaultScale, minScale, maxScale);

            builder.pop();
        }
        if (getFlag(ID_MACHINE_CHILLER).get()) {
            builder.push("Chiller");

            chillerEnergyScale = builder
                    .comment("This sets the recipe energy multiplier for the Blast Chiller. This scales all recipe energy requirements.")
                    .defineInRange("Energy Multiplier", defaultScale, minScale, maxScale);

            builder.pop();
        }
        if (getFlag(ID_MACHINE_REFINERY).get()) {
            builder.push("Refinery");

            refineryEnergyScale = builder
                    .comment("This sets the recipe energy multiplier for the Fractionating Still. This scales all recipe energy requirements.")
                    .defineInRange("Energy Multiplier", defaultScale, minScale, maxScale);

            builder.pop();
        }
        if (getFlag(ID_MACHINE_PYROLYZER).get()) {
            builder.push("Pyrolyzer");

            pyrolyzerEnergyScale = builder
                    .comment("This sets the recipe energy multiplier for the Pyrolyzer. This scales all recipe energy requirements.")
                    .defineInRange("Energy Multiplier", defaultScale, minScale, maxScale);

            builder.pop();
        }
        if (getFlag(ID_MACHINE_BOTTLER).get()) {
            builder.push("Bottler");

            bottlerDefaultBucketRecipes = builder
                    .comment("If TRUE, Bucket handling recipes will be automatically created for the Fluid Encapsulator.")
                    .define("Default Bucket Recipes", true);

            bottlerDefaultPotionRecipes = builder
                    .comment("If TRUE, Potion handling recipes will be automatically created for the Fluid Encapsulator.")
                    .define("Default Potion Recipes", true);

            bottlerEnergyScale = builder
                    .comment("This sets the recipe energy multiplier for the Fluid Encapsulator. This scales all recipe energy requirements.")
                    .defineInRange("Energy Multiplier", defaultScale, minScale, maxScale);

            builder.pop();
        }
        if (getFlag(ID_MACHINE_BREWER).get()) {
            builder.push("Brewer");

            brewerDefaultPotionRecipes = builder
                    .comment("If TRUE, default Potion recipes will be automatically created for the Alchemical Imbuer.")
                    .define("Default Potion Recipes", true);

            brewerEnergyScale = builder
                    .comment("This sets the recipe energy multiplier for the Alchemical Imbuer. This scales all recipe energy requirements.")
                    .defineInRange("Energy Multiplier", defaultScale, minScale, maxScale);

            builder.pop();
        }
        if (getFlag(ID_MACHINE_CRYSTALLIZER).get()) {
            builder.push("Brewer");

            crystallizerEnergyScale = builder
                    .comment("This sets the recipe energy multiplier for the Crystallizer. This scales all recipe energy requirements.")
                    .defineInRange("Energy Multiplier", defaultScale, minScale, maxScale);

            builder.pop();
        }
        if (getFlag(ID_MACHINE_CRAFTER).get()) {
            builder.push("Crafter");

            crafterEnergyScale = builder
                    .comment("This sets the recipe energy multiplier for the Sequential Fabricator. This scales all recipe energy requirements.")
                    .defineInRange("Energy Multiplier", defaultScale, minScale, maxScale);

            builder.pop();
        }
        builder.pop();
    }

    @Override
    public void refresh() {

        if (bottlerDefaultBucketRecipes != null) {
            BottlerRecipeManager.instance().setDefaultBucketRecipes(bottlerDefaultBucketRecipes.get());
        }
        if (bottlerDefaultPotionRecipes != null) {
            BottlerRecipeManager.instance().setDefaultPotionRecipes(bottlerDefaultPotionRecipes.get());
        }
        if (brewerDefaultPotionRecipes != null) {
            BrewerRecipeManager.instance().setDefaultPotionRecipes(brewerDefaultPotionRecipes.get());
        }

        if (furnaceEnergyScale != null) {
            FurnaceRecipeManager.instance().setDefaultScale(furnaceEnergyScale.get().floatValue());
        }
        if (sawmillEnergyScale != null) {
            SawmillRecipeManager.instance().setDefaultScale(sawmillEnergyScale.get().floatValue());
        }
        if (pulverizerEnergyScale != null) {
            PulverizerRecipeManager.instance().setDefaultScale(pulverizerEnergyScale.get().floatValue());
        }
        if (smelterEnergyScale != null) {
            SmelterRecipeManager.instance().setDefaultScale(smelterEnergyScale.get().floatValue());
        }
        if (insolatorEnergyScale != null) {
            InsolatorRecipeManager.instance().setDefaultScale(insolatorEnergyScale.get().floatValue());
        }
        if (centrifugeEnergyScale != null) {
            CentrifugeRecipeManager.instance().setDefaultScale(centrifugeEnergyScale.get().floatValue());
        }
        if (pressEnergyScale != null) {
            PressRecipeManager.instance().setDefaultScale(pressEnergyScale.get().floatValue());
        }
        if (crucibleEnergyScale != null) {
            CrucibleRecipeManager.instance().setDefaultScale(crucibleEnergyScale.get().floatValue());
        }
        if (chillerEnergyScale != null) {
            ChillerRecipeManager.instance().setDefaultScale(chillerEnergyScale.get().floatValue());
        }
        if (refineryEnergyScale != null) {
            RefineryRecipeManager.instance().setDefaultScale(refineryEnergyScale.get().floatValue());
        }
        if (pyrolyzerEnergyScale != null) {
            PyrolyzerRecipeManager.instance().setDefaultScale(pyrolyzerEnergyScale.get().floatValue());
        }
        if (bottlerEnergyScale != null) {
            BottlerRecipeManager.instance().setDefaultScale(bottlerEnergyScale.get().floatValue());
        }
        if (brewerEnergyScale != null) {
            BrewerRecipeManager.instance().setDefaultScale(brewerEnergyScale.get().floatValue());
        }
        if (crystallizerEnergyScale != null) {
            CrystallizerRecipeManager.instance().setDefaultScale(crystallizerEnergyScale.get().floatValue());
        }
        if (crafterEnergyScale != null) {
            CrafterRecipeManager.instance().setDefaultScale(crafterEnergyScale.get().floatValue());
        }
    }

    // region CONFIG VARIABLES
    private Supplier<Boolean> bottlerDefaultBucketRecipes;
    private Supplier<Boolean> bottlerDefaultPotionRecipes;
    private Supplier<Boolean> brewerDefaultPotionRecipes;

    private Supplier<Double> furnaceEnergyScale;
    private Supplier<Double> sawmillEnergyScale;
    private Supplier<Double> pulverizerEnergyScale;
    private Supplier<Double> smelterEnergyScale;
    private Supplier<Double> insolatorEnergyScale;
    private Supplier<Double> centrifugeEnergyScale;
    private Supplier<Double> pressEnergyScale;
    private Supplier<Double> crucibleEnergyScale;
    private Supplier<Double> chillerEnergyScale;
    private Supplier<Double> refineryEnergyScale;
    private Supplier<Double> pyrolyzerEnergyScale;
    private Supplier<Double> bottlerEnergyScale;
    private Supplier<Double> brewerEnergyScale;
    private Supplier<Double> crystallizerEnergyScale;
    private Supplier<Double> crafterEnergyScale;
    // endregion
}
