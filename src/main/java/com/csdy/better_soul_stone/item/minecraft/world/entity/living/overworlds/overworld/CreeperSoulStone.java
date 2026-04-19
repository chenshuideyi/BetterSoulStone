package com.csdy.better_soul_stone.item.minecraft.world.entity.living.overworlds.overworld;

import com.csdy.better_soul_stone.annotation.SoulStoneItems;
import com.csdy.better_soul_stone.item.BaseSoulStone;
import com.csdy.better_soul_stone.soul_stone.soul_stone_capability.ISoulStoneOnDeath;
import com.csdy.better_soul_stone.soul_stone.soul_stone_capability.ISoulStoneTick;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;


@SoulStoneItems(id = "creeper_soul_stone", droppedBy = "minecraft:creeper", chance = 0.05)
public class CreeperSoulStone extends BaseSoulStone implements ISoulStoneOnDeath, ISoulStoneTick {

    @Override
    public void onDeath(LivingEntity living, DamageSource source, ItemStack soulStone) {

        if (living.level().isClientSide) return;

        living.level().explode(
                living,
                null,
                null,
                living.getX(),
                living.getY(),
                living.getZ(),
                5.0F,
                false,
                net.minecraft.world.level.Level.ExplosionInteraction.NONE
        );


    }

    @Override
    public void onTick(ItemStack stack, LivingEntity entity, Level level) {
        if (level.isClientSide) return;

        MobEffectInstance currentEffect = entity.getEffect(MobEffects.MOVEMENT_SPEED);
        if (currentEffect == null || currentEffect.getDuration() < 201) {
            entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 300, 1, false, false, true));
        }
    }
}
