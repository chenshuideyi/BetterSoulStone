package com.csdy.better_soul_stone.soul_stone.soul_stone_capability;

import com.csdy.better_soul_stone.soul_stone.manager.SoulStoneManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.level.BlockEvent;

public interface ISoulStoneOnBlockBreak extends ISoulStoneCapability {

    void onBlockBreak(BlockEvent.BreakEvent event, Player player, Level level, BlockPos pos, BlockState state, ItemStack stack);

    static void dispatch(BlockEvent.BreakEvent event, Player player) {
        Level level = player.level();
        BlockPos pos = event.getPos();
        BlockState state = level.getBlockState(pos);
        ItemStack tool = player.getMainHandItem();

        SoulStoneManager.forEachLogic(player, ISoulStoneOnBlockBreak.class, (logic, stack) ->
                logic.onBlockBreak(event, player, level, pos, state, tool));
    }
}