package net.qbismx.skillwindsword.registry;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.qbismx.skillwindsword.SkillWindSword;

import java.util.function.Supplier;

public class ModParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLES =
            DeferredRegister.create(Registries.PARTICLE_TYPE, SkillWindSword.MODID);

    public static final Supplier<SimpleParticleType> LIGHT = PARTICLES.register("light",
            () -> new SimpleParticleType(true));


    // 登録用のメソッド
    public static void register(IEventBus bus){
        PARTICLES.register(bus);
    }
}
