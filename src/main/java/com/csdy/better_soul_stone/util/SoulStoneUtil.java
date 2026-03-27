package com.csdy.better_soul_stone.util;

import com.csdy.better_soul_stone.item.BaseSoulStone;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class SoulStoneUtil {
    //孩子们，util类不需要写单例

    public static String makeModifierName(Item item, Attribute attribute) {
        ResourceLocation itemKey = ForgeRegistries.ITEMS.getKey(item);
        ResourceLocation attrKey = ForgeRegistries.ATTRIBUTES.getKey(attribute);

        String itemName = (itemKey != null) ? itemKey.toString() : "unknown_item";
        String attrName = (attrKey != null) ? attrKey.toString() : "unknown_attribute";

        return itemName + "." + attrName;
    }

    @SuppressWarnings("all")
    public static void forEachActiveSoulStone(LivingEntity entity, BiConsumer<BaseSoulStone, ItemStack> action) {
        if (entity == null) return;

        CuriosApi.getCuriosHelper().getEquippedCurios(entity).ifPresent(handler -> {
            for (int i = 0; i < handler.getSlots(); i++) {
                ItemStack stack = handler.getStackInSlot(i);
                if (stack.getItem() instanceof BaseSoulStone soulStone) {
                    action.accept(soulStone, stack);
                }
            }
        });
    }

    public static List<ItemStack> getUniqueSoulStonesForRender(LivingEntity entity) {
        List<ItemStack> list = new ArrayList<>();
        forEachActiveSoulStone(entity, (item, stack) -> {
            list.add(stack);
        });
        return list;
    }

}
