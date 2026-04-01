package com.csdy.better_soul_stone.item.sponsor;

import com.csdy.better_soul_stone.annotation.SoulStoneItems;
import com.csdy.better_soul_stone.item.BaseSoulStone;
import com.csdy.better_soul_stone.soul_stone.soul_stone_capability.ISoulStoneDamage;
import com.csdy.better_soul_stone.soul_stone.soul_stone_capability.ISoulStoneLivingHurt;
import com.csdy.better_soul_stone.soul_stone.soul_stone_capability.ISoulStoneOnHeal;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

@SoulStoneItems(id = "player_7_soul_stone", isSponsor = true, sponsorName = "PlayerVII")
public class PlayerVIISoulStone extends BaseSoulStone implements ISoulStoneLivingHurt, ISoulStoneDamage, ISoulStoneOnHeal {

    @Override
    public float livingHurt(LivingHurtEvent event, LivingEntity wearer, DamageSource source, float amount, ItemStack stack) {
        return amount * 4.0F;
    }

    @Override
    public float onDealingDamage(LivingHurtEvent event, LivingEntity attacker, LivingEntity target, DamageSource source, float amount, ItemStack stack) {
        float multiplier = 1.0F;
        if (attacker.getHealth() >= attacker.getMaxHealth()) {
            multiplier *= 0.5F;
        }
        if (attacker.getHealth() <= 1.0F) {
            multiplier = 2.0F;
        }
        return amount * multiplier;
    }

    @Override
    public float onHeal(LivingHealEvent event, LivingEntity entity, float amount, ItemStack stack) {
        return 0;
    }

}
