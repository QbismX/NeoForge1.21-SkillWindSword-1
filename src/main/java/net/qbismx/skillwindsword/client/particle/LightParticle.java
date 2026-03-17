package net.qbismx.skillwindsword.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import org.jetbrains.annotations.NotNull;

public class LightParticle extends TextureSheetParticle {


    protected LightParticle(ClientLevel level,
                            double x, double y, double z,
                            double xSpeed, double ySpeed, double zSpeed) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);

        this.lifetime = 2;
        this.quadSize = 0.2f;

        float t = random.nextFloat();

        setColor(t, 0.2f + 0.8f * t, t);
        setAlpha(0.8f); // 半透明にする
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }
}
