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

    void afterHit(LivingDamageEvent event, LivingEntity attacker, LivingEntity target, ItemStack stack);

    static void dispatchBeforeHit(LivingHurtEvent event, LivingEntity attacker, LivingEntity target) {
        SoulStoneManager.forEachStone(attacker, ISoulStoneHit.class, (logic, stack) ->
                logic.beforeHit(event, attacker, target, stack));
    }

    static void dispatchAfterHit(LivingDamageEvent event, LivingEntity attacker, LivingEntity target) {
        SoulStoneManager.forEachStone(attacker, ISoulStoneHit.class, (logic, stack) ->
                logic.afterHit(event, attacker, target, stack));
    }

}
