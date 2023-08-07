package cofh.thermal.core.event;

import cofh.core.util.ProxyClient;
import cofh.core.util.helpers.AugmentDataHelper;
import cofh.core.util.helpers.vfx.RenderTypes;
import cofh.lib.api.block.entity.IAreaEffectTile;
import cofh.thermal.core.item.WrenchItem;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelLastEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static cofh.lib.util.constants.ModIds.ID_THERMAL;
import static cofh.lib.util.constants.NBTTags.TAG_TYPE;
import static cofh.lib.util.helpers.StringHelper.*;
import static cofh.thermal.lib.common.ThermalAugmentRules.*;

@Mod.EventBusSubscriber (value = Dist.CLIENT, modid = ID_THERMAL)
public class TCoreClientEvents {

    private TCoreClientEvents() {

    }

    @SubscribeEvent
    public static void handleItemTooltipEvent(ItemTooltipEvent event) {

        List<Component> tooltip = event.getToolTip();
        if (tooltip.isEmpty()) {
            return;
        }
        ItemStack stack = event.getItemStack();

        if (AugmentDataHelper.hasAugmentData(stack)) {
            CompoundTag augmentData = AugmentDataHelper.getAugmentData(stack);
            if (augmentData == null || augmentData.isEmpty()) {
                return;
            }
            String type = augmentData.getString(TAG_TYPE);
            if (!type.isEmpty()) {
                MutableComponent typeText = getTextComponent("info.thermal.augment.type." + type).withStyle(ChatFormatting.WHITE);

                //                if (isTypeExclusive(type)) {
                //                    typeText.mergeStyle(TextFormatting.UNDERLINE);
                //                }
                tooltip.add(getTextComponent("info.cofh.type")
                        .withStyle(ChatFormatting.YELLOW)
                        .append(": ")
                        .append(typeText)
                );
            }
            for (String mod : augmentData.getAllKeys()) {
                if (mod.equals(TAG_TYPE) || !canLocalize("info.thermal.augment.attr." + mod)) {
                    continue;
                }
                float value = augmentData.getFloat(mod);
                boolean bad = isAdditive(mod) && value < 0
                        || isAdditive(mod) && value > 0 && isInverse(mod)
                        || isMultiplicative(mod) && (isInverse(mod) ? value > 1.0 : value < 1.0);

                MutableComponent modText = Component.literal("" +
                                (isAdditive(mod) && value > 0 ? "+" : "") +
                                (isInteger(mod) ? DF0.format(value) : isMultiplicative(mod) ? DF2.format(value) + "x" : DF0.format(value * 100) + "%"))
                        .withStyle(bad ? ChatFormatting.RED : ChatFormatting.GREEN);

                if (isMaximized(mod)) {
                    modText.withStyle(ChatFormatting.UNDERLINE);
                }
                tooltip.add(getTextComponent("info.thermal.augment.attr." + mod)
                        .append(": ")
                        .withStyle(ChatFormatting.GRAY)
                        .append(modText)
                );
            }
        }
    }

    @SubscribeEvent
    public static void handleRenderWorldLast(RenderLevelLastEvent event) {

        LocalPlayer player = Minecraft.getInstance().player;

        if (player != null) {
            Item heldItem = player.getMainHandItem().getItem();
            if (heldItem instanceof WrenchItem && ((WrenchItem) heldItem).getMode(player.getMainHandItem()) > 0) {
                renderOperationalAreas(player, event.getPoseStack());
            }
        }
    }

    // region HELPERS
    private static boolean playerWithinDistance(BlockPos pos, Player player, double distanceSq) {

        return pos.distToCenterSqr(player.position()) <= distanceSq;
    }

