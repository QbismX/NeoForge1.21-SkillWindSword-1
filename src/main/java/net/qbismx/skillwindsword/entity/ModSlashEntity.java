package net.qbismx.skillwindsword.entity;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import net.qbismx.skillwindsword.calculate.ModMath;
import net.qbismx.skillwindsword.registry.ModParticles;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ModSlashEntity extends Projectile {

    private static final int TRAIL_LENGTH = 6;
    private final Vec3[] trail = new Vec3[TRAIL_LENGTH];
    private final Set<Integer> hitEntities = new HashSet<>(); // EntityIDだけ保存
    private int trailIndex = 0;
    private static final double LENGTH = 3.0; // 斬撃の長さ
    private static final double WIDTH = 3.0;  // 斬撃の横幅
    private Vec3 prevPos;


    public ModSlashEntity(EntityType<? extends ModSlashEntity> entityType, Level level) {
        super(entityType, level);
    }

    public ModSlashEntity(Level level, LivingEntity owner) {
        this(ModEntities.SLASH.get(), level);
        this.setOwner(owner);
    }

    // コマンドで呼び出された斬撃用のコンストラクタ
    public ModSlashEntity(Level level, Vec3 pos, Vec3 dir, ServerPlayer player) {
        this(ModEntities.SLASH.get(), level);
        this.setPos(pos);
        Vec3 velocity = dir.normalize().scale(0.5);
        this.setDeltaMovement(velocity);
        this.setOwner(player);
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

        // 前tickの位置を保存
        if (prevPos == null) {
            prevPos = position();
        }

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
            // 攻撃したプレイヤー
            Entity owner = getOwner();
            if (owner != null) {

                //　当たり判定計算の空間の設定
                AABB box = getBoundingBox().inflate(LENGTH);

                // ターゲットの候補をまとめる
                List<LivingEntity> targets = level().getEntitiesOfClass(LivingEntity.class, box);

                // 斬撃の傾き
                Vec3 forward = getDeltaMovement().normalize(); // 進行方向
                Vec3 side = forward.cross(new Vec3(0, 1, 0)); // 進行方向と直交する横ベクトルを作る。(斬撃テクスチャの平面の傾き)

                // 斬撃の中心点
                Vec3 slashP = position().add(forward.scale(1.5));
                // 過去の斬撃の中心点
                Vec3 prevP = prevPos.add(forward.scale(1.5));

                // 斬撃の両端点
                Vec3 leftP = slashP.add(side.scale(-WIDTH));
                Vec3 rightP = slashP.add(side.scale(WIDTH));
                // 過去の斬撃の両端点
                Vec3 prevL = prevP.add(side.scale(-WIDTH));
                Vec3 prevR = prevP.add(side.scale(WIDTH));

                // 各ターゲットの当たり判定を計算
                for (LivingEntity target : targets) {

                    if (target == owner) continue;
                    if (hitEntities.contains(target.getId())) continue;

                    AABB enemy = target.getBoundingBox();

                    // 分離軸定理を用いる　視点は多くとも10個。
                    // 全ての視点で重なっていたら、ダメージ。
                    // どれか１つの視点で重なっていなかったら、スルー
                    if (ModMath.intersectRectAABB(leftP, rightP, prevR, prevL, enemy)) {

                        // 盾確認
                        if (target instanceof Player player && player.isBlocking()) {
                            Vec3 look = player.getLookAngle().normalize();
                            Vec3 toSlash = player.position().subtract(this.position()).normalize();

                            if (look.dot(toSlash) < -0.5) {
                                this.discard();
                                return;
                            }

                        } else {
                                // ダメージ判定
                                target.hurt(
                                        damageSources().mobAttack((LivingEntity) owner),
                                        2.0F
                                );
                                hitEntities.add(target.getId());
                        }
                    }// 当たり判定計算終了

                } // 各ターゲットの当たり判定計算終了

            }
        }

        if (tickCount > 30) this.discard(); // tick=41で消える

        prevPos = position(); // 位置の保存
        // 移動
        Vec3 v = getDeltaMovement();
        setPos(getX() + v.x, getY() + v.y, getZ() + v.z);

    }
}
