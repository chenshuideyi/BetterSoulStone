package com.csdy.better_soul_stone.soul_stone.soul_stone_capability;

import com.csdy.better_soul_stone.soul_stone.manager.SoulStoneManager;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface ISoulStoneTick extends ISoulStoneCapability {

    void onTick(ItemStack stack, LivingEntity entity, Level level);

    static void dispatch(LivingEntity entity) {
        SoulStoneManager.forEachLogic(entity, ISoulStoneTick.class, (logic, stack) ->
                logic.onTick(stack, entity,entity.level()));
    }

}
