package cofh.thermal.core.init.data.providers;

import cofh.lib.init.data.DatapackRegistryProviderCoFH;
import cofh.thermal.core.init.data.damage.TCoreDamageTypes;
import cofh.thermal.core.init.data.worldgen.TCoreBiomeModifiers;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.concurrent.CompletableFuture;

import static cofh.lib.util.constants.ModIds.ID_THERMAL;

public class TCoreDatapackRegistryProvider extends DatapackRegistryProviderCoFH {

    public static final RegistrySetBuilder REGISTRIES = new RegistrySetBuilder()
            .add(ForgeRegistries.Keys.BIOME_MODIFIERS, TCoreBiomeModifiers::init)
            .add(Registries.DAMAGE_TYPE, TCoreDamageTypes::init);

    public TCoreDatapackRegistryProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {

        super(output, provider, REGISTRIES, ID_THERMAL);
    }

}
