package net.qbismx.skillwindsword.event;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.qbismx.skillwindsword.SkillWindSword;
import net.qbismx.skillwindsword.network.SpawnSlashPayload;

@EventBusSubscriber(modid = SkillWindSword.MODID, bus = EventBusSubscriber.Bus.GAME)
public class PlayerSkillHandler {

    @SubscribeEvent
    public static void onLeftClickEmpty(PlayerInteractEvent.LeftClickEmpty event){
        Player player = event.getEntity();
        // クライアントだけで送信
        if (!player.level().isClientSide) return;

        // 手に剣を持っている時に使える
        ItemStack stack = player.getMainHandItem();
        if (!(stack.getItem() instanceof SwordItem)) return;

        PacketDistributor.sendToServer(new SpawnSlashPayload());

    }




}
