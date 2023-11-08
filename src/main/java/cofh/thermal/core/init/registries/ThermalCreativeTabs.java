package cofh.thermal.core.init.registries;

import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.RegistryObject;

import java.util.Collections;
import java.util.Comparator;
import java.util.Set;

import static cofh.lib.util.constants.ModIds.ID_THERMAL;
import static cofh.thermal.core.ThermalCore.CREATIVE_TABS;
import static cofh.thermal.core.ThermalCore.ITEMS;
import static cofh.thermal.lib.util.ThermalIDs.*;

public class ThermalCreativeTabs {

    private ThermalCreativeTabs() {

    }

    public static void register() {

    }

    private static final Set<Pair<RegistryObject<Item>, Integer>> BLOCKS_TAB = Collections.synchronizedSet(Sets.newLinkedHashSet());
    private static final Set<Pair<RegistryObject<Item>, Integer>> FOODS_TAB = Collections.synchronizedSet(Sets.newLinkedHashSet());
    private static final Set<Pair<RegistryObject<Item>, Integer>> DEVICES_TAB = Collections.synchronizedSet(Sets.newLinkedHashSet());
    private static final Set<Pair<RegistryObject<Item>, Integer>> ITEMS_TAB = Collections.synchronizedSet(Sets.newLinkedHashSet());
    private static final Set<Pair<RegistryObject<Item>, Integer>> TOOLS_TAB = Collections.synchronizedSet(Sets.newLinkedHashSet());

    public static RegistryObject<Item> blocksTab(RegistryObject<Item> reg) {

        return blocksTab(BLOCKS_TAB.size(), reg);
    }

    public static RegistryObject<Item> blocksTab(int order, RegistryObject<Item> reg) {

        BLOCKS_TAB.add(Pair.of(reg, order));
        return reg;
    }

    public static RegistryObject<Item> devicesTab(RegistryObject<Item> reg) {

        return devicesTab(DEVICES_TAB.size(), reg);
    }

    public static RegistryObject<Item> devicesTab(int order, RegistryObject<Item> reg) {

        DEVICES_TAB.add(Pair.of(reg, order));
        return reg;
    }

    public static RegistryObject<Item> foodsTab(RegistryObject<Item> reg) {

        return foodsTab(FOODS_TAB.size(), reg);
    }

    public static RegistryObject<Item> foodsTab(int order, RegistryObject<Item> reg) {

        FOODS_TAB.add(Pair.of(reg, order));
        return reg;
    }

    public static RegistryObject<Item> itemsTab(RegistryObject<Item> reg) {

        return itemsTab(ITEMS_TAB.size(), reg);
    }

    public static RegistryObject<Item> itemsTab(int order, RegistryObject<Item> reg) {

        ITEMS_TAB.add(Pair.of(reg, order));
        return reg;
    }

    public static RegistryObject<Item> toolsTab(RegistryObject<Item> reg) {

        return toolsTab(TOOLS_TAB.size(), reg);
    }

    public static RegistryObject<Item> toolsTab(int order, RegistryObject<Item> reg) {

        TOOLS_TAB.add(Pair.of(reg, order));
        return reg;
    }

    private static final Comparator<Pair<RegistryObject<Item>, Integer>> ORDER_COMPARISON = Comparator.comparing(Pair::getSecond);

    private static final Comparator<RegistryObject<Item>> MOD_ID_COMPARISON = (itemA, itemB) -> {

        String modA = itemA.get().getCreatorModId(new ItemStack(itemA.get()));
        String modB = itemB.get().getCreatorModId(new ItemStack(itemB.get()));
        return modA == null || modB == null ? 0 : modA.compareTo(modB);
    };

    private static final RegistryObject<CreativeModeTab> THERMAL_BLOCKS = CREATIVE_TABS.register(ID_THERMAL + ".blocks", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.thermal.blocks"))
            .icon(() -> new ItemStack(ITEMS.get(ID_ENDERIUM_BLOCK)))
            .displayItems((parameters, output) -> BLOCKS_TAB.stream().sorted(ORDER_COMPARISON).forEach((item) -> output.accept(item.getFirst().get())))
            .build());

    private static final RegistryObject<CreativeModeTab> THERMAL_DEVICES = CREATIVE_TABS.register(ID_THERMAL + ".devices", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.thermal.devices"))
            .icon(() -> new ItemStack(ITEMS.get(ID_TINKER_BENCH)))
            .displayItems((parameters, output) -> DEVICES_TAB.stream().sorted(ORDER_COMPARISON).forEach((item) -> output.accept(item.getFirst().get())))
            .build());

    private static final RegistryObject<CreativeModeTab> THERMAL_FOODS = CREATIVE_TABS.register(ID_THERMAL + ".foods", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.thermal.foods"))
            .icon(() -> new ItemStack(ITEMS.get(ID_APPLE_BLOCK)))
            .displayItems((parameters, output) -> FOODS_TAB.stream().sorted(ORDER_COMPARISON).forEach((item) -> output.accept(item.getFirst().get())))
            .build());

    private static final RegistryObject<CreativeModeTab> THERMAL_ITEMS = CREATIVE_TABS.register(ID_THERMAL + ".items", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.thermal.items"))
            .icon(() -> new ItemStack(ITEMS.get("signalum_gear")))
            .displayItems((parameters, output) -> ITEMS_TAB.stream().sorted(ORDER_COMPARISON).forEach((item) -> output.accept(item.getFirst().get())))
            .build());

    private static final RegistryObject<CreativeModeTab> THERMAL_TOOLS = CREATIVE_TABS.register(ID_THERMAL + ".tools", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.thermal.tools"))
            .icon(() -> new ItemStack(ITEMS.get(ID_WRENCH)))
            .displayItems((parameters, output) -> TOOLS_TAB.stream().sorted(ORDER_COMPARISON).forEach((item) -> output.accept(item.getFirst().get())))
            .build());

}
