package com.csdy.better_soul_stone.soul_stone.manager;

import com.csdy.better_soul_stone.soul_stone.soul_stone_capability.ISoulStoneCapability;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.SlotResult;

import java.util.*;
import java.util.function.BiConsumer;

@SuppressWarnings("all")
public class SoulStoneManager {

    private static final Map<UUID, Map<Class<?>, List<SlotResult>>> ABILITY_CACHE = new WeakHashMap<>();
    private static final Set<Class<? extends ISoulStoneCapability>> REGISTERED_INTERFACES = new java.util.concurrent.ConcurrentHashMap<>().newKeySet();

    public static void markInterfaceAsActive(Class<?> clazz) {
        Class<?> current = clazz;
        while (current != null && current != Object.class) {
            for (Class<?> iface : current.getInterfaces()) {
                if (ISoulStoneCapability.class.isAssignableFrom(iface) && iface != ISoulStoneCapability.class) {
                    REGISTERED_INTERFACES.add((Class<? extends ISoulStoneCapability>) iface);
                }
            }
            current = current.getSuperclass();
        }
    }

    /**
     * 只在 CurioChangeEvent 和 EntityJoinLevelEvent时调用
     */
    public static void refresh(LivingEntity entity) {
        if (entity == null || entity.level().isClientSide) return; // 基础保护

        Map<Class<?>, List<SlotResult>> playerAbilities = new HashMap<>();

        CuriosApi.getCuriosHelper().getCuriosHandler(entity).ifPresent(handler -> {
            handler.getCurios().forEach((identifier, stackHandler) -> {
                var stacks = stackHandler.getStacks();
                for (int i = 0; i < stacks.getSlots(); i++) {
                    ItemStack stack = stacks.getStackInSlot(i);
                    if (stack.isEmpty()) continue;

                    Item item = stack.getItem();
                    for (Class<? extends ISoulStoneCapability> iface : REGISTERED_INTERFACES) {
                        if (iface.isInstance(item)) {
                            SlotContext slotContext = new SlotContext(identifier, entity, i, false, true);
                            // 存入 copy 副本 解决 stack 变更问题 骑士史莱姆做不到的我来做
                            playerAbilities.computeIfAbsent(iface, k -> new ArrayList<>())
                                    .add(new SlotResult(slotContext, stack.copy()));
                        }
                    }
                }
            });
        });
        ABILITY_CACHE.put(entity.getUUID(), playerAbilities);
    }

    public static void clear(UUID playerUUID) {
        ABILITY_CACHE.remove(playerUUID);
    }

    private static void registerIfMatch(Object item, Class<?> clazz, SlotResult result, Map<Class<?>, List<SlotResult>> map) {
        if (clazz.isInstance(item)) {
            map.computeIfAbsent(clazz, k -> new ArrayList<>()).add(result);
        }
    }


    @SuppressWarnings("unchecked")
    public static <T> List<SlotResult> getStones(LivingEntity entity, Class<T> clazz) {
        return ABILITY_CACHE
                .getOrDefault(entity.getUUID(), Collections.emptyMap())
                .getOrDefault(clazz, Collections.emptyList());
    }

    //复用一下素材
    public static <T> void forEachStone(LivingEntity entity, Class<T> capabilityClass, BiConsumer<T, ItemStack> action) {
        if (entity == null || entity.level().isClientSide) return;
        getStones(entity, capabilityClass).forEach(result -> {
            ItemStack stack = result.stack();
            if (capabilityClass.isInstance(stack.getItem())) {
                action.accept(capabilityClass.cast(stack.getItem()), stack);
            }
        });
    }
}
