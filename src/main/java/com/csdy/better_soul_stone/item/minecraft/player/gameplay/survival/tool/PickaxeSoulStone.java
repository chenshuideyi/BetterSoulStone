package com.csdy.better_soul_stone.item.minecraft.player.gameplay.survival.tool;

import com.csdy.better_soul_stone.annotation.SoulStoneItems;
import com.csdy.better_soul_stone.item.BaseSoulStone;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeMod;
import top.theillusivec4.curios.api.SlotContext;

import java.util.UUID;

import static com.csdy.better_soul_stone.util.SoulStoneUtil.makeModifierName;

@SoulStoneItems(id = "pickaxe_soul_stone")
public class PickaxeSoulStone extends BaseSoulStone {

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> modifiers = HashMultimap.create();
        modifiers.put(ForgeMod.BLOCK_REACH.get(),
                new AttributeModifier(
                        uuid,
                        makeModifierName(this,ForgeMod.BLOCK_REACH.get()),
                        2.0,
                        AttributeModifier.Operation.ADDITION
                ));
        return modifiers;
    }

}