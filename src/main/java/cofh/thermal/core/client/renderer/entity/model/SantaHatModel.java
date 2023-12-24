package cofh.thermal.core.client.renderer.entity.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

import static cofh.lib.util.constants.ModIds.ID_THERMAL;

// Made with Blockbench 4.8.3
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

public class SantaHatModel<T extends Entity> extends EntityModel<T> {

    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation HAT_LAYER = new ModelLayerLocation(new ResourceLocation("thermal:santa_hat"), "main");
    public static final ResourceLocation TEXTURE = new ResourceLocation(ID_THERMAL + ":textures/entity/santa_hat.png");

    private final ModelPart hat;

    public SantaHatModel(ModelPart root) {

        this.hat = root.getChild("hat");
    }

    public static LayerDefinition createBodyLayer() {

        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        partdefinition.addOrReplaceChild("hat",
                CubeListBuilder.create()
                        .texOffs(30, 17).addBox(-1.0F, -5.0F, 6.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                        .texOffs(35, 17).addBox(-3.0F, -7.0F, -1.0F, 6.0F, 2.0F, 8.0F, new CubeDeformation(0.0F))
                        .texOffs(0, 17).addBox(-5.0F, -5.0F, -5.0F, 10.0F, 5.0F, 10.0F, new CubeDeformation(0.0F))
                        .texOffs(0, 0).addBox(-5.0F, -5.0F, -5.0F, 10.0F, 5.0F, 10.0F, new CubeDeformation(0.5F)),
                PartPose.ZERO);

        return LayerDefinition.create(meshdefinition, 64, 32);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {

        hat.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

}