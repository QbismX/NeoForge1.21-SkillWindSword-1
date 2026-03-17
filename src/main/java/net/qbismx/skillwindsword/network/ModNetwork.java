package net.qbismx.skillwindsword.network;

import jdk.jfr.Event;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.qbismx.skillwindsword.SkillWindSword;
import net.qbismx.skillwindsword.entity.ModSlashEntity;
import org.apache.logging.log4j.core.jmx.Server;

@EventBusSubscriber(modid = SkillWindSword.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModNetwork {

    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event){

        PayloadRegistrar registrar = event.registrar("1");

        registrar.playToServer(
                SpawnSlashPayload.TYPE,
                SpawnSlashPayload.STREAM_CODEC,
                (payload, context) -> {

                    context.enqueueWork(() -> {
                        Player playerBase = context.player();
                        if (!(playerBase instanceof ServerPlayer player)) return;

                        ServerLevel level = (ServerLevel) player.level(); // player.serverLevel()がない

                        Vec3 look = player.getLookAngle();
                        Vec3 pos = player.getEyePosition().add(look.scale(1.2));

                        ModSlashEntity slash = new ModSlashEntity(level, player);

                        slash.setPos(pos.x, pos.y, pos.z);
                        slash.setDeltaMovement(look.scale(1.5));
                        slash.setYRot(player.getYRot());
                        slash.setXRot(player.getXRot());

                        level.addFreshEntity(slash);
                    });
                }
        );
    }
}
