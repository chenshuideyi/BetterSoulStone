package com.csdy.better_soul_stone.item.minecraft.world.entity.living.undead.undead;

import com.csdy.better_soul_stone.annotation.SoulStoneItems;
import com.csdy.better_soul_stone.item.BaseSoulStone;
import com.csdy.better_soul_stone.soul_stone.soul_stone_capability.ISoulStoneOnProjectile;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;

@SoulStoneItems(id = "skeleton_soul_stone", droppedBy = "minecraft:skeleton", chance = 0.05)
public class SkeletonSoulStone extends BaseSoulStone implements ISoulStoneOnProjectile {

    @Override
    public void onProjectileLaunch(LivingEntity shooter, AbstractArrow arrow, ItemStack soulStone) {
        arrow.setDeltaMovement(arrow.getDeltaMovement().scale(1.5D));
        arrow.setKnockback(arrow.getKnockback() + 2);
        arrow.setCritArrow(true);

    }
}
