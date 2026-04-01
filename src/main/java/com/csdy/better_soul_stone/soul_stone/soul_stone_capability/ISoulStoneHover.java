package com.csdy.better_soul_stone.soul_stone.soul_stone_capability;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import com.csdy.better_soul_stone.soul_stone.manager.SoulStoneManager;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public interface ISoulStoneHover extends ISoulStoneCapability {

    /**
     * 当玩家准星指向某个实体时触发
     * 非玩家生物则对仇恨目标触发
     */

    void onHoverEntity(LivingEntity wearer, Entity target, ItemStack stack);

    static void dispatchHoverTrigger(LivingEntity entity) {
        if (entity.level().isClientSide || entity.tickCount % 2 != 0) return;

        Entity target = null;

        if (entity instanceof Player player) {
            target = getLookingAtEntity(player, 20.0);
        } else if (entity instanceof Mob mob) {
            target = mob.getTarget();
        }

        if (target != null) {
            Entity finalTarget = target;
            SoulStoneManager.forEachStone(entity, ISoulStoneHover.class, (logic, stack) ->
                    logic.onHoverEntity(entity, finalTarget, stack));
        }
    }

    private static Entity getLookingAtEntity(Player player, double range) {
        Vec3 eyePos = player.getEyePosition(1.0F);
        Vec3 viewVec = player.getViewVector(1.0F).scale(range);
        Vec3 reachVec = eyePos.add(viewVec);

        AABB searchBox = player.getBoundingBox().expandTowards(viewVec).inflate(1.0D);

        EntityHitResult hitResult = ProjectileUtil.getEntityHitResult(
                player.level(),
                player,
                eyePos,
                reachVec,
                searchBox,
                e -> !e.isSpectator() && e.isPickable() && e != player
        );

        return (hitResult != null) ? hitResult.getEntity() : null;
    }

}
