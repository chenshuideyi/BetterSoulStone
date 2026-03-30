package com.csdy.better_soul_stone.util;

import com.csdy.better_soul_stone.item.BaseSoulStone;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

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

    /**
     * 物理销毁玩家饰品栏中的特定魂石并触发更新
     * @return 是否成功销毁
     */
    @SuppressWarnings("all")
    public static boolean destroySoulStone(LivingEntity entity, ItemStack targetStack) {
        if (entity == null || entity.level().isClientSide || targetStack.isEmpty()) return false;

        return CuriosApi.getCuriosHelper().getCuriosHandler(entity).map(handler -> {
            for (var entry : handler.getCurios().entrySet()) {
                IDynamicStackHandler stackHandler = entry.getValue().getStacks();

                for (int i = 0; i < stackHandler.getSlots(); i++) {
                    ItemStack stackInSlot = stackHandler.getStackInSlot(i);

                    if (stackInSlot == targetStack ||
                            (!stackInSlot.isEmpty() && stackInSlot.getItem() == targetStack.getItem())) {

                        stackHandler.setStackInSlot(i, ItemStack.EMPTY);
                        return true;
                    }
                }
            }
            return false;
        }).orElse(false);
    }

}
