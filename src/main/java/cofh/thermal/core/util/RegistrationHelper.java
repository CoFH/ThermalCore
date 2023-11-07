package cofh.thermal.core.util;

import cofh.core.common.entity.AbstractGrenade;
import cofh.core.common.entity.AbstractTNTMinecart;
import cofh.core.common.item.*;
import cofh.lib.api.IDetonatable;
import cofh.lib.common.block.CropBlockCoFH;
import cofh.lib.common.block.CropBlockPerennial;
import cofh.lib.common.block.CropBlockTall;
import cofh.lib.common.block.TntBlockCoFH;
import cofh.lib.common.entity.PrimedTntCoFH;
import cofh.thermal.core.common.entity.explosive.DetonateUtils;
import cofh.thermal.core.common.entity.explosive.Grenade;
import cofh.thermal.core.common.entity.explosive.ThermalTNTEntity;
import cofh.thermal.core.common.entity.explosive.ThermalTNTMinecart;
import cofh.thermal.lib.common.item.BlockItemAugmentable;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

import static cofh.lib.util.constants.ModIds.*;
import static cofh.thermal.core.ThermalCore.*;
import static cofh.thermal.lib.init.ThermalCreativeTabs.*;
import static net.minecraft.world.level.block.state.BlockBehaviour.Properties.of;

public final class RegistrationHelper {

    private RegistrationHelper() {

    }

    // region BLOCKS
    public static RegistryObject<Item> registerBlock(String name, Supplier<Block> sup) {

        return registerBlock(name, sup, ID_THERMAL);
    }

    public static RegistryObject<Item> registerBlock(String name, Supplier<Block> sup, Rarity rarity) {

        return registerBlock(name, sup, rarity, ID_THERMAL);
    }

    public static RegistryObject<Item> registerBlock(String name, Supplier<Block> sup, String modId) {

        return registerBlock(name, sup, Rarity.COMMON, modId);
    }

    public static RegistryObject<Item> registerBlock(String name, Supplier<Block> sup, Rarity rarity, String modId) {

        return registerBlock(name, sup, () -> new BlockItemCoFH(BLOCKS.get(name), new Item.Properties().rarity(rarity)).setModId(modId));
    }

    public static void registerBlockOnly(String name, Supplier<Block> sup) {

        BLOCKS.register(name, sup);
    }

    public static RegistryObject<Item> registerBlock(String name, Supplier<Block> blockSup, Supplier<Item> itemSup) {

        BLOCKS.register(name, blockSup);
        return registerItem(name, itemSup);
    }
    // endregion

    // region AUGMENTABLE BLOCKS
    public static RegistryObject<Item> registerAugmentableBlock(String name, Supplier<Block> sup, IntSupplier numSlots, BiPredicate<ItemStack, List<ItemStack>> validAugment) {

        return registerAugmentableBlock(name, sup, numSlots, validAugment, ID_THERMAL);
    }

    public static RegistryObject<Item> registerAugmentableBlock(String name, Supplier<Block> sup, IntSupplier numSlots, BiPredicate<ItemStack, List<ItemStack>> validAugment, String modId) {

        return registerAugmentableBlock(name, sup, numSlots, validAugment, Rarity.COMMON, modId);
    }

    public static RegistryObject<Item> registerAugmentableBlock(String name, Supplier<Block> sup, IntSupplier numSlots, BiPredicate<ItemStack, List<ItemStack>> validAugment, Rarity rarity, String modId) {

        BLOCKS.register(name, sup);
        return registerItem(name, () -> new BlockItemAugmentable(BLOCKS.get(name), new Item.Properties().rarity(rarity)).setNumSlots(numSlots).setAugValidator(validAugment).setModId(modId));
    }
    // endregion

