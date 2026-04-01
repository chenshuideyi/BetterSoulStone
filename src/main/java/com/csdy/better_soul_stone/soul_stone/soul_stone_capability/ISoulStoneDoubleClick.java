package com.csdy.better_soul_stone.soul_stone.soul_stone_capability;

import com.csdy.better_soul_stone.soul_stone.manager.SoulStoneManager;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface ISoulStoneDoubleClick extends ISoulStoneCapability {

    /**
     * 当双击特定功能时触发
     * @param keyType 区分是哪个按键（比如：前进、后退、或自定义快捷键）
     */

    void onDoubleClick(ItemStack stack, Player player, String keyType);

    static void dispatch(Player player, String keyType) {
        SoulStoneManager.forEachStone(player, ISoulStoneDoubleClick.class, (logic, stack) ->
                logic.onDoubleClick(stack, player, keyType));
    }
}
