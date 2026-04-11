package com.csdy.better_soul_stone.soul_stone.soul_stone_capability;

import com.csdy.better_soul_stone.soul_stone.manager.SoulStoneManager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingFallEvent;

public interface ISoulStoneOnFall extends ISoulStoneCapability {

    void onFall(Player player, float distance, float damageMultiplier, ItemStack stack);

    static void dispatchFallTrigger(Player player, float distance, float damageMultiplier) {
        SoulStoneManager.forEachStone(player, ISoulStoneOnFall.class, (capability, stack) -> {
            capability.onFall(player, distance, damageMultiplier, stack);
        });
    }
}