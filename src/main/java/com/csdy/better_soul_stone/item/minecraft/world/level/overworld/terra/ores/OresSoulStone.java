package com.csdy.better_soul_stone.item.minecraft.world.level.overworld.terra.ores;

import com.csdy.better_soul_stone.annotation.SoulStoneItems;
import com.csdy.better_soul_stone.item.BaseSoulStone;
import com.csdy.better_soul_stone.soul_stone.soul_stone_capability.ISoulStoneHover;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@SoulStoneItems(
        id = "ores_soul_stone",
        parentIds = {
                "iron_ore_soul_stone",
                "gold_ore_soul_stone",
                "copper_ore_soul_stone",
                "diamond_ore_soul_stone",
                "coal_ore_soul_stone",
                "emerald_ore_soul_stone",
                "lapis_ore_soul_stone",
                "redstone_ore_soul_stone",
                "debris_ore_soul_stone"
        },
        scale = 1.5f,
        chance = 1,
        droppedBy = "minecraft:zombie"
)
public class OresSoulStone extends BaseSoulStone implements ISoulStoneHover {

    private static final Map<Block, Block> TRANSFORMATION_MAP = new HashMap<>();

    //考虑到巨大多喝水mcr模组写的矿压根就不是矿，我没什么支持其他模组的理由
    static {

        TRANSFORMATION_MAP.put(Blocks.IRON_ORE, Blocks.IRON_BLOCK);
        TRANSFORMATION_MAP.put(Blocks.DEEPSLATE_IRON_ORE, Blocks.IRON_BLOCK);

        TRANSFORMATION_MAP.put(Blocks.GOLD_ORE, Blocks.GOLD_BLOCK);
        TRANSFORMATION_MAP.put(Blocks.DEEPSLATE_GOLD_ORE, Blocks.GOLD_BLOCK);

        TRANSFORMATION_MAP.put(Blocks.COPPER_ORE, Blocks.COPPER_BLOCK);
        TRANSFORMATION_MAP.put(Blocks.DEEPSLATE_COPPER_ORE, Blocks.COPPER_BLOCK);

        TRANSFORMATION_MAP.put(Blocks.DIAMOND_ORE, Blocks.DIAMOND_BLOCK);
        TRANSFORMATION_MAP.put(Blocks.DEEPSLATE_DIAMOND_ORE, Blocks.DIAMOND_BLOCK);

        TRANSFORMATION_MAP.put(Blocks.EMERALD_ORE, Blocks.EMERALD_BLOCK);
        TRANSFORMATION_MAP.put(Blocks.DEEPSLATE_EMERALD_ORE, Blocks.EMERALD_BLOCK);

        TRANSFORMATION_MAP.put(Blocks.COAL_ORE, Blocks.COAL_BLOCK);
        TRANSFORMATION_MAP.put(Blocks.DEEPSLATE_COAL_ORE, Blocks.COAL_BLOCK);

        TRANSFORMATION_MAP.put(Blocks.LAPIS_ORE, Blocks.LAPIS_BLOCK);
        TRANSFORMATION_MAP.put(Blocks.DEEPSLATE_LAPIS_ORE, Blocks.LAPIS_BLOCK);

        TRANSFORMATION_MAP.put(Blocks.REDSTONE_ORE, Blocks.REDSTONE_BLOCK);
        TRANSFORMATION_MAP.put(Blocks.DEEPSLATE_REDSTONE_ORE, Blocks.REDSTONE_BLOCK);
    }

    @Override
    public void onHoverBlock(Player player, BlockHitResult hitResult, ItemStack stack) {
        Level level = player.level();
        if (level.isClientSide || this.isOnCooldown(player, stack)) return;

        BlockPos pos = hitResult.getBlockPos();
        BlockState state = level.getBlockState(pos);
        Block targetBlock = state.getBlock();

        if (TRANSFORMATION_MAP.containsKey(targetBlock)) {
            Block resultBlock = TRANSFORMATION_MAP.get(targetBlock);
            level.setBlock(pos, resultBlock.defaultBlockState(), 3);
            var sLevel = (ServerLevel) level;
            double x = pos.getX() + 0.5;
            double y = pos.getY() + 0.5;
            double z = pos.getZ() + 0.5;

            sLevel.sendParticles(ParticleTypes.FLAME, x, y, z, 20, 0.3, 0.3, 0.3, 0.05);
            sLevel.sendParticles(ParticleTypes.LARGE_SMOKE, x, y + 0.5, z, 5, 0.2, 0.2, 0.2, 0.02);
            sLevel.sendParticles(ParticleTypes.LAVA, x, y, z, 3, 0.4, 0.4, 0.4, 0.1);
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public String getSoulStoneType() {
        return "better";
    }


}
