package com.csdy.better_soul_stone.soul_stone.soul_stone_capability;

import com.csdy.better_soul_stone.soul_stone.manager.SoulStoneManager;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public interface ISoulStoneLeftClick extends ISoulStoneCapability {

    default void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event, Player player, Level level, BlockPos pos, BlockState state, ItemStack stack){};

    default void onLeftClickEmpty(PlayerInteractEvent.LeftClickEmpty event, Player player, Level level, ItemStack stack){};

    default void onLeftClickEntity(AttackEntityEvent event, Player player, Entity target, ItemStack stack){};

    static void dispatchLeftClickEntity(AttackEntityEvent event) {
        SoulStoneManager.forEachLogic(event.getEntity(), ISoulStoneLeftClick.class, (logic, stack) ->
                logic.onLeftClickEntity(event, event.getEntity(), event.getTarget(), stack));
    }

    static void dispatchLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        SoulStoneManager.forEachLogic(event.getEntity(), ISoulStoneLeftClick.class, (logic, stack) ->
                logic.onLeftClickBlock(event, event.getEntity(), event.getLevel(), event.getPos(),
                        event.getLevel().getBlockState(event.getPos()), stack));
    }


}
