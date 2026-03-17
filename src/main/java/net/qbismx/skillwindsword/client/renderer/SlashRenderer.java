package net.qbismx.skillwindsword.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.qbismx.skillwindsword.SkillWindSword;
import net.qbismx.skillwindsword.entity.ModSlashEntity;
import org.joml.Matrix4f;


public class SlashRenderer extends EntityRenderer<ModSlashEntity> {

    private static final ResourceLocation SLASH_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(SkillWindSword.MODID, "textures/effect/zangeki.png");

    public SlashRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(ModSlashEntity entity,
                       float entityYaw,
                       float partialTick,
                       PoseStack poseStack,
                       MultiBufferSource buffer,
                       int packedLight) {

        Vec3 v = entity.getDeltaMovement();
        float yaw = (float) (Math.atan2(v.x, v.z) * 180 / Math.PI); // x * 180 / Math.PI = x度
        float pitch = (float)(Math.atan2(Math.sqrt(v.x*v.x + v.z*v.z), v.y) * 180 / Math.PI); // x * 180 / Math.PI = x度

        poseStack.pushPose(); //描画の設定開始

        // 斬撃の向き
        poseStack.mulPose(Axis.YP.rotationDegrees(yaw - 5)); // -5にしているのは、斬撃を飛ばす人に斬撃を視認できるようにするため傾けている
        poseStack.mulPose(Axis.XP.rotationDegrees(pitch - 5));

         VertexConsumer vc = buffer.getBuffer(RenderType.energySwirl(SLASH_TEXTURE, 0, 0)); //テクスチャの色を残したまま、半透明になる。光っぽい。
        // VertexConsumer vc = buffer.getBuffer(RenderType.entityTranslucentEmissive(SLASH_TEXTURE)); // 半透明になるが、見えづらい

        Matrix4f matrix = poseStack.last().pose();

        renderQuad(vc, matrix, 15728880, 1.5f);

        poseStack.popPose(); // 描画の設定終了

        super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);
    }

    private void renderQuad(VertexConsumer vc, Matrix4f matrix, int fullBright, float size){
        vc.addVertex(matrix, -size*2, -size, 0)
                .setColor(0,255,0,220)
                .setUv(0,1)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(fullBright)
                .setNormal(0,1,0);

        vc.addVertex(matrix, size*2, -size, 0)
                .setColor(0,255,0,220)
                .setUv(1,1)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(fullBright)
                .setNormal(0,1,0);


        vc.addVertex(matrix, size*2, size, 0)
                .setColor(0,255,0,220)
                .setUv(1,0)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(fullBright)
                .setNormal(0,1,0);

        vc.addVertex(matrix, -size*2, size, 0)
                .setColor(0,255,0,220)
                .setUv(0,0)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(fullBright)
                .setNormal(0,1,0);
    }

    @Override
    public ResourceLocation getTextureLocation(ModSlashEntity modSlashEntity) {
        return SLASH_TEXTURE;
    }


}
