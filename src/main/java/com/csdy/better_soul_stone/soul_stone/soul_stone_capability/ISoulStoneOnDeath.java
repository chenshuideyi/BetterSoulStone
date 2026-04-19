package com.csdy.better_soul_stone.soul_stone.soul_stone_capability;

import com.csdy.better_soul_stone.soul_stone.manager.SoulStoneManager;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public interface ISoulStoneOnDeath extends ISoulStoneCapability {

    void onDeath(LivingEntity living, DamageSource source, ItemStack soulStone);

    static void dispatchDeathTrigger(LivingEntity living, DamageSource source) {
        if (living == null) return;

        SoulStoneManager.forEachLogic(living, ISoulStoneOnDeath.class, (capability, stack) -> {
            capability.onDeath(living, source, stack);
        });
    }

}
