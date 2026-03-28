package com.csdy.better_soul_stone.soul_stone.soul_stone_capability;

import com.csdy.better_soul_stone.soul_stone.manager.SoulStoneManager;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import top.theillusivec4.curios.api.SlotResult;

import java.util.List;

public interface ISoulStoneHit extends ISoulStoneCapability {

    void beforeHit(LivingHurtEvent event, LivingEntity attacker, LivingEntity target, ItemStack stack);

    void afterHit(LivingHurtEvent event, LivingEntity attacker, LivingEntity target, ItemStack stack);

    void failedMeleeHit(LivingEntity attacker, LivingEntity target, ItemStack stack, float damageAttempted);

    /**
     * 在 LivingAttackEvent 中调用，用来捕捉那些“本该发生但没发生的伤害”
     */
    static void checkAttackFailure(LivingAttackEvent event) {
        if (!(event.getSource().getEntity() instanceof LivingEntity attacker)) return;
        LivingEntity target = event.getEntity();

        boolean isFailed = false;
        if (target.invulnerableTime > 10) isFailed = true;
        if (target.isInvulnerable()) isFailed = true;
        if (event.isCanceled()) isFailed = true;

        if (isFailed) {
            List<SlotResult> stones = SoulStoneManager.getStones(attacker, ISoulStoneHit.class);
            for (SlotResult result : stones) {
                if (result.stack().getItem() instanceof ISoulStoneHit logic) {
                    logic.failedMeleeHit(attacker, target, result.stack(), event.getAmount());
                }
            }
        }
    }

    static void dispatchAfterHit(LivingDamageEvent event) {
        if (!(event.getSource().getEntity() instanceof LivingEntity attacker) || attacker.level().isClientSide) return;
        LivingEntity target = event.getEntity();

        List<SlotResult> stones = SoulStoneManager.getStones(attacker, ISoulStoneHit.class);
        for (SlotResult result : stones) {
            ItemStack stack = result.stack();
            if (stack.getItem() instanceof ISoulStoneHit logic) {

                logic.afterHit(null, attacker, target, stack);
            }
        }
    }

}
