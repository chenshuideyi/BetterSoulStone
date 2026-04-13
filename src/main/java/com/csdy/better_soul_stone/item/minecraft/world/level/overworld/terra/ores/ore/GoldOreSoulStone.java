package com.csdy.better_soul_stone.item.minecraft.world.level.overworld.terra.ores.ore;

import com.csdy.better_soul_stone.annotation.SoulStoneItems;
import com.csdy.better_soul_stone.item.BaseSoulStone;
import com.csdy.better_soul_stone.soul_stone.soul_stone_capability.ISoulStoneHit;
import com.csdy.better_soul_stone.soul_stone.soul_stone_capability.ISoulStoneOnBlockBreak;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.level.BlockEvent;

import static com.csdy.better_soul_stone.util.SoulStoneUtil.*;

@SoulStoneItems(id = "gold_ore_soul_stone")
public class GoldOreSoulStone extends BaseSoulStone implements ISoulStoneOnBlockBreak {

    @Override
    public void onBlockBreak(BlockEvent.BreakEvent event, Player player, Level level, BlockPos pos, BlockState state, ItemStack stack) {
        if (level.isClientSide) return;
        if (state.is(Blocks.GOLD_ORE) || state.is(Blocks.DEEPSLATE_GOLD_ORE)) {
            ItemStack bonusDrop;

            if (CSDY_RANDOM.nextDouble() < 0.7) {
                bonusDrop = new ItemStack(Items.BLAZE_ROD);
            } else {
                bonusDrop = new ItemStack(Items.ENDER_PEARL);
            }

            dropItemAt(level, pos, bonusDrop);
        }
    }

}
