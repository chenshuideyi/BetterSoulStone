package com.csdy.better_soul_stone.item.minecraft.player.creation.craft.forge.ingots;

import com.csdy.better_soul_stone.annotation.SoulStoneItems;
import com.csdy.better_soul_stone.item.BaseSoulStone;
import com.csdy.better_soul_stone.soul_stone.soul_stone_capability.ISoulStoneTick;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

@SoulStoneItems(
        id = "ingots_soul_stone",
        parentIds = {
                "iron_soul_stone",
                "gold_soul_stone",
                "copper_soul_stone",
                "diamond_soul_stone",
                "coal_soul_stone",
                "emerald_soul_stone",
                "lapis_soul_stone",
                "redstone_soul_stone",
                "debris_soul_stone"
        },
        scale = 1.5f
)
public class IngotsSoulStone extends BaseSoulStone implements ISoulStoneTick {

    @Override
    public void onTick(ItemStack stack, LivingEntity entity, Level level) {
        if (level.isClientSide) return;
        if (entity.isOnFire()) {
            entity.clearFire();
            entity.heal(5);
        }
    }

}
