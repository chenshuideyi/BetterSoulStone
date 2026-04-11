package com.csdy.better_soul_stone.soul_stone.soul_stone_capability;

import com.csdy.better_soul_stone.util.CooldownManager;
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
        return CooldownManager.isOnCooldown((net.minecraft.world.entity.player.Player) entity, stack, getAbilityId());
    }

    default int getRemainingCooldown(LivingEntity entity, ItemStack stack) {
        return CooldownManager.getRemainingCooldown((net.minecraft.world.entity.player.Player) entity, stack, getAbilityId());
    }

    default void setCooldown(LivingEntity entity, ItemStack stack) {
        int cooldown = getCooldownTicks();
        if (cooldown > 0) {
            CooldownManager.setCooldown((net.minecraft.world.entity.player.Player) entity, stack, getAbilityId(), cooldown);
        }
    }

}
