package com.csdy.better_soul_stone.soul_stone.soul_stone_capability;

import com.csdy.better_soul_stone.util.CooldownManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public interface ISoulStoneCapability {

    /** 定义所属分组，例如 "ore", "combat" */
    default String getGroupName() { return "general"; }

    /** 基础描述 */
    default String getDescriptionKey() { return ""; }

    default void onForceRemoved(LivingEntity entity, ItemStack stack) {

    }

    /** 获取该能力的冷却时间（单位：tick），返回0表示无冷却 */
    default int getCooldownTicks() { return 0; }

    /** 获取该能力的唯一ID，用于区分同一个魂石上的不同能力 */
    default String getAbilityId() { return "default"; }

    default boolean isOnCooldown(LivingEntity entity, ItemStack stack) {
        int cooldown = getCooldownTicks();
        if (cooldown <= 0) return false;

        long currentTime = entity.level().getGameTime();
        long unlockTime = getUnlockTime(stack, getAbilityId());

        return currentTime < unlockTime;
    }

    default int getRemainingCooldown(LivingEntity entity, ItemStack stack) {
        long currentTime = entity.level().getGameTime();
        long unlockTime = getUnlockTime(stack, getAbilityId());
        return (int) Math.max(0, unlockTime - currentTime);
    }

    default void setCooldown(LivingEntity entity, ItemStack stack) {
        int cooldown = getCooldownTicks();
        if (cooldown > 0) {
            long unlockTime = entity.level().getGameTime() + cooldown;
            saveUnlockTime(stack, getAbilityId(), unlockTime);
        }
    }

    private long getUnlockTime(ItemStack stack, String abilityId) {
        CompoundTag nbt = stack.getTag();
        if (nbt != null && nbt.contains("SoulStoneCooldowns")) {
            return nbt.getCompound("SoulStoneCooldowns").getLong(abilityId);
        }
        return 0L;
    }

    private void saveUnlockTime(ItemStack stack, String abilityId, long unlockTime) {
        CompoundTag nbt = stack.getOrCreateTag();
        if (!nbt.contains("SoulStoneCooldowns")) {
            nbt.put("SoulStoneCooldowns", new CompoundTag());
        }
        nbt.getCompound("SoulStoneCooldowns").putLong(abilityId, unlockTime);
    }

}
