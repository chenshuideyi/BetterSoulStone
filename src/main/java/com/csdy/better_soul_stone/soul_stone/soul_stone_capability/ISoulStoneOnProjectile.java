package com.csdy.better_soul_stone.soul_stone.soul_stone_capability;

import com.csdy.better_soul_stone.soul_stone.manager.SoulStoneManager;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;

public interface ISoulStoneOnProjectile extends ISoulStoneCapability {

    void onProjectileLaunch(LivingEntity shooter, AbstractArrow arrow, ItemStack soulStone);

    static void dispatchProjectileTrigger(LivingEntity shooter, AbstractArrow arrow) {
        if (shooter == null) return;
        SoulStoneManager.forEachLogic(shooter, ISoulStoneOnProjectile.class, (capability, stack) -> {
            capability.onProjectileLaunch(shooter, arrow, stack);
        });
    }
}
