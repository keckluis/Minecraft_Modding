package net.luis.myfirstmod.entity.client;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.luis.myfirstmod.entity.animations.ModAnimationsDefinitions;
import net.luis.myfirstmod.entity.custom.PenguinEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class PenguinModel<T extends Entity> extends HierarchicalModel<T> {
    private final ModelPart penguin;
    private final ModelPart torso;
    private final ModelPart head;
    private final ModelPart wings;
    private final ModelPart left_wing;
    private final ModelPart right_wing;
    private final ModelPart feet;
    private final ModelPart left_foot;
    private final ModelPart right_foot;

    public PenguinModel(ModelPart root) {
        this.penguin = root.getChild("penguin");
        this.torso = this.penguin.getChild("torso");
        this.head = this.torso.getChild("head");
        this.wings = this.torso.getChild("wings");
        this.left_wing = this.wings.getChild("left_wing");
        this.right_wing = this.wings.getChild("right_wing");
        this.feet = this.torso.getChild("feet");
        this.left_foot = this.feet.getChild("left_foot");
        this.right_foot = this.feet.getChild("right_foot");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition penguin = partdefinition.addOrReplaceChild("penguin", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition torso = penguin.addOrReplaceChild("torso", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -13.0F, -4.0F, 8.0F, 12.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition head = torso.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 19).addBox(-3.0F, -5.0F, -3.0F, 6.0F, 5.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(10, 30).addBox(-1.0F, -2.0F, -5.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -13.0F, -1.0F));

        PartDefinition wings = torso.addOrReplaceChild("wings", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition left_wing = wings.addOrReplaceChild("left_wing", CubeListBuilder.create(), PartPose.offset(4.0F, -12.0F, 0.0F));

        PartDefinition left_wing_r1 = left_wing.addOrReplaceChild("left_wing_r1", CubeListBuilder.create().texOffs(24, 19).addBox(-5.5206F, 1.8577F, -2.0F, 1.0F, 10.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.4333F, -2.1404F, 0.0F, 0.0F, 0.0F, -0.0436F));

        PartDefinition right_wing = wings.addOrReplaceChild("right_wing", CubeListBuilder.create(), PartPose.offset(-4.0F, -12.0F, 0.0F));

        PartDefinition right_wing_r1 = right_wing.addOrReplaceChild("right_wing_r1", CubeListBuilder.create().texOffs(0, 30).addBox(-0.5F, -5.0F, -2.0F, 1.0F, 10.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.7167F, 4.9298F, 0.0F, 0.0F, 0.0F, 0.0436F));

        PartDefinition feet = torso.addOrReplaceChild("feet", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition left_foot = feet.addOrReplaceChild("left_foot", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition left_foot_r1 = left_foot.addOrReplaceChild("left_foot_r1", CubeListBuilder.create().texOffs(30, 5).addBox(-1.5F, 0.0F, -4.0F, 3.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, -1.0F, 1.0F, 0.0873F, 0.0F, 0.0F));

        PartDefinition right_foot = feet.addOrReplaceChild("right_foot", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition right_foot_r1 = right_foot.addOrReplaceChild("right_foot_r1", CubeListBuilder.create().texOffs(30, 0).addBox(-1.5F, 0.0F, -4.0F, 3.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.5F, -1.0F, 1.0F, 0.0873F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.applyHeadRotation(netHeadYaw, headPitch, ageInTicks);

        this.animateWalk(ModAnimationsDefinitions.PENGUIN_WALK, limbSwing, limbSwingAmount, 5f, 4.5f);
        this.animate(((PenguinEntity) entity).idleAnimationState, ModAnimationsDefinitions.PENGUIN_IDLE, ageInTicks, 1f);
    }

    private void applyHeadRotation(float pNetHeadYaw, float pHeadPitch, float pAgeInTicks) {
        pNetHeadYaw = Mth.clamp(pNetHeadYaw, -30.0f, 30.0f);
        pHeadPitch = Mth.clamp(pHeadPitch, -25.0f, 45.0f);

        this.head.yRot = pNetHeadYaw * ((float) Math.PI / 180f);
        this.head.xRot = pHeadPitch * ((float) Math.PI / 180f);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        penguin.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart root() {
        return penguin;
    }
}