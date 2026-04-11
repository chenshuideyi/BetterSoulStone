package com.csdy.better_soul_stone.soul_stone.soul_stone_capability;

import com.csdy.better_soul_stone.BetterSoulStoneModMain;
import com.csdy.better_soul_stone.soul_stone.manager.SoulStoneManager;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import top.theillusivec4.curios.api.SlotResult;

import java.util.List;

public interface ISoulStoneOnAttacked extends ISoulStoneCapability {

    /**
     * @return 返回 false 则取消攻击（免疫）
     */
    default boolean onAttacked(LivingAttackEvent event, LivingEntity wearer, Entity attacker, ItemStack stack) {
        return true;
    }

    static boolean dispatchAttackTrigger(LivingEntity wearer, LivingAttackEvent event, Entity attacker) {
        var logics = SoulStoneManager.getLogics(wearer, ISoulStoneOnAttacked.class);
        if (logics.isEmpty()) return false;

        boolean globalCancel = false;
        for (var active : logics) {
            try {
                ISoulStoneOnAttacked logic = (ISoulStoneOnAttacked) active.logicItem();
                ItemStack stack = active.slotResult().stack();
                if (!logic.onAttacked(event, wearer, attacker, stack)) {
                    globalCancel = true;
                }
            } catch (Exception e) {
                BetterSoulStoneModMain.LOGGER.error("魂石受击逻辑执行异常", e);
            }
        }
        return globalCancel;
    }
}
