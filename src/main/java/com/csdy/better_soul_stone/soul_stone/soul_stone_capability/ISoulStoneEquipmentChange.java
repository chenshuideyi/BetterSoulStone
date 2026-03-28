package com.csdy.better_soul_stone.soul_stone.soul_stone_capability;

import com.csdy.better_soul_stone.BetterSoulStoneModMain;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotResult;

import java.util.List;

public interface ISoulStoneEquipmentChange extends ISoulStoneCapability {

    void onEquip(LivingEntity wearer, ItemStack stack);

    void onUnequip(LivingEntity wearer, ItemStack stack);

    /**
     * 对比新旧快照，触发变更回调
     */
    static void dispatchChangeTrigger(LivingEntity wearer, List<SlotResult> oldStones, List<SlotResult> newStones) {

        List<ItemStack> oldStacks = oldStones.stream().map(SlotResult::stack).toList();
        List<ItemStack> newStacks = newStones.stream().map(SlotResult::stack).toList();

        // Unequip: 旧快照中有，但新快照中没有的
        for (SlotResult oldResult : oldStones) {
            ItemStack oldStack = oldResult.stack();
            if (newStacks.stream().noneMatch(s -> ItemStack.matches(s, oldStack))) {
                executeSafe(oldStack, (logic) -> logic.onUnequip(wearer, oldStack));
            }
        }

        // Equip: 新快照中有，但旧快照中没有的
        for (SlotResult newResult : newStones) {
            ItemStack newStack = newResult.stack();
            if (oldStacks.stream().noneMatch(s -> ItemStack.matches(s, newStack))) {
                executeSafe(newStack, (logic) -> logic.onEquip(wearer, newStack));
            }
        }
    }

    private static void executeSafe(ItemStack stack, java.util.function.Consumer<ISoulStoneEquipmentChange> action) {
        try {
            if (stack.getItem() instanceof ISoulStoneEquipmentChange logic) {
                action.accept(logic);
            }
        } catch (Exception e) {
            BetterSoulStoneModMain.LOGGER.error("Soul Stone Equipment Change trigger error!!!!!!!!", e);
        }
    }
}
