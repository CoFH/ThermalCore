package cofh.thermal.core.init;

import cofh.core.item.EnergyContainerItem;
import cofh.core.item.ItemCoFH;
import cofh.core.item.SpawnEggItemCoFH;
import cofh.core.util.filter.FilterRegistry;
import cofh.core.util.helpers.AugmentDataHelper;
import cofh.lib.block.TntBlockCoFH;
import cofh.lib.item.ArmorMaterialCoFH;
import cofh.thermal.core.item.*;
import cofh.thermal.lib.item.AugmentItem;
import cofh.thermal.lib.util.ThermalEnergyHelper;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.HoneyBottleItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.IEnergyStorage;

import static cofh.lib.util.constants.NBTTags.*;
import static cofh.thermal.core.ThermalCore.BLOCKS;
import static cofh.thermal.core.ThermalCore.ITEMS;
import static cofh.thermal.core.init.TCoreEntities.*;
import static cofh.thermal.core.util.RegistrationHelper.*;
import static cofh.thermal.lib.common.ThermalAugmentRules.flagUniqueAugment;
import static cofh.thermal.lib.common.ThermalCreativeTabs.*;
import static cofh.thermal.lib.common.ThermalIDs.*;
import static net.minecraft.world.item.Items.GLASS_BOTTLE;

public class TCoreItems {

    private TCoreItems() {

    }

    public static void register() {

        registerResources();
        registerMaterials();
        registerParts();
        registerAugments();
        registerTools();
        registerArmor();

        registerSpawnEggs();
    }

    public static void setup() {

        DetonatorItem.registerTNT(Blocks.TNT, PrimedTnt::new);

        DetonatorItem.registerTNT(BLOCKS.get(ID_SLIME_TNT), ((TntBlockCoFH) (BLOCKS.get(ID_SLIME_TNT))).getFactory());
        DetonatorItem.registerTNT(BLOCKS.get(ID_REDSTONE_TNT), ((TntBlockCoFH) (BLOCKS.get(ID_REDSTONE_TNT))).getFactory());
        DetonatorItem.registerTNT(BLOCKS.get(ID_GLOWSTONE_TNT), ((TntBlockCoFH) (BLOCKS.get(ID_GLOWSTONE_TNT))).getFactory());
        DetonatorItem.registerTNT(BLOCKS.get(ID_ENDER_TNT), ((TntBlockCoFH) (BLOCKS.get(ID_ENDER_TNT))).getFactory());

        DetonatorItem.registerTNT(BLOCKS.get(ID_FIRE_TNT), ((TntBlockCoFH) (BLOCKS.get(ID_FIRE_TNT))).getFactory());
        DetonatorItem.registerTNT(BLOCKS.get(ID_EARTH_TNT), ((TntBlockCoFH) (BLOCKS.get(ID_EARTH_TNT))).getFactory());
        DetonatorItem.registerTNT(BLOCKS.get(ID_ICE_TNT), ((TntBlockCoFH) (BLOCKS.get(ID_ICE_TNT))).getFactory());
        DetonatorItem.registerTNT(BLOCKS.get(ID_LIGHTNING_TNT), ((TntBlockCoFH) (BLOCKS.get(ID_LIGHTNING_TNT))).getFactory());

        DetonatorItem.registerTNT(BLOCKS.get(ID_PHYTO_TNT), ((TntBlockCoFH) (BLOCKS.get(ID_PHYTO_TNT))).getFactory());
        DetonatorItem.registerTNT(BLOCKS.get(ID_NUKE_TNT), ((TntBlockCoFH) (BLOCKS.get(ID_NUKE_TNT))).getFactory());

        ((DivingArmorItem) ITEMS.get(ID_DIVING_HELMET)).setup();
        ((DivingArmorItem) ITEMS.get(ID_DIVING_CHESTPLATE)).setup();
        ((DivingArmorItem) ITEMS.get(ID_DIVING_LEGGINGS)).setup();
        ((DivingArmorItem) ITEMS.get(ID_DIVING_BOOTS)).setup();

        flagUniqueAugment(ITEMS.get("rs_control_augment"));
        flagUniqueAugment(ITEMS.get("side_config_augment"));
        flagUniqueAugment(ITEMS.get("xp_storage_augment"));

        flagUniqueAugment(ITEMS.get("upgrade_augment_1"));
        flagUniqueAugment(ITEMS.get("upgrade_augment_2"));
        flagUniqueAugment(ITEMS.get("upgrade_augment_3"));

        flagUniqueAugment(ITEMS.get("rf_coil_augment"));
        flagUniqueAugment(ITEMS.get("rf_coil_storage_augment"));
        flagUniqueAugment(ITEMS.get("rf_coil_xfer_augment"));
        flagUniqueAugment(ITEMS.get("rf_coil_creative_augment"));

        flagUniqueAugment(ITEMS.get("fluid_tank_augment"));
        flagUniqueAugment(ITEMS.get("fluid_tank_creative_augment"));

        flagUniqueAugment(ITEMS.get("fluid_filter_augment"));
        flagUniqueAugment(ITEMS.get("item_filter_augment"));

        flagUniqueAugment(ITEMS.get("machine_efficiency_creative_augment"));
        flagUniqueAugment(ITEMS.get("machine_catalyst_creative_augment"));
        flagUniqueAugment(ITEMS.get("machine_cycle_augment"));
        flagUniqueAugment(ITEMS.get("machine_null_augment"));

        flagUniqueAugment(ITEMS.get("dynamo_throttle_augment"));
    }

