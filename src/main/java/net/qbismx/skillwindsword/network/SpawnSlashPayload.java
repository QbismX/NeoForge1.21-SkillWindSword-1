package net.qbismx.skillwindsword.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.qbismx.skillwindsword.SkillWindSword;

public record SpawnSlashPayload() implements CustomPacketPayload {


    public static final Type<SpawnSlashPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(SkillWindSword.MODID, "spawn_modslash"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SpawnSlashPayload> STREAM_CODEC =
            StreamCodec.unit(new SpawnSlashPayload());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
