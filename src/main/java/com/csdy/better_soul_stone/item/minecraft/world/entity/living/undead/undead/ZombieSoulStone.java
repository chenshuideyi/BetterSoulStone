package com.csdy.better_soul_stone.item.minecraft.world.entity.living.undead.undead;

import com.csdy.better_soul_stone.annotation.SoulStoneItems;
import com.csdy.better_soul_stone.item.BaseSoulStone;
import com.csdy.better_soul_stone.soul_stone.soul_stone_capability.ISoulStoneOnDeath;
import com.csdy.better_soul_stone.soul_stone.soul_stone_capability.ISoulStoneOnEat;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

@SoulStoneItems(id = "zombie_soul_stone", droppedBy = "minecraft:zombie", chance = 0.05)
public class ZombieSoulStone extends BaseSoulStone implements ISoulStoneOnEat {

    @Override
    public void onEat(LivingEntity entity, ItemStack foodStack, ItemStack soulStone) {
        if (foodStack.is(Items.ROTTEN_FLESH)) {
            entity.removeEffect(MobEffects.HUNGER);
            entity.heal(2.0F);
            entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 300, 0));

        }
    }

}
