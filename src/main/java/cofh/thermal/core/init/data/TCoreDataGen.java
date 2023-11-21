package cofh.thermal.core.init.data;

import cofh.thermal.core.init.data.providers.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.CompletableFuture;

import static cofh.lib.util.constants.ModIds.ID_THERMAL;

@Mod.EventBusSubscriber (bus = Mod.EventBusSubscriber.Bus.MOD, modid = ID_THERMAL)
public class TCoreDataGen {

    @SubscribeEvent
    public static void gatherData(final GatherDataEvent event) {

        DataGenerator gen = event.getGenerator();
        PackOutput output = gen.getPackOutput();
        ExistingFileHelper exFileHelper = event.getExistingFileHelper();

        TCoreDatapackRegistryProvider datapackRegistry = new TCoreDatapackRegistryProvider(output, event.getLookupProvider());
        gen.addProvider(event.includeServer(), datapackRegistry);
        CompletableFuture<HolderLookup.Provider> lookup = datapackRegistry.getRegistryProvider();
        TCoreTagsProvider.Block blockTags = new TCoreTagsProvider.Block(output, lookup, exFileHelper);
        gen.addProvider(event.includeServer(), blockTags);
        gen.addProvider(event.includeServer(), new TCoreTagsProvider.Item(output, lookup, blockTags.contentsGetter(), exFileHelper));
        gen.addProvider(event.includeServer(), new TCoreTagsProvider.Fluid(output, lookup, exFileHelper));
        gen.addProvider(event.includeServer(), new TCoreTagsProvider.Entity(output, lookup, exFileHelper));
        gen.addProvider(event.includeServer(), new TCoreTagsProvider.DamageType(output, lookup, exFileHelper));

        // gen.addProvider(event.includeServer(), new TCoreAdvancementProvider(gen));
        gen.addProvider(event.includeServer(), new TCoreLootTableProvider(output));
        gen.addProvider(event.includeServer(), new TCoreRecipeProvider(output));

        gen.addProvider(event.includeClient(), new TCoreBlockStateProvider(output, exFileHelper));
        gen.addProvider(event.includeClient(), new TCoreItemModelProvider(output, exFileHelper));
    }

}
