package cofh.thermal.core.init;

import cofh.thermal.core.world.ConfigPlacementFilter;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraftforge.registries.RegistryObject;

import static cofh.thermal.core.ThermalCore.PLACEMENT_MODIFIERS;

public class TCorePlacementModifiers {

    private TCorePlacementModifiers() {

    }

    public static void register() {

    }

    public static final RegistryObject<PlacementModifierType<ConfigPlacementFilter>> CONFIG_FILTER = PLACEMENT_MODIFIERS.register("config", () -> () -> ConfigPlacementFilter.CODEC);

}
