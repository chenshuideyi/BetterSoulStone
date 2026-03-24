package com.csdy.better_soul_stone.item.minecraft;

import com.csdy.better_soul_stone.annotation.SoulStoneItems;
import com.csdy.better_soul_stone.item.BaseSoulStone;
import com.csdy.better_soul_stone.soul_stone.ISoulStoneOnAttacked;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import top.theillusivec4.curios.api.SlotContext;

import java.util.UUID;

import static com.csdy.better_soul_stone.util.SoulStoneUtil.makeModifierName;

@SoulStoneItems(id = "test_soul_stone")
public class TestSoulStone extends BaseSoulStone implements ISoulStoneOnAttacked {


    @Override
    public boolean onAttacked(LivingAttackEvent event, LivingEntity attacker, Entity target, ItemStack stack) {
        return false;
    }

}
