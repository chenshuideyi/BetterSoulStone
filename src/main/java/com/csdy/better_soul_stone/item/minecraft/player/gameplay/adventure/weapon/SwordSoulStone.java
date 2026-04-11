package com.csdy.better_soul_stone.item.minecraft.player.gameplay.adventure.weapon;

import com.csdy.better_soul_stone.annotation.SoulStoneItems;
import com.csdy.better_soul_stone.item.BaseSoulStone;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;

import java.util.UUID;

import static com.csdy.better_soul_stone.util.SoulStoneUtil.makeModifierName;

@SoulStoneItems(id = "sword_soul_stone")
public class SwordSoulStone extends BaseSoulStone {

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> modifiers = HashMultimap.create();
        modifiers.put(Attributes.ATTACK_SPEED,
                new AttributeModifier(
                        uuid,
                        makeModifierName(this,Attributes.ATTACK_SPEED),
                        1.0,
                        AttributeModifier.Operation.ADDITION
                ));
        return modifiers;
    }

}