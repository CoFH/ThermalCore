package cofh.thermal.core.data;

import cofh.thermal.core.data.providers.*;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static cofh.lib.util.constants.ModIds.ID_THERMAL;

@Mod.EventBusSubscriber (bus = Mod.EventBusSubscriber.Bus.MOD, modid = ID_THERMAL)
public class TCoreDataGen {

    @SubscribeEvent
    public static void gatherData(final GatherDataEvent event) {

        DataGenerator gen = event.getGenerator();
        PackOutput output = gen.getPackOutput();
        ExistingFileHelper exFileHelper = event.getExistingFileHelper();

        TCoreTagsProvider.Block blockTags = new TCoreTagsProvider.Block(output, event.getLookupProvider(), exFileHelper);
        gen.addProvider(event.includeServer(), blockTags);
        gen.addProvider(event.includeServer(), new TCoreTagsProvider.Item(output, event.getLookupProvider(), blockTags.contentsGetter(), exFileHelper));
        gen.addProvider(event.includeServer(), new TCoreTagsProvider.Fluid(output, event.getLookupProvider(), exFileHelper));
        gen.addProvider(event.includeServer(), new TCoreTagsProvider.Entity(output, event.getLookupProvider(), exFileHelper));

        // gen.addProvider(event.includeServer(), new TCoreAdvancementProvider(gen));
        gen.addProvider(event.includeServer(), new TCoreLootTableProvider(output));
        gen.addProvider(event.includeServer(), new TCoreRecipeProvider(output));

        gen.addProvider(event.includeClient(), new TCoreBlockStateProvider(output, exFileHelper));
        gen.addProvider(event.includeClient(), new TCoreItemModelProvider(output, exFileHelper));

        // RegistryOps<JsonElement> regOps = RegistryOps.create(JsonOps.INSTANCE, RegistryAccess.builtinCopy());

        // gen.addProvider(event.includeServer(), TCoreBiomeModifiers.dataGenBiomeModifiers(gen, exFileHelper, regOps));
    }

}