    // region BLOCK SETS
    public static void registerWoodBlockSet(String woodName, MapColor color, float hardness, float resistance, SoundType soundType, WoodType type, String modId) {

        blocksTab(registerBlock(woodName + "_planks", () -> new Block(of().mapColor(color).instrument(NoteBlockInstrument.BASS).strength(hardness, resistance).sound(soundType)), modId));
        blocksTab(registerBlock(woodName + "_slab", () -> new SlabBlock(of().mapColor(color).instrument(NoteBlockInstrument.BASS).strength(hardness, resistance).sound(soundType)), modId));
        blocksTab(registerBlock(woodName + "_stairs", () -> new StairBlock(() -> BLOCKS.get(woodName + "_planks").defaultBlockState(), of().mapColor(color).instrument(NoteBlockInstrument.BASS).strength(hardness, resistance).sound(soundType)), modId));
        blocksTab(registerBlock(woodName + "_door", () -> new DoorBlock(of().mapColor(color).instrument(NoteBlockInstrument.BASS).strength(resistance).sound(soundType).noOcclusion(), type.setType()), modId));
        blocksTab(registerBlock(woodName + "_trapdoor", () -> new TrapDoorBlock(of().mapColor(color).instrument(NoteBlockInstrument.BASS).strength(resistance).sound(soundType).noOcclusion().isValidSpawn((state, reader, pos, entityType) -> false), type.setType()), modId));
        blocksTab(registerBlock(woodName + "_button", () -> Blocks.woodenButton(type.setType()), modId));
        blocksTab(registerBlock(woodName + "_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, of().mapColor(color).forceSolidOn().instrument(NoteBlockInstrument.BASS).noCollission().strength(0.5F).ignitedByLava().pushReaction(PushReaction.DESTROY), type.setType()), modId));
        blocksTab(registerBlock(woodName + "_fence", () -> new FenceBlock(of().mapColor(color).instrument(NoteBlockInstrument.BASS).strength(hardness, resistance).sound(soundType)), modId));
        blocksTab(registerBlock(woodName + "_fence_gate", () -> new FenceGateBlock(of().mapColor(color).forceSolidOn().instrument(NoteBlockInstrument.BASS).strength(hardness, resistance).ignitedByLava(), type), modId));
    }
    // endregion

    // region ITEMS
    public static RegistryObject<Item> registerItem(String name, Supplier<Item> sup) {

        return ITEMS.register(name, sup);
    }

    public static RegistryObject<Item> registerItem(String name) {

        return registerItem(name, Rarity.COMMON);
    }

    public static RegistryObject<Item> registerItem(String name, Rarity rarity) {

        return registerItem(name, () -> new ItemCoFH(new Item.Properties().rarity(rarity)));
    }
    // endregion

    // region METAL SETS
    public static void registerMetalSet(String prefix, Rarity rarity) {

        registerMetalSet(prefix, rarity, false, false, ID_THERMAL);
    }

    public static void registerMetalSet(String prefix) {

        registerMetalSet(prefix, Rarity.COMMON, false, false, ID_THERMAL);
    }

    public static void registerMetalSet(String prefix, String modId) {

        registerMetalSet(prefix, Rarity.COMMON, false, false, modId);
    }

    public static void registerAlloySet(String prefix, Rarity rarity) {

        registerMetalSet(prefix, rarity, false, true, ID_THERMAL);
    }

    public static void registerAlloySet(String prefix) {

        registerMetalSet(prefix, Rarity.COMMON, false, true, ID_THERMAL);
    }

    public static void registerAlloySet(String prefix, String modId) {

        registerMetalSet(prefix, Rarity.COMMON, false, true, modId);
    }

    public static void registerVanillaMetalSet(String prefix) {

        registerMetalSet(prefix, Rarity.COMMON, true, false, ID_THERMAL);
    }

    public static void registerMetalSet(String prefix, Rarity rarity, boolean vanilla, boolean alloy, String modId) {

        int order = vanilla ? 999 : alloy ? 1001 : 1000;

        // Hacky but whatever.
        if (prefix.equals("copper") || prefix.equals("netherite")) {
            itemsTab(order, registerItem(prefix + "_nugget", () -> new ItemCoFH(new Item.Properties().rarity(rarity)).setModId(modId)));
        }
        if (!vanilla) {
            if (!alloy) {
                itemsTab(order, registerItem("raw_" + prefix, () -> new ItemCoFH(new Item.Properties().rarity(rarity)).setModId(modId)));
            }
            itemsTab(order, registerItem(prefix + "_ingot", () -> new ItemCoFH(new Item.Properties().rarity(rarity)).setModId(modId)));
            itemsTab(order, registerItem(prefix + "_nugget", () -> new ItemCoFH(new Item.Properties().rarity(rarity)).setModId(modId)));
        }
        itemsTab(order, registerItem(prefix + "_dust", () -> new ItemCoFH(new Item.Properties().rarity(rarity)).setModId(modId)));
        itemsTab(order, registerItem(prefix + "_gear", () -> new ItemCoFH(new Item.Properties().rarity(rarity)).setModId(modId)));
        itemsTab(order, registerItem(prefix + "_plate", () -> new CountedItem(new Item.Properties().rarity(rarity)).setModId(modId)));
        itemsTab(order, registerItem(prefix + "_coin", () -> new CoinItem(new Item.Properties().rarity(rarity)).setModId(modId)));
    }
    // endregion

    // region GEM SETS
    public static void registerGemSet(String prefix, Rarity rarity) {

        registerGemSet(prefix, rarity, false, ID_THERMAL);
    }

    public static void registerGemSet(String prefix) {

        registerGemSet(prefix, Rarity.COMMON, false, ID_THERMAL);
    }

    public static void registerGemSet(String prefix, String modId) {

        registerGemSet(prefix, Rarity.COMMON, false, modId);
    }

    public static void registerVanillaGemSet(String prefix) {

        registerGemSet(prefix, Rarity.COMMON, true, ID_THERMAL);
    }

    public static void registerGemSet(String prefix, Rarity rarity, boolean vanilla, String modId) {

        if (!vanilla) {
            itemsTab(registerItem(prefix, () -> new ItemCoFH(new Item.Properties().rarity(rarity)).setModId(modId)));
        }
        // itemsTab(registerItem(prefix + "_nugget", () -> new ItemCoFH(new Item.Properties().group(group).rarity(rarity)).setModId(modId)));
        itemsTab(registerItem(prefix + "_dust", () -> new ItemCoFH(new Item.Properties().rarity(rarity)).setModId(modId)));
        itemsTab(registerItem(prefix + "_gear", () -> new ItemCoFH(new Item.Properties().rarity(rarity)).setModId(modId)));
        // itemsTab(registerItem(prefix + "_plate", () -> new CountedItem(new Item.Properties().group(group).rarity(rarity)).setModId(modId)));
        // itemsTab(registerItem(prefix + "_coin", () -> new CoinItem(new Item.Properties().group(group).rarity(rarity)).setModId(modId)));
    }
    // endregion

    // region CROPS
    public static void registerAnnual(String id) {

        BLOCKS.register(id, () -> new CropBlockCoFH(of().mapColor(MapColor.PLANT).noCollission().randomTicks().strength(0.0F, 0.0F).sound(SoundType.CROP)).crop(ITEMS.getSup(id)).seed(ITEMS.getSup(seeds(id))));
    }

    public static void registerTallAnnual(String id) {

        BLOCKS.register(id, () -> new CropBlockTall(of().mapColor(MapColor.PLANT).noCollission().randomTicks().strength(0.0F, 0.0F).sound(SoundType.CROP)).crop(ITEMS.getSup(id)).seed(ITEMS.getSup(seeds(id))));
    }

    public static void registerPerennial(String id) {

        registerPerennial(id, CropBlockPerennial.DEFAULT_POST_HARVEST_AGE);
    }

    public static void registerPerennial(String id, int postHarvestAge) {

        BLOCKS.register(id, () -> new CropBlockPerennial(of().mapColor(MapColor.PLANT).noCollission().randomTicks().strength(0.0F, 0.0F).sound(SoundType.CROP)).postHarvestAge(postHarvestAge).crop(ITEMS.getSup(id)).seed(ITEMS.getSup(seeds(id))));
    }

    public static void registerCropAndSeed(String id) {

        registerCropAndSeed(id, null);
    }

    public static void registerCropAndSeed(String id, FoodProperties food) {

        if (food != null) {
            foodsTab(registerItem(id, () -> new ItemCoFH(new Item.Properties().food(food)).setModId(ID_THERMAL_CULTIVATION)));
        } else {
            foodsTab(registerItem(id, () -> new ItemCoFH(new Item.Properties()).setModId(ID_THERMAL_CULTIVATION)));
        }
        foodsTab(registerItem(seeds(id), () -> new BlockNamedItemCoFH(BLOCKS.get(id), new Item.Properties()).setModId(ID_THERMAL_CULTIVATION)));
    }

    public static void registerBowlFoodItem(String id, FoodProperties food, Rarity rarity) {

        foodsTab(registerItem(id, () -> new ItemCoFH(new Item.Properties().stacksTo(1).food(food).rarity(rarity)) {

            @Override
            public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity entityLiving) {

                ItemStack itemstack = super.finishUsingItem(stack, worldIn, entityLiving);
                return entityLiving instanceof Player player && player.abilities.instabuild ? itemstack : new ItemStack(Items.BOWL);
            }
        }.setModId(ID_THERMAL_CULTIVATION)));
    }

    public static void registerSpores(String id) {

        foodsTab(registerItem(spores(id), () -> new BlockNamedItemCoFH(BLOCKS.get(id), new Item.Properties()).setModId(ID_THERMAL_CULTIVATION)));
    }
    // endregion

    // region EXPLOSIVES
    public static RegistryObject<Item> registerGrenade(String id, IDetonatable.IDetonateAction action, Supplier<Boolean> flag) {

        RegistryObject<EntityType<? extends AbstractGrenade>> entity = ENTITIES.register(id, () -> EntityType.Builder.<Grenade>of((type, world) -> new Grenade(type, world, action), MobCategory.MISC).sized(0.25F, 0.25F).build(id));
        DetonateUtils.GRENADES.add(entity);
        return registerItem(id, () -> new GrenadeItem(new GrenadeItem.IGrenadeFactory<>() {

            @Override
            public AbstractGrenade createGrenade(Level level, LivingEntity living) {

                return new Grenade(entity.get(), level, action, living);
            }

            @Override
            public AbstractGrenade createGrenade(Level level, double posX, double posY, double posZ) {

                return new Grenade(entity.get(), level, action, posX, posY, posZ);
            }

        }, new Item.Properties().stacksTo(16)));
    }

    public static RegistryObject<Item> registerTNT(String id, IDetonatable.IDetonateAction action, Supplier<Boolean> flag) {

        RegistryObject<EntityType<? extends PrimedTntCoFH>> tntEntity = ENTITIES.register(id, () -> EntityType.Builder.<ThermalTNTEntity>of((type, world) -> new ThermalTNTEntity(type, world, action), MobCategory.MISC).fireImmune().sized(0.98F, 0.98F).build(id));
        registerBlockOnly(id, () -> new TntBlockCoFH((world, x, y, z, igniter) -> new ThermalTNTEntity(tntEntity.get(), world, action, x, y, z, igniter), of().mapColor(MapColor.COLOR_YELLOW).strength(0.0F).sound(SoundType.GRASS)));
        DetonateUtils.TNT.add(tntEntity);
        return registerItem(id, () -> new BlockItemCoFH(BLOCKS.get(id), new Item.Properties()));

    }

    public static RegistryObject<Item> registerTNTMinecart(String id, String tntId, IDetonatable.IDetonateAction action, Supplier<Boolean> flag) {

        RegistryObject<EntityType<? extends AbstractTNTMinecart>> entity = ENTITIES.register(id, () -> EntityType.Builder.<ThermalTNTMinecart>of((type, world) -> new ThermalTNTMinecart(type, world, action, BLOCKS.get(tntId)), MobCategory.MISC).sized(0.98F, 0.7F).build(id));
        DetonateUtils.CARTS.add(entity);
        return registerItem(id, () -> new MinecartItemCoFH((world, x, y, z) -> new ThermalTNTMinecart(entity.get(), world, action, BLOCKS.get(tntId), x, y, z), new Item.Properties()).setModId(ID_THERMAL_LOCOMOTION));
    }
    // endregion

    // region ID AFFIXES
    public static String deepslate(String id) {

        return "deepslate_" + id;
    }

    public static String netherrack(String id) {

        return "netherrack_" + id;
    }

    public static String raw(String id) {

        return "raw_" + id;
    }

    public static String block(String id) {

        return id + "_block";
    }

    public static String seeds(String id) {

        return id + "_seeds";
    }

    public static String spores(String id) {

        return id + "_spores";
    }
    // endregion
}