    // region HELPERS
    private static void registerResources() {

        itemsTab(registerItem("apatite"));
        itemsTab(registerItem("apatite_dust"));
        itemsTab(registerItem("cinnabar"));
        itemsTab(registerItem("cinnabar_dust"));
        itemsTab(registerItem("niter"));
        itemsTab(registerItem("niter_dust"));
        itemsTab(registerItem("sulfur", () -> new ItemCoFH(new Item.Properties()).setBurnTime(1200)));
        itemsTab(registerItem("sulfur_dust", () -> new ItemCoFH(new Item.Properties()).setBurnTime(1200)));

        itemsTab(registerItem("sawdust"));
        itemsTab(registerItem("coal_coke", () -> new ItemCoFH(new Item.Properties()).setBurnTime(3200)));
        itemsTab(registerItem("bitumen", () -> new ItemCoFH(new Item.Properties()).setBurnTime(1600)));
        itemsTab(registerItem("tar", () -> new ItemCoFH(new Item.Properties()).setBurnTime(800)));
        itemsTab(registerItem("rosin", () -> new ItemCoFH(new Item.Properties()).setBurnTime(800)));
        itemsTab(registerItem("rubber"));
        itemsTab(registerItem("cured_rubber"));
        itemsTab(registerItem("slag"));
        itemsTab(registerItem("rich_slag"));

        foodsTab(registerItem("syrup_bottle", () -> new HoneyBottleItem(new Item.Properties().craftRemainder(GLASS_BOTTLE).food(Foods.HONEY_BOTTLE).stacksTo(16))));

        //        registerItem("biomass");
        //        registerItem("rich_biomass");

        itemsTab(registerItem("basalz_rod"));
        itemsTab(registerItem("basalz_powder"));
        itemsTab(registerItem("blitz_rod"));
        itemsTab(registerItem("blitz_powder"));
        itemsTab(registerItem("blizz_rod"));
        itemsTab(registerItem("blizz_powder"));

        itemsTab(registerItem("beekeeper_fabric"));
        itemsTab(registerItem("diving_fabric"));
        itemsTab(registerItem("hazmat_fabric"));
    }

    private static void registerParts() {

        registerItem("redstone_servo");
        registerItem("rf_coil");

        registerItem("drill_head", () -> new ItemCoFH(new Item.Properties()));
        registerItem("saw_blade", () -> new ItemCoFH(new Item.Properties()));
        registerItem("laser_diode", () -> new ItemCoFH(new Item.Properties()));//.setShowInGroups(getFeature(FLAG_TOOL_COMPONENTS))); // TODO: Implement
    }

    private static void registerMaterials() {

        registerItem("ender_pearl_dust");

        registerVanillaMetalSet("iron");
        registerVanillaMetalSet("gold");
        registerVanillaMetalSet("copper");
        registerVanillaMetalSet("netherite");

        registerVanillaGemSet("lapis");
        registerVanillaGemSet("diamond");
        registerVanillaGemSet("emerald");
        registerVanillaGemSet("quartz");

        Rarity rarity = Rarity.UNCOMMON;

        registerAlloySet("signalum", rarity);
        registerAlloySet("lumium", rarity);
        registerAlloySet("enderium", rarity);
    }

