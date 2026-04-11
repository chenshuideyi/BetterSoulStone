package com.csdy.better_soul_stone.soul_stone.soul_stone_capability;

import com.csdy.better_soul_stone.soul_stone.manager.SoulStoneManager;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public interface ISoulStoneTick extends ISoulStoneCapability {

    void onTick(ItemStack stack, LivingEntity entity);

    static void dispatch(LivingEntity entity) {
        SoulStoneManager.forEachLogic(entity, ISoulStoneTick.class, (logic, stack) ->
                logic.onTick(stack, entity));
    }

}
