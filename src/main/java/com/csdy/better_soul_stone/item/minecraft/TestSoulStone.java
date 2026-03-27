package com.csdy.better_soul_stone.item.minecraft;

import com.csdy.better_soul_stone.annotation.SoulStoneItems;
import com.csdy.better_soul_stone.item.BaseSoulStone;
import com.csdy.better_soul_stone.soul_stone.soul_stone_capability.ISoulStoneOnAttacked;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingAttackEvent;

@SoulStoneItems(id = "test_soul_stone")
public class TestSoulStone extends BaseSoulStone implements ISoulStoneOnAttacked {


    @Override
    public boolean onAttacked(LivingAttackEvent event, LivingEntity attacker, Entity target, ItemStack stack) {
        return false;
    }

}
