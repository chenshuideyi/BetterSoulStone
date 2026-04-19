package com.csdy.better_soul_stone.soul_stone.soul_stone_capability;

import com.csdy.better_soul_stone.soul_stone.manager.SoulStoneManager;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public interface ISoulStoneOnEat extends ISoulStoneCapability {

    void onEat(LivingEntity entity, ItemStack foodStack, ItemStack soulStone);

    static void dispatchEatTrigger(LivingEntity entity, ItemStack foodStack) {
        if (entity == null || foodStack.isEmpty()) return;

        SoulStoneManager.forEachLogic(entity, ISoulStoneOnEat.class, (capability, stack) -> {
            capability.onEat(entity, foodStack, stack);
        });
    }
}
