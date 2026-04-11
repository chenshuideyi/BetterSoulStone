package com.csdy.better_soul_stone.soul_stone.soul_stone_capability;

import com.csdy.better_soul_stone.soul_stone.manager.SoulStoneManager;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface ISoulStoneOnChat extends ISoulStoneCapability {

    void onChat(Player player, Component message, ItemStack soulStone);

    static void dispatchChatTrigger(Player player, Component message) {
        SoulStoneManager.forEachLogic(player, ISoulStoneOnChat.class, (capability, stack) -> {
            capability.onChat(player, message, stack);
        });
    }
}