package com.csdy.better_soul_stone.item.minecraft.world.level.overworld.terra.ores.ore;

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

@SoulStoneItems(id = "redstone_ore_soul_stone")
public class RedstoneOreSoulStone extends BaseSoulStone implements ISoulStoneOnBlockBreak {

    @Override
    public void onBlockBreak(BlockEvent.BreakEvent event, Player player, Level level, BlockPos pos, BlockState state, ItemStack stack) {
        if (level.isClientSide) return;
        if (state.is(Blocks.POWERED_RAIL)) {
            ItemStack bonusDrop = new ItemStack(Items.POWERED_RAIL);
            dropItemAt(level, pos, bonusDrop);
        }
    }

}
