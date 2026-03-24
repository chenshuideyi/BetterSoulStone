package com.csdy.better_soul_stone.soul_stone;

import com.csdy.better_soul_stone.BetterSoulStoneModMain;
import com.csdy.better_soul_stone.soul_stone.manager.SoulStoneManager;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

import java.util.List;

public interface ISoulStoneOnAttacked {
    /**
     * @param event 原生受击事件
     * @param wearer 佩戴魂石的人（受害者）
     * @param attacker 攻击者（可能为 null）
     * @param stack 魂石堆栈
     * @return 返回 false 则取消攻击（免疫）
     */
    boolean onAttacked(LivingAttackEvent event, LivingEntity wearer, Entity attacker, ItemStack stack);


    static boolean dispatchAttackTrigger(LivingEntity wearer, LivingAttackEvent event, Entity attacker) {
        List<SlotResult> soulStones = SoulStoneManager.getStones(wearer, ISoulStoneOnAttacked.class);
        if (soulStones.isEmpty()) return false;

        boolean globalCancel = false;
        for (SlotResult result : soulStones) {
            try {
                ItemStack stack = result.stack();
                ISoulStoneOnAttacked logic = (ISoulStoneOnAttacked) stack.getItem();
                if (!logic.onAttacked(event, wearer, attacker, stack)) {
                    globalCancel = true;
                }
            } catch (Exception e) {
                BetterSoulStoneModMain.LOGGER.error("Soul Stone trigger error", e);
            }
        }
        return globalCancel;
    }
}
