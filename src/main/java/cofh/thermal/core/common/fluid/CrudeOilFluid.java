package cofh.thermal.core.common.fluid;

import cofh.lib.common.fluid.FluidCoFH;
import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static cofh.thermal.core.ThermalCore.*;
import static cofh.thermal.lib.init.ThermalIDs.ID_FLUID_CRUDE_OIL;
import static net.minecraft.world.level.block.state.BlockBehaviour.Properties.of;

public class CrudeOilFluid extends FluidCoFH {

    private static CrudeOilFluid INSTANCE;

    public static CrudeOilFluid instance() {

        if (INSTANCE == null) {
            INSTANCE = new CrudeOilFluid();
        }
        return INSTANCE;
    }

    protected CrudeOilFluid() {

        super(FLUIDS, ID_FLUID_CRUDE_OIL);

        particleColor = new Vector3f(0.05F, 0.05F, 0.05F);

        block = BLOCKS.register(fluid(ID_FLUID_CRUDE_OIL), () -> new FluidBlock(stillFluid, of().mapColor(MapColor.COLOR_BLACK).replaceable().noCollission().strength(100.0F).pushReaction(PushReaction.DESTROY).noLootTable()));
        bucket = ITEMS.register(bucket(ID_FLUID_CRUDE_OIL), () -> new BucketItem(stillFluid, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));
    }

    @Override
    protected ForgeFlowingFluid.Properties fluidProperties() {

        return new ForgeFlowingFluid.Properties(type(), stillFluid, flowingFluid).block(block).bucket(bucket).levelDecreasePerBlock(2);
    }

    @Override
    protected Supplier<FluidType> type() {

        return TYPE;
    }

    public static final RegistryObject<FluidType> TYPE = FLUID_TYPES.register(ID_FLUID_CRUDE_OIL, () -> new FluidType(FluidType.Properties.create()
            .fallDistanceModifier(0F)
            .density(850)
            .viscosity(1400)
            .canDrown(true)
            .canSwim(false)
            .supportsBoating(true)
            .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
            .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)) {

        @Override
        public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {

            consumer.accept(new IClientFluidTypeExtensions() {

                private static final ResourceLocation
                        STILL = new ResourceLocation("thermal:block/fluids/crude_oil_still"),
                        FLOW = new ResourceLocation("thermal:block/fluids/crude_oil_flow");

                @Override
                public ResourceLocation getStillTexture() {

                    return STILL;
                }

                @Override
                public ResourceLocation getFlowingTexture() {

                    return FLOW;
                }

                @Nullable
                @Override
                public ResourceLocation getOverlayTexture() {

                    return WATER_OVERLAY;
                }

                @Override
                public ResourceLocation getRenderOverlayTexture(Minecraft mc) {

                    return UNDERWATER_LOCATION;
                }

                @Override
                public @NotNull Vector3f modifyFogColor(Camera camera, float partialTick, ClientLevel level, int renderDistance, float darkenWorldAmount, Vector3f fluidFogColor) {

                    return instance().particleColor;
                }

                @Override
                public void modifyFogRender(Camera camera, FogRenderer.FogMode mode, float renderDistance, float partialTick, float nearDistance, float farDistance, FogShape shape) {

                    nearDistance = -8F;
                    farDistance = 4F;

                    if (farDistance > renderDistance) {
                        farDistance = renderDistance;
                        shape = FogShape.CYLINDER;
                    }

                    RenderSystem.setShaderFogStart(nearDistance);
                    RenderSystem.setShaderFogEnd(farDistance);
                    RenderSystem.setShaderFogShape(shape);
                }
            });
        }
    });

    // region BLOCK CLASS
    public static class FluidBlock extends LiquidBlock {

        public FluidBlock(Supplier<? extends FlowingFluid> fluidSup, Properties properties) {

            super(fluidSup, properties);
        }

    }
    // endregion
}
