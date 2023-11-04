package cofh.thermal.lib.common;

import com.google.common.collect.Sets;
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
import static cofh.thermal.lib.common.ThermalIDs.*;

public class ThermalCreativeTabs {

    private ThermalCreativeTabs() {

    }

    public static void register() {

    }

    private static final Set<RegistryObject<Item>> BLOCKS_TAB = Collections.synchronizedSet(Sets.newLinkedHashSet());
    private static final Set<RegistryObject<Item>> FOODS_TAB = Collections.synchronizedSet(Sets.newLinkedHashSet());
    private static final Set<RegistryObject<Item>> DEVICES_TAB = Collections.synchronizedSet(Sets.newLinkedHashSet());
    private static final Set<RegistryObject<Item>> ITEMS_TAB = Collections.synchronizedSet(Sets.newLinkedHashSet());
    private static final Set<RegistryObject<Item>> TOOLS_TAB = Collections.synchronizedSet(Sets.newLinkedHashSet());

    public static RegistryObject<Item> blocksTab(RegistryObject<Item> reg) {

        BLOCKS_TAB.add(reg);
        return reg;
    }

    public static RegistryObject<Item> devicesTab(RegistryObject<Item> reg) {

        DEVICES_TAB.add(reg);
        return reg;
    }

    public static RegistryObject<Item> foodsTab(RegistryObject<Item> reg) {

        FOODS_TAB.add(reg);
        return reg;
    }

    public static RegistryObject<Item> itemsTab(RegistryObject<Item> reg) {

        ITEMS_TAB.add(reg);
        return reg;
    }

    public static RegistryObject<Item> toolsTab(RegistryObject<Item> reg) {

        TOOLS_TAB.add(reg);
        return reg;
    }

    public static final Comparator<RegistryObject<Item>> MOD_ID_COMPARISON = (itemA, itemB) -> {

        String modA = itemA.get().getCreatorModId(new ItemStack(itemA.get()));
        String modB = itemB.get().getCreatorModId(new ItemStack(itemB.get()));
        return modA == null || modB == null ? 0 : modA.compareTo(modB);
    };

    private static final RegistryObject<CreativeModeTab> THERMAL_BLOCKS = CREATIVE_TABS.register(ID_THERMAL + ".blocks", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.thermal.blocks"))
            .icon(() -> new ItemStack(ITEMS.get(ID_ENDERIUM_BLOCK)))
            .displayItems((parameters, output) -> BLOCKS_TAB.stream().sorted(MOD_ID_COMPARISON).forEach((item) -> output.accept(item.get())))
            .build());

    private static final RegistryObject<CreativeModeTab> THERMAL_DEVICES = CREATIVE_TABS.register(ID_THERMAL + ".devices", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.thermal.devices"))
            .icon(() -> new ItemStack(ITEMS.get(ID_TINKER_BENCH)))
            .displayItems((parameters, output) -> DEVICES_TAB.stream().sorted(MOD_ID_COMPARISON).forEach((item) -> output.accept(item.get())))
            .build());

    private static final RegistryObject<CreativeModeTab> THERMAL_FOODS = CREATIVE_TABS.register(ID_THERMAL + ".foods", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.thermal.foods"))
            .icon(() -> new ItemStack(ITEMS.get(ID_APPLE_BLOCK)))
            .displayItems((parameters, output) -> FOODS_TAB.stream().sorted(MOD_ID_COMPARISON).forEach((item) -> output.accept(item.get())))
            .build());

    private static final RegistryObject<CreativeModeTab> THERMAL_ITEMS = CREATIVE_TABS.register(ID_THERMAL + ".items", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.thermal.items"))
            .icon(() -> new ItemStack(ITEMS.get("signalum_gear")))
            .displayItems((parameters, output) -> ITEMS_TAB.stream().sorted(MOD_ID_COMPARISON).forEach((item) -> output.accept(item.get())))
            .build());

    private static final RegistryObject<CreativeModeTab> THERMAL_TOOLS = CREATIVE_TABS.register(ID_THERMAL + ".tools", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.thermal.tools"))
            .icon(() -> new ItemStack(ITEMS.get(ID_WRENCH)))
            .displayItems((parameters, output) -> TOOLS_TAB.stream().sorted(MOD_ID_COMPARISON).forEach((item) -> output.accept(item.get())))
            .build());

}
