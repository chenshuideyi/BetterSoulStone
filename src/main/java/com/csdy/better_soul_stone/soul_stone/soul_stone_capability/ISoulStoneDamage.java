package com.csdy.better_soul_stone.soul_stone.soul_stone_capability;

import com.csdy.better_soul_stone.soul_stone.manager.SoulStoneManager;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public interface ISoulStoneDamage extends ISoulStoneCapability {

    default float onDealingDamage(LivingHurtEvent event, LivingEntity attacker, LivingEntity target, DamageSource source, float amount, ItemStack stack) {
        return amount;
    }

    static float dispatchDamageTrigger(LivingEntity attacker, LivingHurtEvent event, LivingEntity target, DamageSource source, float amount) {
        var logics = SoulStoneManager.getLogics(attacker, ISoulStoneDamage.class);
        if (logics.isEmpty()) return amount;

        float currentAmount = amount;
        for (var active : logics) {
            ISoulStoneDamage logic = (ISoulStoneDamage) active.logicItem();
            ItemStack stack = active.slotResult().stack();
            currentAmount = logic.onDealingDamage(event, attacker, target, source, currentAmount, stack);
        }
        return currentAmount;
    }
}
