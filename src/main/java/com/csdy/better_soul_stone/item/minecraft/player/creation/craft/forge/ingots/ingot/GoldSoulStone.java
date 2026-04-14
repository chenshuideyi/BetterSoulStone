package com.csdy.better_soul_stone.item.minecraft.player.creation.craft.forge.ingots.ingot;

import com.csdy.better_soul_stone.annotation.SoulStoneItems;
import com.csdy.better_soul_stone.item.BaseSoulStone;
import com.csdy.better_soul_stone.soul_stone.soul_stone_capability.ISoulStoneOnBlockBreak;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.level.BlockEvent;

import static com.csdy.better_soul_stone.util.SoulStoneUtil.CSDY_RANDOM;
import static com.csdy.better_soul_stone.util.SoulStoneUtil.dropItemAt;

@SoulStoneItems(id = "gold_soul_stone")
public class GoldSoulStone extends BaseSoulStone implements ISoulStoneOnBlockBreak {

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
