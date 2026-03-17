package net.qbismx.skillwindsword.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.Nullable;

public class LightParticleProvider implements ParticleProvider<SimpleParticleType> {

    private final SpriteSet sprites;

    public LightParticleProvider(SpriteSet sprites) {
        this.sprites = sprites;
    }

    @Override
    public @Nullable Particle createParticle(SimpleParticleType type,
                                             ClientLevel level,
                                             double x, double y, double z,
                                             double xd, double yd, double zd) {

        LightParticle particle = new LightParticle(level, x, y, z, xd, yd, zd);

        particle.pickSprite(sprites);
        return particle;
    }
}
