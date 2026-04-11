package com.csdy.better_soul_stone.soul_stone.soul_stone_capability;

import com.csdy.better_soul_stone.soul_stone.manager.SoulStoneManager;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;

public interface ISoulStoneOnJump extends ISoulStoneCapability {

    void onJump(LivingEntity jumper, ItemStack stack);

    static void dispatchJumpTrigger(LivingEntity jumper) {
        SoulStoneManager.forEachLogic(jumper, ISoulStoneOnJump.class, (capability, stack) -> {
            capability.onJump(jumper, stack);
        });
    }
}