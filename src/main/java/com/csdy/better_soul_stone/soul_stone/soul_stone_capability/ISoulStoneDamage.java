package com.csdy.better_soul_stone.soul_stone.soul_stone_capability;

import com.csdy.better_soul_stone.soul_stone.manager.SoulStoneManager;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import top.theillusivec4.curios.api.SlotResult;

import java.util.List;

public interface ISoulStoneDamage extends ISoulStoneCapability{

    default float onDealingDamage(LivingHurtEvent event, LivingEntity attacker, LivingEntity target, DamageSource source, float amount, ItemStack stack){
        return amount;
    };


    static float dispatchDamageTrigger(LivingEntity attacker, LivingHurtEvent event, LivingEntity target, DamageSource source, float amount) {
        List<SlotResult> soulStones = SoulStoneManager.getStones(attacker, ISoulStoneDamage.class);
        if (soulStones.isEmpty()) return amount;

        float currentAmount = amount;
        for (SlotResult result : soulStones) {
            ItemStack stack = result.stack();
            if (stack.getItem() instanceof ISoulStoneDamage logic) {
                currentAmount = logic.onDealingDamage(event, attacker, target, source, currentAmount, stack);
            }
        }
        return currentAmount;
    }

}
