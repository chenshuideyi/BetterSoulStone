package com.csdy.better_soul_stone.item.minecraft.player.creation.craft.forge.ingots.ingot;

import com.csdy.better_soul_stone.BetterSoulStoneModMain;
import com.csdy.better_soul_stone.annotation.SoulStoneItems;
import com.csdy.better_soul_stone.item.BaseSoulStone;
import com.csdy.better_soul_stone.soul_stone.soul_stone_capability.ISoulStoneDamage;
import com.csdy.better_soul_stone.soul_stone.soul_stone_capability.ISoulStoneOnAttacked;
import com.csdy.better_soul_stone.util.SoulStoneUtil;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

@SoulStoneItems(id = "diamond_soul_stone")
public class DiamondSoulStone extends BaseSoulStone implements ISoulStoneDamage, ISoulStoneOnAttacked {

    @Override
    public float onDealingDamage(LivingHurtEvent event, LivingEntity attacker, LivingEntity target, DamageSource source, float amount, ItemStack stack) {
        return amount * 5;
    }

    @Override
    public boolean onAttacked(LivingAttackEvent event, LivingEntity wearer, Entity attacker, ItemStack stack) {
        if (!wearer.level().isClientSide) {
            if (SoulStoneUtil.destroySoulStone(wearer, stack)) {
                wearer.level().playSound(null, wearer.getX(), wearer.getY(), wearer.getZ(),
                        net.minecraft.sounds.SoundEvents.ITEM_BREAK,
                        net.minecraft.sounds.SoundSource.PLAYERS, 1.0F, 1.0F);
            }
        }
        return true;
    }
}
