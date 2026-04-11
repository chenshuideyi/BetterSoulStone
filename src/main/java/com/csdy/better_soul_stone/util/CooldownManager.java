package com.csdy.better_soul_stone.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public class CooldownManager {

    private static final String COOLDOWN_TAG = "soul_stone_cooldowns";

    public static boolean isOnCooldown(Player player, ItemStack stack, String abilityId) {
        CompoundTag tag = getOrCreateTag(stack);
        String key = getCooldownKey(player.getUUID(), abilityId);
        if (tag.contains(key)) {
            long cooldownEnd = tag.getLong(key);
            return player.level().getGameTime() < cooldownEnd;
        }
        return false;
    }

    public static int getRemainingCooldown(Player player, ItemStack stack, String abilityId) {
        CompoundTag tag = getOrCreateTag(stack);
        String key = getCooldownKey(player.getUUID(), abilityId);
        if (tag.contains(key)) {
            long cooldownEnd = tag.getLong(key);
            long remaining = cooldownEnd - player.level().getGameTime();
            return remaining > 0 ? (int) remaining : 0;
        }
        return 0;
    }

    public static void setCooldown(Player player, ItemStack stack, String abilityId, int durationTicks) {
        CompoundTag tag = getOrCreateTag(stack);
        String key = getCooldownKey(player.getUUID(), abilityId);
        tag.putLong(key, player.level().getGameTime() + durationTicks);
    }

    public static void clearCooldown(Player player, ItemStack stack, String abilityId) {
        CompoundTag tag = getOrCreateTag(stack);
        String key = getCooldownKey(player.getUUID(), abilityId);
        tag.remove(key);
    }

    public static void clearAllCooldowns(Player player, ItemStack stack) {
        CompoundTag tag = getOrCreateTag(stack);
        String prefix = player.getUUID().toString() + "_";
        String[] keys = tag.getAllKeys().toArray(new String[0]);
        for (String key : keys) {
            if (key.startsWith(prefix)) {
                tag.remove(key);
            }
        }
    }

    private static CompoundTag getOrCreateTag(ItemStack stack) {
        if (!stack.hasTag()) {
            stack.setTag(new CompoundTag());
        }
        CompoundTag tag = stack.getTag();
        if (!tag.contains(COOLDOWN_TAG)) {
            tag.put(COOLDOWN_TAG, new CompoundTag());
        }
        return tag.getCompound(COOLDOWN_TAG);
    }

    private static String getCooldownKey(UUID playerUUID, String abilityId) {
        return playerUUID.toString() + "_" + abilityId;
    }
}