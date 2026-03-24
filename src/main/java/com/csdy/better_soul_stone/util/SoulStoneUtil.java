package com.csdy.better_soul_stone.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

public class SoulStoneUtil {
    //孩子们，util类不需要写单例

    public static String makeModifierName(Item item, Attribute attribute) {
        ResourceLocation itemKey = ForgeRegistries.ITEMS.getKey(item);
        ResourceLocation attrKey = ForgeRegistries.ATTRIBUTES.getKey(attribute);

        String itemName = (itemKey != null) ? itemKey.toString() : "unknown_item";
        String attrName = (attrKey != null) ? attrKey.toString() : "unknown_attribute";

        return itemName + "." + attrName;
    }

}
