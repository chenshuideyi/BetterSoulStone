package com.csdy.better_soul_stone.item.minecraft.player.creation.craft.forge.ingots.ingot;

import com.csdy.better_soul_stone.annotation.SoulStoneItems;
import com.csdy.better_soul_stone.item.BaseSoulStone;
import com.csdy.better_soul_stone.soul_stone.soul_stone_capability.ISoulStoneOnBlockBreak;
import com.csdy.better_soul_stone.soul_stone.soul_stone_capability.ISoulStoneTick;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.event.level.BlockEvent;

import static com.csdy.better_soul_stone.util.SoulStoneUtil.dropItemAt;

@SoulStoneItems(id = "redstone_soul_stone")
public class RedstoneSoulStone extends BaseSoulStone implements ISoulStoneTick {

    @Override
    public void onTick(ItemStack stack, LivingEntity entity, Level level) {
        if (level.isClientSide) return;

        BlockPos pos = entity.blockPosition().below();
        BlockState state = level.getBlockState(pos);

        if (isRedstoneComponent(state)) {
            BlockState newState = tryPowerUp(state);

            if (newState != state) {
                level.setBlock(pos, newState, 3);
            }

        }
    }

    /**
     * 判断方块是否具有红石相关的状态属性
     */
    private boolean isRedstoneComponent(BlockState state) {
        return state.hasProperty(BlockStateProperties.POWER) ||
                state.hasProperty(BlockStateProperties.POWERED) ||
                state.hasProperty(BlockStateProperties.LIT);
    }

    /**
     * 根据方块属性尝试提升其能量等级
     */
    private BlockState tryPowerUp(BlockState state) {
        BlockState result = state;

        if (state.hasProperty(BlockStateProperties.POWER)) {
            result = result.setValue(BlockStateProperties.POWER, 15);
        }

        if (state.hasProperty(BlockStateProperties.POWERED)) {
            result = result.setValue(BlockStateProperties.POWERED, true);
        }

        if (state.hasProperty(BlockStateProperties.LIT)) {
            result = result.setValue(BlockStateProperties.LIT, true);
        }

        return result;
    }


}
