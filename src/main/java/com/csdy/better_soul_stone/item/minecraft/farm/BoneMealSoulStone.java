package com.csdy.better_soul_stone.item.minecraft.farm;

import com.csdy.better_soul_stone.annotation.SoulStoneItems;
import com.csdy.better_soul_stone.item.BaseSoulStone;
import com.csdy.better_soul_stone.soul_stone.soul_stone_capability.ISoulStoneDoubleClick;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.List;
import java.util.Optional;

@SoulStoneItems(id = "bone_meal_soul_stone")
public class BoneMealSoulStone extends BaseSoulStone implements ISoulStoneDoubleClick {

    @Override
    public void onDoubleClick(ItemStack stack, Player player, String keyType) {
        if ("key.sneak".equals(keyType)) {
            Level level = player.level();
            if (!level.isClientSide) {
                BlockPos center = player.blockPosition();
                for (BlockPos pos : BlockPos.betweenClosed(center.offset(-2, -1, -2), center.offset(2, 1, 2))) {
                    applyGrowthEffect(level, pos.immutable(), player);
                }
            }
        }
    }


    private void applyGrowthEffect(Level level, BlockPos pos, Player player) {
        BlockState state = level.getBlockState(pos);
        Block block = state.getBlock();
        boolean success = false;

        if (block instanceof SugarCaneBlock || block instanceof CactusBlock) {
            for (int i = 1; i < 3; i++) {
                BlockPos target = pos.above(i);
                if (level.isEmptyBlock(target)) {
                    level.setBlockAndUpdate(target, block.defaultBlockState());
                    success = true;
                    break;
                }
            }
        }
        else if (block instanceof StemBlock) {
            int age = state.getValue(StemBlock.AGE);
            if (age < StemBlock.MAX_AGE) {
                level.setBlockAndUpdate(pos, state.setValue(StemBlock.AGE, age + 1));
                success = true;
            } else {
                Block fruitBlock = (block == Blocks.PUMPKIN_STEM) ? Blocks.PUMPKIN : Blocks.MELON;
                boolean fruitFound = false;

                for (Direction dir : Direction.Plane.HORIZONTAL) {
                    BlockPos fruitPos = pos.relative(dir);
                    if (level.getBlockState(fruitPos).is(fruitBlock)) {
                        harvestAndReplant(level, fruitPos, level.getBlockState(fruitPos), player, false);
                        fruitFound = true;
                        success = true;
                        break;
                    }
                }

                if (!fruitFound) {
                    for (Direction dir : Direction.Plane.HORIZONTAL) {
                        BlockPos targetPos = pos.relative(dir);
                        if (level.isEmptyBlock(targetPos) && level.getBlockState(targetPos.below()).canOcclude()) {
                            level.setBlockAndUpdate(targetPos, fruitBlock.defaultBlockState());
                            success = true;
                            break;
                        }
                    }
                }
            }
        }
        else {
            Optional<Property<?>> agePropOpt = state.getProperties().stream()
                    .filter(p -> p.getName().equals("age") && p instanceof IntegerProperty)
                    .findFirst();

            if (agePropOpt.isPresent()) {
                IntegerProperty ageProp = (IntegerProperty) agePropOpt.get();
                int maxAge = ageProp.getPossibleValues().stream().mapToInt(v -> v).max().orElse(0);
                int currentAge = state.getValue(ageProp);

                if (currentAge >= maxAge) {
                    harvestAndReplant(level, pos, state, player, true);
                    success = true;
                } else {
                    level.setBlockAndUpdate(pos, state.setValue(ageProp, currentAge + 1));
                    success = true;
                }
            }
        }

        if (!success) {
            ItemStack dummyBoneMeal = new ItemStack(Items.BONE_MEAL);
            if (net.minecraft.world.item.BoneMealItem.applyBonemeal(dummyBoneMeal, level, pos, player)) {
                success = true;
            }
        }

        if (success && level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.HAPPY_VILLAGER,
                    pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 3, 0.2, 0.2, 0.2, 0.05);
        }

    }

    private void harvestAndReplant(Level level, BlockPos pos, BlockState state, Player player, boolean isReplant) {
        if (!(level instanceof ServerLevel serverLevel)) return;

        List<ItemStack> drops = Block.getDrops(state, serverLevel, pos, null, player, player.getMainHandItem());
        for (ItemStack drop : drops) {
            Block.popResource(level, pos, drop);
        }

        serverLevel.sendParticles(new net.minecraft.core.particles.BlockParticleOption(ParticleTypes.BLOCK, state),
                pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 5, 0.1, 0.1, 0.1, 0.05);

        if (isReplant) {
            Optional<Property<?>> agePropOpt = state.getProperties().stream()
                    .filter(p -> p.getName().equals("age") && p instanceof IntegerProperty)
                    .findFirst();
            agePropOpt.ifPresent(property ->
                    level.setBlockAndUpdate(pos, state.setValue((IntegerProperty) property, 0))
            );
        } else {
            level.removeBlock(pos, false);
        }
    }
}