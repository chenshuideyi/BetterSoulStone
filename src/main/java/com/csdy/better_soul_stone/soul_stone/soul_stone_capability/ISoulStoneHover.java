package com.csdy.better_soul_stone.soul_stone.soul_stone_capability;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import com.csdy.better_soul_stone.soul_stone.manager.SoulStoneManager;
import net.minecraft.world.phys.*;

public interface ISoulStoneHover extends ISoulStoneCapability {

    /**
     * 当玩家准星指向某个实体时触发
     * 非玩家生物则对仇恨目标触发
     */

    default void onHoverEntity(LivingEntity wearer, Entity target, ItemStack stack){

    }

    /**
     * 当玩家准星指向某个方块时触发
     */

    default void onHoverBlock(Player player, BlockHitResult hitResult, ItemStack stack) {

    }

    static void dispatchHoverTrigger(LivingEntity entity) {
        if (entity.level().isClientSide || entity.tickCount % 2 != 0) return;

        Entity targetEntity = null;
        if (entity instanceof Player player) {
            targetEntity = getLookingAtEntity(player, 20.0);
        } else if (entity instanceof Mob mob) {
            targetEntity = mob.getTarget();
        }

        if (targetEntity != null) {
            Entity finalTarget = targetEntity;
            SoulStoneManager.forEachLogic(entity, ISoulStoneHover.class, (logic, stack) ->
                    logic.onHoverEntity(entity, finalTarget, stack));
        }

        if (entity instanceof Player player) {
            BlockHitResult blockHit = getLookingAtBlock(player, 12.0);
            if (blockHit != null && blockHit.getType() == HitResult.Type.BLOCK) {
                SoulStoneManager.forEachLogic(player, ISoulStoneHover.class, (logic, stack) ->
                        logic.onHoverBlock(player, blockHit, stack));
            }
        }
    }

    private static BlockHitResult getLookingAtBlock(Player player, double range) {
        Vec3 eyePos = player.getEyePosition(1.0F);
        Vec3 viewVec = player.getViewVector(1.0F).scale(range);
        Vec3 reachVec = eyePos.add(viewVec);

        return player.level().clip(new net.minecraft.world.level.ClipContext(
                eyePos,
                reachVec,
                net.minecraft.world.level.ClipContext.Block.OUTLINE,
                net.minecraft.world.level.ClipContext.Fluid.NONE,
                player
        ));
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
