package net.qbismx.skillwindsword.registry;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.qbismx.skillwindsword.SkillWindSword;
import net.qbismx.skillwindsword.client.renderer.SlashRenderer;
import net.qbismx.skillwindsword.entity.ModEntities;

@EventBusSubscriber(modid = SkillWindSword.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModRenderers {

    @SubscribeEvent
    public static void register(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(
                ModEntities.SLASH.get(),
                SlashRenderer::new
        );
    }
}
