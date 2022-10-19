package cofh.thermal.core.data;

import cofh.lib.util.constants.ModIds;
import com.google.gson.JsonElement;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.JsonCodecProvider;
import net.minecraftforge.registries.holdersets.AndHolderSet;
import net.minecraftforge.registries.holdersets.OrHolderSet;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static cofh.lib.util.constants.ModIds.ID_THERMAL;

public final class TCoreDatapackHelper {
    static <T> JsonCodecProvider<T> datapackProvider(DataGenerator dataGenerator, ExistingFileHelper existingFileHelper, RegistryOps<JsonElement> registryOps, ResourceKey<Registry<T>> registryKey, Map<ResourceLocation, T> entries) {
        return JsonCodecProvider.forDatapackRegistry(dataGenerator, existingFileHelper, ID_THERMAL, registryOps, registryKey, entries);
    }

    static <T> HolderSet<T> tagsAnd(Registry<T> tagGetter, TagKey<T>... biomes) {
        return holderSetIntersection(Arrays.stream(biomes).<HolderSet<T>>map(tagGetter::getOrCreateTag).toArray(i -> new HolderSet[i]));
    }

    static <T> HolderSet<T> tagsOr(Registry<T> tagGetter, TagKey<T>... biomes) {
        return holderSetUnion(Arrays.stream(biomes).<HolderSet<T>>map(tagGetter::getOrCreateTag).toArray(i -> new HolderSet[i]));
    }

    static <T> HolderSet<T> holderSetIntersection(HolderSet<T>... holderSets) {
        return new AndHolderSet<>(List.of(holderSets));
    }

    static <T> HolderSet<T> holderSetUnion(HolderSet<T>... holderSets) {
        return new OrHolderSet<>(List.of(holderSets));
    }

    static ResourceLocation id(String name) {
        return new ResourceLocation(ModIds.ID_THERMAL, name);
    }
}
