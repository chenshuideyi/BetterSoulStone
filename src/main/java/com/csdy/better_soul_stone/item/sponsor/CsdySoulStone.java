package com.csdy.better_soul_stone.item.sponsor;

import com.csdy.better_soul_stone.annotation.SoulStoneItems;
import com.csdy.better_soul_stone.item.BaseSoulStone;
import com.csdy.better_soul_stone.soul_stone.soul_stone_capability.ISoulStoneDamage;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

@SoulStoneItems(id = "csdy_soul_stone",isSponsor = true,sponsorName = "CSDY")
public class CsdySoulStone extends BaseSoulStone implements ISoulStoneDamage {

    @Override
    public float onDealingDamage(LivingHurtEvent event, LivingEntity attacker, LivingEntity target, DamageSource source, float amount, ItemStack stack) {
        if (attacker.getMainHandItem().isEmpty()) return amount * 5;
        return amount;
    }

}