    private static void registerTools() {

        toolsTab(registerItem(ID_WRENCH, () -> new WrenchItem(new Item.Properties().stacksTo(1))));
        toolsTab(registerItem(ID_REDPRINT, () -> new RedprintItem(new Item.Properties().stacksTo(1))));
        toolsTab(registerItem(ID_RF_POTATO, () -> new EnergyContainerItem(new Item.Properties().stacksTo(1), 100000, 40) {

            @Override
            public Capability<? extends IEnergyStorage> getEnergyCapability() {

                return ThermalEnergyHelper.getBaseEnergySystem();
            }
        }));
        toolsTab(registerItem(ID_XP_CRYSTAL, () -> new XpCrystalItem(new Item.Properties().stacksTo(1), 10000)));
        toolsTab(registerItem(ID_LOCK, () -> new LockItem(new Item.Properties())));
        toolsTab(registerItem(ID_SATCHEL, () -> new SatchelItem(new Item.Properties().stacksTo(1), 9)));

        toolsTab(registerItem("compost", () -> new FertilizerItem(new Item.Properties(), 2)));
        toolsTab(registerItem("phytogro", () -> new FertilizerItem(new Item.Properties())));
        // toolsTab(registerItem("fluxed_phytogro", () -> new FertilizerItem(new Item.Properties(), 5)));

        toolsTab(registerItem("junk_net"));
        toolsTab(registerItem("aquachow"));
        toolsTab(registerItem("deep_aquachow"));
        //        registerItem("rich_aquachow");
        //        registerItem("fluxed_aquachow");

        toolsTab(registerItem("earth_charge", () -> new EarthChargeItem(new Item.Properties())));
        toolsTab(registerItem("ice_charge", () -> new IceChargeItem(new Item.Properties())));
        toolsTab(registerItem("lightning_charge", () -> new LightningChargeItem(new Item.Properties())));

        toolsTab(registerItem(ID_DETONATOR, () -> new DetonatorItem(new Item.Properties().stacksTo(1))));
    }

    private static void registerArmor() {

        toolsTab(registerItem(ID_BEEKEEPER_HELMET, () -> new BeekeeperArmorItem(BEEKEEPER, ArmorItem.Type.HELMET, new Item.Properties())));
        toolsTab(registerItem(ID_BEEKEEPER_CHESTPLATE, () -> new BeekeeperArmorItem(BEEKEEPER, ArmorItem.Type.CHESTPLATE, new Item.Properties())));
        toolsTab(registerItem(ID_BEEKEEPER_LEGGINGS, () -> new BeekeeperArmorItem(BEEKEEPER, ArmorItem.Type.LEGGINGS, new Item.Properties())));
        toolsTab(registerItem(ID_BEEKEEPER_BOOTS, () -> new BeekeeperArmorItem(BEEKEEPER, ArmorItem.Type.BOOTS, new Item.Properties())));

        toolsTab(registerItem(ID_DIVING_HELMET, () -> new DivingArmorItem(DIVING, ArmorItem.Type.HELMET, new Item.Properties())));
        toolsTab(registerItem(ID_DIVING_CHESTPLATE, () -> new DivingArmorItem(DIVING, ArmorItem.Type.CHESTPLATE, new Item.Properties())));
        toolsTab(registerItem(ID_DIVING_LEGGINGS, () -> new DivingArmorItem(DIVING, ArmorItem.Type.LEGGINGS, new Item.Properties())));
        toolsTab(registerItem(ID_DIVING_BOOTS, () -> new DivingArmorItem(DIVING, ArmorItem.Type.BOOTS, new Item.Properties())));

        toolsTab(registerItem(ID_HAZMAT_HELMET, () -> new HazmatArmorItem(HAZMAT, ArmorItem.Type.HELMET, new Item.Properties())));
        toolsTab(registerItem(ID_HAZMAT_CHESTPLATE, () -> new HazmatArmorItem(HAZMAT, ArmorItem.Type.CHESTPLATE, new Item.Properties())));
        toolsTab(registerItem(ID_HAZMAT_LEGGINGS, () -> new HazmatArmorItem(HAZMAT, ArmorItem.Type.LEGGINGS, new Item.Properties())));
        toolsTab(registerItem(ID_HAZMAT_BOOTS, () -> new HazmatArmorItem(HAZMAT, ArmorItem.Type.BOOTS, new Item.Properties())));
    }

