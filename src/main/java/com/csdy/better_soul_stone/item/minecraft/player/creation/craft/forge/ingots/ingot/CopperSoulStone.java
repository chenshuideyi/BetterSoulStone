package com.csdy.better_soul_stone.item.minecraft.player.creation.craft.forge.ingots.ingot;

import com.csdy.better_soul_stone.annotation.SoulStoneItems;
import com.csdy.better_soul_stone.item.BaseSoulStone;
import com.csdy.better_soul_stone.soul_stone.soul_stone_capability.ISoulStonePickUpItem;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

@SoulStoneItems(id = "copper_soul_stone")
public class CopperSoulStone extends BaseSoulStone implements ISoulStonePickUpItem {

    @Override
    public void onPickUp(ItemStack soulStoneStack, LivingEntity entity, ItemEntity itemEntity) {
        if (entity.level().isClientSide) return;
        ItemStack pickedStack = itemEntity.getItem();
        if (pickedStack.is(Items.RAW_COPPER)) {

            Level level = entity.level();
            EntityType.LIGHTNING_BOLT.spawn(
                    (ServerLevel) level,
                    (ItemStack) null,
                    null,
                    entity.blockPosition(),
                    MobSpawnType.TRIGGERED,
                    true,
                    true
            );

        }
    }

}
