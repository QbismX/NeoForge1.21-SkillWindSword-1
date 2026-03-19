package net.qbismx.skillwindsword.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import net.qbismx.skillwindsword.entity.ModSlashEntity;

public class ShootSlashCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("shootslash")
                        .requires(source -> source.hasPermission(2))
                        .executes(ctx -> {
                            ServerLevel level = ctx.getSource().getLevel();

                            // 個定位置
                            Vec3 pos = new Vec3(0, -59, 0); // フラットワールド用, 座標(0, -59, 0)から斬撃を放つ
                            // 進行方向
                            Vec3 dir = new Vec3(1, 0, 0);

                            if (!level.isClientSide) {
                                ServerPlayer player = ctx.getSource().getPlayer();
                                level.addFreshEntity(new ModSlashEntity(level, pos, dir, player));
                            }

                            return 1;
                        })
        );
    }
}