    // region AUGMENTS
    private static void registerAugments() {

        registerUpgradeAugments();
        registerFeatureAugments();
        registerStorageAugments();
        registerFilterAugments();
        registerMachineAugments();
        registerDynamoAugments();
        registerAreaAugments();
        registerPotionAugments();
    }

    private static void registerUpgradeAugments() {

        final float[] upgradeMods = new float[]{1.0F, 2.0F, 3.0F, 4.0F, 6.0F, 8.5F};
        // final float[] upgradeMods = new float[]{1.0F, 1.5F, 2.0F, 2.5F, 3.0F, 3.5F};

        for (int i = 1; i <= 3; ++i) {
            int tier = i;
            itemsTab(registerItem("upgrade_augment_" + i, () -> new AugmentItem(new Item.Properties(),
                    AugmentDataHelper.builder()
                            .type(TAG_AUGMENT_TYPE_UPGRADE)
                            .mod(TAG_AUGMENT_BASE_MOD, upgradeMods[tier])
                            .build())));
        }
    }

    private static void registerFeatureAugments() {

        itemsTab(registerItem("rs_control_augment", () -> new AugmentItem(new Item.Properties(),
                AugmentDataHelper.builder()
                        .mod(TAG_AUGMENT_FEATURE_RS_CONTROL, 1.0F)
                        .build())));

        itemsTab(registerItem("side_config_augment", () -> new AugmentItem(new Item.Properties(),
                AugmentDataHelper.builder()
                        .mod(TAG_AUGMENT_FEATURE_SIDE_CONFIG, 1.0F)
                        .build())));

        itemsTab(registerItem("xp_storage_augment", () -> new AugmentItem(new Item.Properties(),
                AugmentDataHelper.builder()
                        .mod(TAG_AUGMENT_FEATURE_XP_STORAGE, 1.0F)
                        .build())));
    }

    private static void registerStorageAugments() {

        itemsTab(registerItem("rf_coil_augment", () -> new AugmentItem(new Item.Properties(),
                AugmentDataHelper.builder()
                        .type(TAG_AUGMENT_TYPE_RF)
                        .mod(TAG_AUGMENT_RF_STORAGE, 4.0F)
                        .mod(TAG_AUGMENT_RF_XFER, 4.0F)
                        .build())));

        itemsTab(registerItem("rf_coil_storage_augment", () -> new AugmentItem(new Item.Properties(),
                AugmentDataHelper.builder()
                        .type(TAG_AUGMENT_TYPE_RF)
                        .mod(TAG_AUGMENT_RF_STORAGE, 6.0F)
                        .mod(TAG_AUGMENT_RF_XFER, 2.0F)
                        .build())));

        itemsTab(registerItem("rf_coil_xfer_augment", () -> new AugmentItem(new Item.Properties(),
                AugmentDataHelper.builder()
                        .type(TAG_AUGMENT_TYPE_RF)
                        .mod(TAG_AUGMENT_RF_STORAGE, 2.0F)
                        .mod(TAG_AUGMENT_RF_XFER, 6.0F)
                        .build())));

        itemsTab(registerItem("rf_coil_creative_augment", () -> new AugmentItem(new Item.Properties().rarity(Rarity.EPIC),
                AugmentDataHelper.builder()
                        .type(TAG_AUGMENT_TYPE_RF)
                        .mod(TAG_AUGMENT_RF_STORAGE, 16.0F)
                        .mod(TAG_AUGMENT_RF_XFER, 16.0F)
                        .mod(TAG_AUGMENT_RF_CREATIVE, 1.0F)
                        .build())));

        itemsTab(registerItem("fluid_tank_augment", () -> new AugmentItem(new Item.Properties(),
                AugmentDataHelper.builder()
                        .type(TAG_AUGMENT_TYPE_FLUID)
                        .mod(TAG_AUGMENT_FLUID_STORAGE, 4.0F)
                        .build())));

        itemsTab(registerItem("fluid_tank_creative_augment", () -> new AugmentItem(new Item.Properties().rarity(Rarity.EPIC),
                AugmentDataHelper.builder()
                        .type(TAG_AUGMENT_TYPE_FLUID)
                        .mod(TAG_AUGMENT_FLUID_STORAGE, 16.0F)
                        .mod(TAG_AUGMENT_FLUID_CREATIVE, 1.0F)
                        .build())));
    }

