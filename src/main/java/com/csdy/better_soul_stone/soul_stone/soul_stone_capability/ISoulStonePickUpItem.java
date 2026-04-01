package com.csdy.better_soul_stone.soul_stone.soul_stone_capability;

import com.csdy.better_soul_stone.soul_stone.manager.SoulStoneManager;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;

public interface ISoulStonePickUpItem extends ISoulStoneCapability {
    // 增加 itemEntity 参数，代表地面上被捡起的那个物品实体
    void onPickUp(ItemStack soulStoneStack, LivingEntity entity, ItemEntity itemEntity);

    static void dispatch(LivingEntity entity, ItemEntity itemEntity) {
        SoulStoneManager.forEachStone(entity, ISoulStonePickUpItem.class, (logic, stack) ->
                logic.onPickUp(stack, entity, itemEntity));
    }
}
