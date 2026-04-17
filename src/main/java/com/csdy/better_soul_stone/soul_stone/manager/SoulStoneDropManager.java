package com.csdy.better_soul_stone.soul_stone.manager;

import com.csdy.better_soul_stone.BetterSoulStoneModMain;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class SoulStoneDropManager {
    private static final Map<String, List<DropEntry>> MOB_MAP = new HashMap<>();

    public record DropEntry(Supplier<? extends Item> itemSupplier, double chance) {}

    public static void addEntry(String mobId, Supplier<? extends Item> itemSupplier, double chance) {
        //又是时点害的！！！
        MOB_MAP.computeIfAbsent(mobId, k -> new ArrayList<>()).add(new DropEntry(itemSupplier, chance));
    }

    public static ItemStack rollForMob(LivingEntity entity) {
        // 如 minecraft:wither
        String entityId = ForgeRegistries.ENTITY_TYPES.getKey(entity.getType()).toString();

        ItemStack result = checkAndRoll(entityId, entity);
        if (!result.isEmpty()) return result;

        if (entity.getType().is(Tags.EntityTypes.BOSSES)) {
            return checkAndRoll("#forge:bosses", entity);
        }

        BetterSoulStoneModMain.LOGGER.info("DEBUG: Living Entity ID: {}, MOB_MAP Size: {}", entityId, MOB_MAP.size());

        return ItemStack.EMPTY;
    }

    private static ItemStack checkAndRoll(String key, LivingEntity entity) {
        List<DropEntry> entries = MOB_MAP.get(key);
        if (entries != null) {
            for (DropEntry entry : entries) {
                if (entity.getRandom().nextDouble() < entry.chance) {
                    // 只有在这里（游戏运行中）才调用 get()
                    return new ItemStack(entry.itemSupplier.get());
                }
            }
        }
        return ItemStack.EMPTY;
    }
}
