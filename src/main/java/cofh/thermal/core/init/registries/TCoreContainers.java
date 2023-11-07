package cofh.thermal.core.init.registries;

import cofh.thermal.core.common.inventory.ChargeBenchMenu;
import cofh.thermal.core.common.inventory.TinkerBenchMenu;
import cofh.thermal.core.common.inventory.device.*;
import cofh.thermal.core.common.inventory.storage.EnergyCellMenu;
import cofh.thermal.core.common.inventory.storage.FluidCellMenu;
import cofh.thermal.core.common.inventory.storage.SatchelMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.RegistryObject;

import static cofh.core.util.ProxyUtils.getClientPlayer;
import static cofh.core.util.ProxyUtils.getClientWorld;
import static cofh.thermal.core.ThermalCore.CONTAINERS;
import static cofh.thermal.lib.init.ThermalIDs.*;

public class TCoreContainers {

    private TCoreContainers() {

    }

    public static void register() {

        // CONTAINERS.register(ID_CHUNK_LOADER, () -> IForgeMenuType.create((windowId, inv, data) -> new ChunkLoaderContainer(windowId, ProxyUtils.getClientWorld(), data.readBlockPos(), inv, ProxyUtils.getClientPlayer())));
        // CONTAINERS.register(ID_ITEM_CELL, () -> IForgeMenuType.create((windowId, inv, data) -> new ItemCellContainer(windowId, ProxyUtils.getClientWorld(), data.readBlockPos(), inv, ProxyUtils.getClientPlayer())));
    }

    public static final RegistryObject<MenuType<DeviceHiveExtractorMenu>> DEVICE_HIVE_EXTRACTOR_CONTAINER = CONTAINERS.register(ID_DEVICE_HIVE_EXTRACTOR, () -> IForgeMenuType.create((windowId, inv, data) -> new DeviceHiveExtractorMenu(windowId, getClientWorld(), data.readBlockPos(), inv, getClientPlayer())));
    public static final RegistryObject<MenuType<DeviceTreeExtractorMenu>> DEVICE_TREE_EXTRACTOR_CONTAINER = CONTAINERS.register(ID_DEVICE_TREE_EXTRACTOR, () -> IForgeMenuType.create((windowId, inv, data) -> new DeviceTreeExtractorMenu(windowId, getClientWorld(), data.readBlockPos(), inv, getClientPlayer())));
    public static final RegistryObject<MenuType<DeviceFisherMenu>> DEVICE_FISHER_CONTAINER = CONTAINERS.register(ID_DEVICE_FISHER, () -> IForgeMenuType.create((windowId, inv, data) -> new DeviceFisherMenu(windowId, getClientWorld(), data.readBlockPos(), inv, getClientPlayer())));
    public static final RegistryObject<MenuType<DeviceComposterMenu>> DEVICE_COMPOSTER_CONTAINER = CONTAINERS.register(ID_DEVICE_COMPOSTER, () -> IForgeMenuType.create((windowId, inv, data) -> new DeviceComposterMenu(windowId, getClientWorld(), data.readBlockPos(), inv, getClientPlayer())));
    public static final RegistryObject<MenuType<DeviceSoilInfuserMenu>> DEVICE_SOIL_INFUSER_CONTAINER = CONTAINERS.register(ID_DEVICE_SOIL_INFUSER, () -> IForgeMenuType.create((windowId, inv, data) -> new DeviceSoilInfuserMenu(windowId, getClientWorld(), data.readBlockPos(), inv, getClientPlayer())));
    public static final RegistryObject<MenuType<DeviceWaterGenMenu>> DEVICE_WATER_GEN_CONTAINER = CONTAINERS.register(ID_DEVICE_WATER_GEN, () -> IForgeMenuType.create((windowId, inv, data) -> new DeviceWaterGenMenu(windowId, getClientWorld(), data.readBlockPos(), inv, getClientPlayer())));
    public static final RegistryObject<MenuType<DeviceRockGenMenu>> DEVICE_ROCK_GEN_CONTAINER = CONTAINERS.register(ID_DEVICE_ROCK_GEN, () -> IForgeMenuType.create((windowId, inv, data) -> new DeviceRockGenMenu(windowId, getClientWorld(), data.readBlockPos(), inv, getClientPlayer())));
    public static final RegistryObject<MenuType<DeviceCollectorMenu>> DEVICE_COLLECTOR_CONTAINER = CONTAINERS.register(ID_DEVICE_COLLECTOR, () -> IForgeMenuType.create((windowId, inv, data) -> new DeviceCollectorMenu(windowId, getClientWorld(), data.readBlockPos(), inv, getClientPlayer())));
    public static final RegistryObject<MenuType<DeviceXpCondenserMenu>> DEVICE_XP_CONDENSER_CONTAINER = CONTAINERS.register(ID_DEVICE_XP_CONDENSER, () -> IForgeMenuType.create((windowId, inv, data) -> new DeviceXpCondenserMenu(windowId, getClientWorld(), data.readBlockPos(), inv, getClientPlayer())));
    public static final RegistryObject<MenuType<DevicePotionDiffuserMenu>> DEVICE_POTION_DIFFUSER_CONTAINER = CONTAINERS.register(ID_DEVICE_POTION_DIFFUSER, () -> IForgeMenuType.create((windowId, inv, data) -> new DevicePotionDiffuserMenu(windowId, getClientWorld(), data.readBlockPos(), inv, getClientPlayer())));
    public static final RegistryObject<MenuType<DeviceNullifierMenu>> DEVICE_NULLIFIER_CONTAINER = CONTAINERS.register(ID_DEVICE_NULLIFIER, () -> IForgeMenuType.create((windowId, inv, data) -> new DeviceNullifierMenu(windowId, getClientWorld(), data.readBlockPos(), inv, getClientPlayer())));

    public static final RegistryObject<MenuType<TinkerBenchMenu>> TINKER_BENCH_CONTAINER = CONTAINERS.register(ID_TINKER_BENCH, () -> IForgeMenuType.create((windowId, inv, data) -> new TinkerBenchMenu(windowId, getClientWorld(), data.readBlockPos(), inv, getClientPlayer())));
    public static final RegistryObject<MenuType<ChargeBenchMenu>> CHARGE_BENCH_CONTAINER = CONTAINERS.register(ID_CHARGE_BENCH, () -> IForgeMenuType.create((windowId, inv, data) -> new ChargeBenchMenu(windowId, getClientWorld(), data.readBlockPos(), inv, getClientPlayer())));

    public static final RegistryObject<MenuType<EnergyCellMenu>> ENERGY_CELL_CONTAINER = CONTAINERS.register(ID_ENERGY_CELL, () -> IForgeMenuType.create((windowId, inv, data) -> new EnergyCellMenu(windowId, getClientWorld(), data.readBlockPos(), inv, getClientPlayer())));
    public static final RegistryObject<MenuType<FluidCellMenu>> FLUID_CELL_CONTAINER = CONTAINERS.register(ID_FLUID_CELL, () -> IForgeMenuType.create((windowId, inv, data) -> new FluidCellMenu(windowId, getClientWorld(), data.readBlockPos(), inv, getClientPlayer())));

    public static final RegistryObject<MenuType<SatchelMenu>> SATCHEL_CONTAINER = CONTAINERS.register(ID_SATCHEL, () -> IForgeMenuType.create((windowId, inv, data) -> new SatchelMenu(windowId, inv, getClientPlayer())));

}
