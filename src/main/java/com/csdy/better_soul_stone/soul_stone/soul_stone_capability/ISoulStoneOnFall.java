package com.csdy.better_soul_stone.soul_stone.soul_stone_capability;

import com.csdy.better_soul_stone.soul_stone.manager.SoulStoneManager;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingFallEvent;

public interface ISoulStoneOnFall extends ISoulStoneCapability {

    void onFall(LivingEntity faller, float distance, float damageMultiplier, ItemStack stack);

    static void dispatchFallTrigger(LivingEntity faller, float distance, float damageMultiplier) {
        SoulStoneManager.forEachStone(faller, ISoulStoneOnFall.class, (capability, stack) -> {
            capability.onFall(faller, distance, damageMultiplier, stack);
        });
    }
}