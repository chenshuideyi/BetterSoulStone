package com.csdy.better_soul_stone.item.minecraft.world.level.overworld.terra.ores.ore;

import com.csdy.better_soul_stone.annotation.SoulStoneItems;
import com.csdy.better_soul_stone.item.BaseSoulStone;
import com.csdy.better_soul_stone.soul_stone.soul_stone_capability.ISoulStoneDamage;
import com.csdy.better_soul_stone.soul_stone.soul_stone_capability.ISoulStoneOnAttacked;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

@SoulStoneItems(id = "diamond_ore_soul_stone")
public class DiamondOreSoulStone extends BaseSoulStone implements ISoulStoneDamage, ISoulStoneOnAttacked {

    @Override
    public float onDealingDamage(LivingHurtEvent event, LivingEntity attacker, LivingEntity target, DamageSource source, float amount, ItemStack stack) {
        return amount * 5;
    }

    @Override
    public boolean onAttacked(LivingAttackEvent event, LivingEntity wearer, Entity attacker, ItemStack stack) {
        return false;
    }

}
