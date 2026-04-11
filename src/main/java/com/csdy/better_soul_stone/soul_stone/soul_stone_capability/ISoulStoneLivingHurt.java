package com.csdy.better_soul_stone.soul_stone.soul_stone_capability;

import com.csdy.better_soul_stone.soul_stone.manager.SoulStoneManager;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public interface ISoulStoneLivingHurt extends ISoulStoneCapability {

    float livingHurt(LivingHurtEvent event, LivingEntity wearer, DamageSource source, float amount, ItemStack stack);

    static float dispatchHurtTrigger(LivingEntity wearer, LivingHurtEvent event, DamageSource source, float amount) {
        var logics = SoulStoneManager.getLogics(wearer, ISoulStoneLivingHurt.class);
        if (logics.isEmpty()) return amount;

        float currentAmount = amount;

        for (var active : logics) {
            ISoulStoneLivingHurt logic = (ISoulStoneLivingHurt) active.logicItem();
            ItemStack stack = active.slotResult().stack();
            currentAmount = logic.livingHurt(event, wearer, source, currentAmount, stack);
        }

        return currentAmount;
    }
}
