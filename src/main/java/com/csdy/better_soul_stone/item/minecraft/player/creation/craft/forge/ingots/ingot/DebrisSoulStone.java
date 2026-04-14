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

import static com.csdy.better_soul_stone.util.SoulStoneUtil.dropItemAt;

@SoulStoneItems(id = "debris_soul_stone")
public class DebrisSoulStone extends BaseSoulStone implements ISoulStoneOnBlockBreak {

    @Override
    public void onBlockBreak(BlockEvent.BreakEvent event, Player player, Level level, BlockPos pos, BlockState state, ItemStack stack) {
        if (level.isClientSide) return;
        if (state.is(Blocks.NETHERRACK) || state.is(Blocks.BASALT)) {
            ItemStack bonusDrop = new ItemStack(Items.RED_BED);
            dropItemAt(level, pos, bonusDrop);
        }
    }

}