    private static void registerFilterAugments() {

        itemsTab(registerItem("item_filter_augment", () -> new AugmentItem(new Item.Properties(),
                AugmentDataHelper.builder()
                        .type(TAG_AUGMENT_TYPE_FILTER)
                        .feature(TAG_FILTER_TYPE, FilterRegistry.ITEM_FILTER_TYPE)
                        .build())));

        itemsTab(registerItem("fluid_filter_augment", () -> new AugmentItem(new Item.Properties(),
                AugmentDataHelper.builder()
                        .type(TAG_AUGMENT_TYPE_FILTER)
                        .feature(TAG_FILTER_TYPE, FilterRegistry.FLUID_FILTER_TYPE)
                        .build())));
        //
        //        registerItem("dual_filter_augment", () -> new AugmentItem(new Item.Properties().group(group),
        //                AugmentDataHelper.builder()
        //                        .type(TAG_AUGMENT_TYPE_FILTER)
        //                        .feature(TAG_FILTER_TYPE, FilterRegistry.DUAL_FILTER_TYPE)
        //                        .build()).setShowInGroups(getFlag(FLAG_FILTER_AUGMENTS)));
    }

    private static void registerMachineAugments() {

        itemsTab(registerItem("machine_speed_augment", () -> new AugmentItem(new Item.Properties(),
                AugmentDataHelper.builder()
                        .type(TAG_AUGMENT_TYPE_MACHINE)
                        .mod(TAG_AUGMENT_MACHINE_POWER, 1.0F)
                        .mod(TAG_AUGMENT_MACHINE_ENERGY, 1.1F)
                        .build())));

        itemsTab(registerItem("machine_efficiency_augment", () -> new AugmentItem(new Item.Properties(),
                AugmentDataHelper.builder()
                        .type(TAG_AUGMENT_TYPE_MACHINE)
                        .mod(TAG_AUGMENT_MACHINE_SPEED, -0.1F)
                        .mod(TAG_AUGMENT_MACHINE_ENERGY, 0.9F)
                        .build())));

        itemsTab(registerItem("machine_efficiency_creative_augment", () -> new AugmentItem(new Item.Properties().rarity(Rarity.EPIC),
                AugmentDataHelper.builder()
                        .type(TAG_AUGMENT_TYPE_MACHINE)
                        .mod(TAG_AUGMENT_MACHINE_ENERGY, 0.0F)
                        .build())));

        itemsTab(registerItem("machine_output_augment", () -> new AugmentItem(new Item.Properties(),
                AugmentDataHelper.builder()
                        .type(TAG_AUGMENT_TYPE_MACHINE)
                        .mod(TAG_AUGMENT_MACHINE_SECONDARY, 0.15F)
                        .mod(TAG_AUGMENT_MACHINE_ENERGY, 1.25F)
                        .build())));

        itemsTab(registerItem("machine_catalyst_augment", () -> new AugmentItem(new Item.Properties(),
                AugmentDataHelper.builder()
                        .type(TAG_AUGMENT_TYPE_MACHINE)
                        .mod(TAG_AUGMENT_MACHINE_CATALYST, 0.8F)
                        .mod(TAG_AUGMENT_MACHINE_ENERGY, 1.25F)
                        .build())));

        itemsTab(registerItem("machine_catalyst_creative_augment", () -> new AugmentItem(new Item.Properties().rarity(Rarity.EPIC),
                AugmentDataHelper.builder()
                        .type(TAG_AUGMENT_TYPE_MACHINE)
                        .mod(TAG_AUGMENT_MACHINE_CATALYST, 0.0F)
                        .build())));

        itemsTab(registerItem("machine_cycle_augment", () -> new AugmentItem(new Item.Properties(),
                AugmentDataHelper.builder()
                        .type(TAG_AUGMENT_TYPE_MACHINE)
                        .mod(TAG_AUGMENT_FEATURE_CYCLE_PROCESS, 1.0F)
                        .build())));

        itemsTab(registerItem("machine_null_augment", () -> new AugmentItem(new Item.Properties(),
                AugmentDataHelper.builder()
                        .type(TAG_AUGMENT_TYPE_MACHINE)
                        .mod(TAG_AUGMENT_FEATURE_SECONDARY_NULL, 1.0F)
                        .build())));
    }

