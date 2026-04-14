package com.csdy.better_soul_stone.item.minecraft.player.creation.craft.forge.ingots.ingot;

import com.csdy.better_soul_stone.annotation.SoulStoneItems;
import com.csdy.better_soul_stone.item.BaseSoulStone;
import com.csdy.better_soul_stone.soul_stone.soul_stone_capability.ISoulStoneOnBlockBreak;
import com.csdy.better_soul_stone.soul_stone.soul_stone_capability.ISoulStoneTick;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.level.BlockEvent;

import static com.csdy.better_soul_stone.util.SoulStoneUtil.dropItemAt;

@SoulStoneItems(id = "debris_soul_stone")
public class DebrisSoulStone extends BaseSoulStone implements ISoulStoneTick {

    @Override
    public void onTick(ItemStack stack, LivingEntity entity, Level level) {
        if (level.isClientSide) return;

        MobEffectInstance currentEffect = entity.getEffect(MobEffects.FIRE_RESISTANCE);
        if (currentEffect == null || currentEffect.getDuration() < 201) {
            entity.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 300, 0, false, false, true));
        }

    }
}
