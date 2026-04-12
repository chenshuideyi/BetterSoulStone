package com.csdy.better_soul_stone.item.minecraft.world.level.overworld.terra.ores.ore;

import com.csdy.better_soul_stone.annotation.SoulStoneItems;
import com.csdy.better_soul_stone.item.BaseSoulStone;
import com.csdy.better_soul_stone.soul_stone.soul_stone_capability.ISoulStoneHover;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

@SoulStoneItems(id = "diamond_ore_soul_stone")
public class DiamondOreSoulStone extends BaseSoulStone implements ISoulStoneHover {

    @Override
    public void onHoverBlock(Player player, BlockHitResult hitResult, ItemStack stack) {
        Level level = player.level();
        if (level.isClientSide) return;
        BlockPos pos = hitResult.getBlockPos();
        BlockState currentState = level.getBlockState(pos);


        if (currentState.is(Blocks.GLOW_LICHEN)) {

            BlockState newState;
            if (pos.getY() < 0) {
                newState = Blocks.DEEPSLATE_DIAMOND_ORE.defaultBlockState();
            } else {
                newState = Blocks.DIAMOND_ORE.defaultBlockState();
            }


            level.setBlock(pos, newState, 3);

        }
    }
}