    private static void line(VertexConsumer builder, Matrix4f positionMatrix, BlockPos pos, float dx1, float dy1, float dz1, float dx2, float dy2, float dz2, int r, int g, int b, int a) {

        builder.vertex(positionMatrix, pos.getX() + dx1, pos.getY() + dy1, pos.getZ() + dz1)
                .color(r, g, b, a)
                .endVertex();
        builder.vertex(positionMatrix, pos.getX() + dx2, pos.getY() + dy2, pos.getZ() + dz2)
                .color(r, g, b, a)
                .endVertex();
    }

    private static void solidBox(VertexConsumer builder, Matrix4f positionMatrix, AABB area, int color) {

        int a = 64; // (color >> 24 & 255);
        int r = (color >> 16 & 255);
        int g = (color >> 8 & 255);
        int b = (color & 255);

        float offset = 0.001953125F; // 1 / 512
        float minX = (float) (area.minX - offset);
        float minY = (float) (area.minY - offset);
        float minZ = (float) (area.minZ - offset);
        float maxX = (float) (area.maxX + offset);
        float maxY = (float) (area.maxY + offset);
        float maxZ = (float) (area.maxZ + offset);

        builder.vertex(positionMatrix, minX, maxY, minZ).color(r, g, b, a).endVertex();
        builder.vertex(positionMatrix, maxX, maxY, minZ).color(r, g, b, a).endVertex();
        builder.vertex(positionMatrix, maxX, minY, minZ).color(r, g, b, a).endVertex();
        builder.vertex(positionMatrix, minX, minY, minZ).color(r, g, b, a).endVertex();

        builder.vertex(positionMatrix, minX, minY, maxZ).color(r, g, b, a).endVertex();
        builder.vertex(positionMatrix, maxX, minY, maxZ).color(r, g, b, a).endVertex();
        builder.vertex(positionMatrix, maxX, maxY, maxZ).color(r, g, b, a).endVertex();
        builder.vertex(positionMatrix, minX, maxY, maxZ).color(r, g, b, a).endVertex();

        builder.vertex(positionMatrix, minX, minY, minZ).color(r, g, b, a).endVertex();
        builder.vertex(positionMatrix, maxX, minY, minZ).color(r, g, b, a).endVertex();
        builder.vertex(positionMatrix, maxX, minY, maxZ).color(r, g, b, a).endVertex();
        builder.vertex(positionMatrix, minX, minY, maxZ).color(r, g, b, a).endVertex();

        builder.vertex(positionMatrix, minX, maxY, maxZ).color(r, g, b, a).endVertex();
        builder.vertex(positionMatrix, maxX, maxY, maxZ).color(r, g, b, a).endVertex();
        builder.vertex(positionMatrix, maxX, maxY, minZ).color(r, g, b, a).endVertex();
        builder.vertex(positionMatrix, minX, maxY, minZ).color(r, g, b, a).endVertex();

        builder.vertex(positionMatrix, minX, minY, maxZ).color(r, g, b, a).endVertex();
        builder.vertex(positionMatrix, minX, maxY, maxZ).color(r, g, b, a).endVertex();
        builder.vertex(positionMatrix, minX, maxY, minZ).color(r, g, b, a).endVertex();
        builder.vertex(positionMatrix, minX, minY, minZ).color(r, g, b, a).endVertex();

        builder.vertex(positionMatrix, maxX, minY, minZ).color(r, g, b, a).endVertex();
        builder.vertex(positionMatrix, maxX, maxY, minZ).color(r, g, b, a).endVertex();
        builder.vertex(positionMatrix, maxX, maxY, maxZ).color(r, g, b, a).endVertex();
        builder.vertex(positionMatrix, maxX, minY, maxZ).color(r, g, b, a).endVertex();
    }

