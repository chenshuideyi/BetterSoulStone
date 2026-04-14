package com.csdy.better_soul_stone.soul_stone.soul_stone_capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public interface ISoulStoneDailyLimit extends ISoulStoneCapability {

    default String getLimitKey() { return getAbilityId(); }

    default int getMaxDailyUses() { return 1; }

    default void checkAndResetDailyLimit(LivingEntity entity, ItemStack stack) {
        long currentDay = entity.level().getGameTime() / 24000L;
        CompoundTag tag = stack.getOrCreateTag();
        String dayKey = getLimitKey() + "_last_reset";
        String countKey = getLimitKey() + "_remains";

        if (tag.getLong(dayKey) != currentDay) {
            tag.putLong(dayKey, currentDay);
            tag.putInt(countKey, getMaxDailyUses());
            if (tag.contains("SoulStoneCooldowns")) {
                tag.getCompound("SoulStoneCooldowns").remove(getAbilityId());
            }
        }
    }

    default int getRemainingUses(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        String countKey = getLimitKey() + "_remains";
        if (!tag.contains(countKey)) {
            tag.putInt(countKey, getMaxDailyUses());
        }
        return tag.getInt(countKey);
    }

    default void consumeUse(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        String countKey = getLimitKey() + "_remains";
        tag.putInt(countKey, Math.max(0, getRemainingUses(stack) - 1));
    }
}
