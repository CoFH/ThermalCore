package cofh.thermal.core.client.renderer.entity.layers;

import cofh.thermal.core.client.renderer.entity.model.SantaHatModel;
import cofh.thermal.core.common.config.ThermalClientConfig;
import cofh.thermal.core.util.HolidayHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;

public class FestiveLayer<T extends LivingEntity, M extends HierarchicalModel<T>> extends RenderLayer<T, M> {

    protected SantaHatModel<T> santaHatModel;

    protected float offset;
    protected float widthScale;

    public FestiveLayer(EntityRendererProvider.Context ctx, RenderLayerParent<T, M> pRenderer) {

        this(ctx, pRenderer, 0.0F, 1.0F);
    }

    public FestiveLayer(EntityRendererProvider.Context ctx, RenderLayerParent<T, M> pRenderer, float offset, float widthScale) {

        super(pRenderer);
        this.santaHatModel = new SantaHatModel<>(ctx.getModelSet().bakeLayer(SantaHatModel.HAT_LAYER));

        this.offset = offset;
        this.widthScale = widthScale;
    }

    @Override
    public void render(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, T pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTick, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {

        if (!ThermalClientConfig.festiveMobs.get() || !HolidayHelper.isChristmas(3, 2)) {
            return;
        }
        pPoseStack.pushPose();
        pPoseStack.translate(0, offset, 0);

        pPoseStack.scale(widthScale, 1.0F, widthScale);

        pPoseStack.mulPose(Axis.YP.rotationDegrees(pNetHeadYaw));
        pPoseStack.mulPose(Axis.XP.rotationDegrees(pHeadPitch));

        VertexConsumer builder = pBuffer.getBuffer(santaHatModel.renderType(SantaHatModel.TEXTURE));
        this.santaHatModel.renderToBuffer(pPoseStack, builder, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        pPoseStack.popPose();
    }

}
