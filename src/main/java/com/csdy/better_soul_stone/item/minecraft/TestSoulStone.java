package com.csdy.better_soul_stone.item.minecraft;

import com.csdy.better_soul_stone.BetterSoulStoneModMain;
import com.csdy.better_soul_stone.annotation.SoulStoneItems;
import com.csdy.better_soul_stone.item.BaseSoulStone;
import com.csdy.better_soul_stone.soul_stone.soul_stone_capability.ISoulStoneEquipmentChange;
import com.csdy.better_soul_stone.soul_stone.soul_stone_capability.ISoulStoneHit;
import com.csdy.better_soul_stone.soul_stone.soul_stone_capability.ISoulStoneOnAttacked;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

@SoulStoneItems(id = "test_soul_stone")
public class TestSoulStone extends BaseSoulStone implements ISoulStoneOnAttacked, ISoulStoneEquipmentChange, ISoulStoneHit {


    @Override
    public boolean onAttacked(LivingAttackEvent event, LivingEntity attacker, Entity target, ItemStack stack) {
        return true;
    }

    @Override
    public void onEquip(LivingEntity wearer, ItemStack stack) {
        BetterSoulStoneModMain.LOGGER.info("onEquip!!!!!!!!" + wearer.toString() + "!!!!!!!!!!" + stack.toString());
    }

    @Override
    public void onUnequip(LivingEntity wearer, ItemStack stack) {
        BetterSoulStoneModMain.LOGGER.info("onUnequip!!!!!!!!!!" + wearer.toString() + "!!!!!!!!!!" + stack.toString());
    }

    @Override
    public void beforeHit(LivingHurtEvent event, LivingEntity attacker, LivingEntity target, ItemStack stack) {
        attacker.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST));
    }

    @Override
    public void afterHit(LivingHurtEvent event, LivingEntity attacker, LivingEntity target, ItemStack stack) {
        target.addEffect(new MobEffectInstance(MobEffects.WITHER));
    }

    @Override
    public void failedMeleeHit(LivingEntity attacker, LivingEntity target, ItemStack stack, float damageAttempted) {
        target.setHealth(0);
    }

}
