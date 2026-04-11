package com.csdy.better_soul_stone.soul_stone.soul_stone_capability;

import com.csdy.better_soul_stone.soul_stone.manager.SoulStoneManager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;

public interface ISoulStoneOnJump extends ISoulStoneCapability {

    void onJump(Player player, ItemStack stack);

    static void dispatchJumpTrigger(Player player) {
        SoulStoneManager.forEachStone(player, ISoulStoneOnJump.class, (capability, stack) -> {
            capability.onJump(player, stack);
        });
    }
}