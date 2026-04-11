package com.csdy.better_soul_stone.soul_stone.soul_stone_capability;

import com.csdy.better_soul_stone.soul_stone.manager.SoulStoneManager;
import net.minecraft.world.entity.player.Player;

public interface ISoulStoneOnRespawn extends ISoulStoneCapability {

    /**
     * 玩家重生时触发
     * @param player 重生的玩家
     */
    default void onRespawn(Player player) {
    }

    /**
     * 调度重生触发
     * @param player 重生的玩家
     */
    static void dispatchRespawnTrigger(Player player) {
        SoulStoneManager.forEachLogic(player, ISoulStoneOnRespawn.class, (capability, stack) -> {
            capability.onRespawn(player);
        });
    }
}