    private static void wireBox(VertexConsumer builder, Matrix4f positionMatrix, BlockPos pos, AABB area, int color) {

        float offset = -0.0009765625F; // 1 / 1024
        float lenX = (float) (area.maxX - area.minX) - offset;
        float lenY = (float) (area.maxY - area.minY) - offset;
        float lenZ = (float) (area.maxZ - area.minZ) - offset;

        int a = 192; // (color >> 24 & 255);
        int r = (color >> 16 & 255);
        int g = (color >> 8 & 255);
        int b = (color & 255);

        line(builder, positionMatrix, pos, offset, offset, offset, lenX, offset, offset, r, g, b, a);
        line(builder, positionMatrix, pos, offset, lenY, offset, lenX, lenY, offset, r, g, b, a);
        line(builder, positionMatrix, pos, offset, offset, lenZ, lenX, offset, lenZ, r, g, b, a);
        line(builder, positionMatrix, pos, offset, lenY, lenZ, lenX, lenY, lenZ, r, g, b, a);

        line(builder, positionMatrix, pos, offset, offset, offset, offset, offset, lenZ, r, g, b, a);
        line(builder, positionMatrix, pos, lenX, offset, offset, lenX, offset, lenZ, r, g, b, a);
        line(builder, positionMatrix, pos, offset, lenY, offset, offset, lenY, lenZ, r, g, b, a);
        line(builder, positionMatrix, pos, lenX, lenY, offset, lenX, lenY, lenZ, r, g, b, a);

        line(builder, positionMatrix, pos, offset, offset, offset, offset, lenY, offset, r, g, b, a);
        line(builder, positionMatrix, pos, lenX, offset, offset, lenX, lenY, offset, r, g, b, a);
        line(builder, positionMatrix, pos, offset, offset, lenZ, offset, lenY, lenZ, r, g, b, a);
        line(builder, positionMatrix, pos, lenX, offset, lenZ, lenX, lenY, lenZ, r, g, b, a);
    }

    private static void renderOperationalAreas(LocalPlayer player, PoseStack matrixStack) {

        Vec3 projectedView = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();

        MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
        matrixStack.pushPose();
        matrixStack.translate(-projectedView.x, -projectedView.y, -projectedView.z);

        Matrix4f positionMatrix = matrixStack.last().pose();
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        VertexConsumer builder = buffer.getBuffer(RenderTypes.OVERLAY_BOX);
        for (IAreaEffectTile tile : ProxyClient.getAreaEffectTiles()) {
            if (!tile.canPlayerAccess(player) || !playerWithinDistance(tile.pos(), player, 32 * 32)) {
                continue;
            }
            AABB area = tile.getArea();
            solidBox(builder, positionMatrix, area, tile.getColor());
        }
        buffer.endBatch(RenderTypes.OVERLAY_BOX);

        builder = buffer.getBuffer(RenderTypes.OVERLAY_LINES);
        for (IAreaEffectTile tile : ProxyClient.getAreaEffectTiles()) {
            if (!tile.canPlayerAccess(player) || !playerWithinDistance(tile.pos(), player, 32 * 32)) {
                continue;
            }
            AABB area = tile.getArea();
            pos.set(area.minX, area.minY, area.minZ);
            if (area.contains(projectedView)) {
                INSIDE_OPERATIONAL_AREA_TILES.add(tile);
            } else {
                wireBox(builder, positionMatrix, pos, area, tile.getColor());
            }
        }
        buffer.endBatch(RenderTypes.OVERLAY_LINES);

        RenderSystem.disableDepthTest();

        builder = buffer.getBuffer(RenderTypes.OVERLAY_LINES);
        for (IAreaEffectTile tile : INSIDE_OPERATIONAL_AREA_TILES) {
            AABB area = tile.getArea();
            pos.set(area.minX, area.minY, area.minZ);
            wireBox(builder, positionMatrix, pos, area, tile.getColor());
        }
        buffer.endBatch(RenderTypes.OVERLAY_LINES);
        matrixStack.popPose();

        INSIDE_OPERATIONAL_AREA_TILES.clear();
    }
    // endregion

    private static final Set<IAreaEffectTile> INSIDE_OPERATIONAL_AREA_TILES = new HashSet<>();

}
