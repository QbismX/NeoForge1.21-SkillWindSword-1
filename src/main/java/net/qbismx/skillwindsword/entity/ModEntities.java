package net.qbismx.skillwindsword.entity;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.qbismx.skillwindsword.SkillWindSword;

import java.util.function.Supplier;

public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(Registries.ENTITY_TYPE, SkillWindSword.MODID);

    public static final Supplier<EntityType<ModSlashEntity>> SLASH =
            ENTITIES.register("slash",
                    () -> EntityType.Builder.<ModSlashEntity>of(ModSlashEntity::new, MobCategory.MISC)
                            .sized(6f,0.2f) //　当たり判定
                            .clientTrackingRange(4)
                            .updateInterval(1)
                            .build("slash")
                    );

    // 登録用のメソッド
    public static void register(IEventBus bus){
        ENTITIES.register(bus);
    }
}