    private static void registerDynamoAugments() {

        itemsTab(registerItem("dynamo_output_augment", () -> new AugmentItem(new Item.Properties(),
                AugmentDataHelper.builder()
                        .type(TAG_AUGMENT_TYPE_DYNAMO)
                        .mod(TAG_AUGMENT_DYNAMO_POWER, 1.0F)
                        .mod(TAG_AUGMENT_DYNAMO_ENERGY, 0.9F)
                        .build())));

        itemsTab(registerItem("dynamo_fuel_augment", () -> new AugmentItem(new Item.Properties(),
                AugmentDataHelper.builder()
                        .type(TAG_AUGMENT_TYPE_DYNAMO)
                        .mod(TAG_AUGMENT_DYNAMO_ENERGY, 1.1F)
                        .build())));

        itemsTab(registerItem("dynamo_throttle_augment", () -> new AugmentItem(new Item.Properties(),
                AugmentDataHelper.builder()
                        .type(TAG_AUGMENT_TYPE_DYNAMO)
                        .mod(TAG_AUGMENT_DYNAMO_THROTTLE, 1.0F)
                        .build())));
    }

    private static void registerAreaAugments() {

        itemsTab(registerItem("area_radius_augment", () -> new AugmentItem(new Item.Properties(),
                AugmentDataHelper.builder()
                        .type(TAG_AUGMENT_TYPE_AREA_EFFECT)
                        .mod(TAG_AUGMENT_RADIUS, 1.0F)
                        .build())));
    }

    private static void registerPotionAugments() {

        itemsTab(registerItem("potion_amplifier_augment", () -> new AugmentItem(new Item.Properties(),
                AugmentDataHelper.builder()
                        .type(TAG_AUGMENT_TYPE_POTION)
                        .mod(TAG_AUGMENT_POTION_AMPLIFIER, 1.0F)
                        .mod(TAG_AUGMENT_POTION_DURATION, -0.25F)
                        .build())));

        itemsTab(registerItem("potion_duration_augment", () -> new AugmentItem(new Item.Properties(),
                AugmentDataHelper.builder()
                        .type(TAG_AUGMENT_TYPE_POTION)
                        .mod(TAG_AUGMENT_POTION_DURATION, 1.0F)
                        .build())));
    }
    // endregion

    private static void registerSpawnEggs() {

        itemsTab(registerItem("basalz_spawn_egg", () -> new SpawnEggItemCoFH(BASALZ::get, 0x363840, 0x080407, new Item.Properties())));
        itemsTab(registerItem("blizz_spawn_egg", () -> new SpawnEggItemCoFH(BLIZZ::get, 0xD8DBE5, 0x91D9FC, new Item.Properties())));
        itemsTab(registerItem("blitz_spawn_egg", () -> new SpawnEggItemCoFH(BLITZ::get, 0xC9EEFF, 0xFFD97E, new Item.Properties())));
    }
    // endregion

    public static final ArmorMaterialCoFH BEEKEEPER = new ArmorMaterialCoFH("thermal:beekeeper", 4, new int[]{1, 2, 3, 1}, 16, SoundEvents.ARMOR_EQUIP_ELYTRA, 0.0F, 0.0F, () -> Ingredient.of(ITEMS.get("beekeeper_fabric")));
    public static final ArmorMaterialCoFH DIVING = new ArmorMaterialCoFH("thermal:diving", 12, new int[]{1, 4, 5, 2}, 20, SoundEvents.ARMOR_EQUIP_CHAIN, 0.0F, 0.0F, () -> Ingredient.of(ITEMS.get("diving_fabric")));
    public static final ArmorMaterialCoFH HAZMAT = new ArmorMaterialCoFH("thermal:hazmat", 6, new int[]{1, 4, 5, 2}, 15, SoundEvents.ARMOR_EQUIP_LEATHER, 0.0F, 0.0F, () -> Ingredient.of(ITEMS.get("hazmat_fabric")));

}
