package net.qbismx.skillwindsword.entity;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import net.qbismx.skillwindsword.registry.ModParticles;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ModSlashEntity extends Projectile {

    private static final int TRAIL_LENGTH = 6;
    private final Vec3[] trail = new Vec3[TRAIL_LENGTH];
    private final Set<Integer> hitEntities = new HashSet<>(); // EntityIDだけ保存
    private int trailIndex = 0;
    private static final double LENGTH = 4.5; // 斬撃の長さ
    private static final double WIDTH = 3.0;  // 斬撃の横幅


    public ModSlashEntity(EntityType<? extends ModSlashEntity> entityType, Level level) {
        super(entityType, level);
    }

    public ModSlashEntity(Level level, LivingEntity owner) {
        this(ModEntities.SLASH.get(), level);
        setOwner(owner);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }

    @Override
    protected boolean canHitEntity(Entity entity) {
        return entity != this.getOwner() && !hitEntities.contains(entity.getId());
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        /*
        Entity entity = result.getEntity();
        Entity owner = getOwner();

        if (entity instanceof LivingEntity target && owner instanceof LivingEntity livingOwner) {


            target.hurt(
                    damageSources().mobAttack(livingOwner),
                    8.0F
            );

            hitEntities.add(target.getId());
        }
         */
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        // 何もしない。ブロックに当たっても貫通させたいので。
    }

    @Override
    public void tick() {
        super.tick();
        // trailを後ろへずらす
        trail[trailIndex] = position();
        trailIndex = (trailIndex + 1) % TRAIL_LENGTH;

        trail[0] = position();

        if (level().isClientSide) {
            double width = 2;
            double height = 0.8;

            for (int i = 0; i < 4; i++) {

                double x = getX() + (random.nextDouble() - 0.5) * width * 2;
                double y = getY() + (random.nextDouble() - 0.5) * height;
                double z = getZ() + (random.nextDouble() - 0.5) * width * 2;

                level().addParticle(
                        ModParticles.LIGHT.get(),
                        x, y, z,
                        0, 0, 0
                );
            }
        } else {

                AABB box = getBoundingBox().inflate(LENGTH);

                List<LivingEntity> targets =
                        level().getEntitiesOfClass(LivingEntity.class, box);

                Entity owner = getOwner();

                Vec3 forward = getDeltaMovement().normalize(); // 斬撃の向き
            Vec3 up = new Vec3(0,1, 0);
                Vec3 right = forward.cross(up); //.normalize(); // 横方向

            if (right.lengthSqr() < 0.001) {
                right = new Vec3(1, 0,0);
            }

            right = right.normalize();


                for (LivingEntity target : targets) {

                    if (target == owner) continue;
                    if (hitEntities.contains(target.getId())) continue;

                    Vec3 toTarget = target.getBoundingBox().getCenter().subtract(position());

                    double forwardDist = toTarget.dot(forward);
                    double sideDist = Math.abs(toTarget.dot(right));
                    AABB slashBox = new AABB(
                            getX() - WIDTH,
                            getY() - 0.5,
                            getZ() - WIDTH,
                            getX() + WIDTH,
                            getY() + 0.5,
                            getZ() + WIDTH
                    );

                    if (forwardDist > 0.2 &&
                            forwardDist < LENGTH &&
                            sideDist < WIDTH &&
                            slashBox.intersects(target.getBoundingBox())) {

                        target.hurt(
                                damageSources().mobAttack((LivingEntity) owner),
                                8.0F
                        );

                        hitEntities.add(target.getId());
                    }
                }
        }

        // 移動
        Vec3 v = getDeltaMovement();
        setPos(getX() + v.x, getY() + v.y, getZ() + v.z);

        if (tickCount > 20) discard();


    }

    /*
    public Vec3[] getTrail() {
        return trail;
    }
     */

}
