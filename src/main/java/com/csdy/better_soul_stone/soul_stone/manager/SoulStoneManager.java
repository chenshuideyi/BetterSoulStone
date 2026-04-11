package com.csdy.better_soul_stone.soul_stone.manager;

import com.csdy.better_soul_stone.BetterSoulStoneModMain;
import com.csdy.better_soul_stone.item.BaseSoulStone;
import com.csdy.better_soul_stone.register.SoulStoneRegistry;
import com.csdy.better_soul_stone.soul_stone.soul_stone_capability.ISoulStoneCapability;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.SlotResult;

import java.util.*;
import java.util.function.BiConsumer;

@SuppressWarnings("all")
public class SoulStoneManager {

    /**
     * 核心记录类
     * logicItem: 真正实现接口的那个物品对象（可能是父级魂石）
     * slotResult: 玩家当前装备的那个魂石堆栈信息
     */
    public record ActiveLogic(Object logicItem, SlotResult slotResult) {}

    private static final Map<UUID, Map<Class<?>, List<ActiveLogic>>> ABILITY_CACHE = new WeakHashMap<>();
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

    public static void refresh(LivingEntity entity) {
        if (entity == null || entity.level().isClientSide) return;

        Map<Class<?>, List<ActiveLogic>> playerAbilities = new HashMap<>();

        CuriosApi.getCuriosHelper().getCuriosHandler(entity).ifPresent(handler -> {
            handler.getCurios().forEach((identifier, stackHandler) -> {
                var stacks = stackHandler.getStacks();
                for (int i = 0; i < stacks.getSlots(); i++) {
                    ItemStack stack = stacks.getStackInSlot(i);
                    if (stack.isEmpty() || !(stack.getItem() instanceof BaseSoulStone equippedStone)) continue;

                    SlotContext slotContext = new SlotContext(identifier, entity, i, false, true);
                    SlotResult slotResult = new SlotResult(slotContext, stack.copy());

                    Set<String> allLogicIds = new LinkedHashSet<>();
                    collectAllLogicIds(equippedStone.getSoulStoneId(), allLogicIds);

                    for (String logicId : allLogicIds) {
                        Item logicItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(BetterSoulStoneModMain.MODID, logicId));
                        if (logicItem == null) continue;

                        for (Class<? extends ISoulStoneCapability> iface : REGISTERED_INTERFACES) {
                            if (iface.isInstance(logicItem)) {
                                playerAbilities.computeIfAbsent(iface, k -> new ArrayList<>())
                                        .add(new ActiveLogic(logicItem, slotResult));
                            }
                        }
                    }
                }
            });
        });
        ABILITY_CACHE.put(entity.getUUID(), playerAbilities);
    }

    public static void clear(UUID uuid){
        ABILITY_CACHE.remove(uuid);
    }

    private static void collectAllLogicIds(String currentId, Set<String> result) {
        if (currentId == null || currentId.isEmpty() || result.contains(currentId)) return;
        result.add(currentId);
        List<String> parents = SoulStoneRegistry.getParentIds(currentId);
        if (parents != null) {
            for (String pid : parents) collectAllLogicIds(pid, result);
        }
    }

    public static <T> List<ActiveLogic> getLogics(LivingEntity entity, Class<T> capabilityClass) {
        return ABILITY_CACHE
                .getOrDefault(entity.getUUID(), Collections.emptyMap())
                .getOrDefault(capabilityClass, Collections.emptyList());
    }

    // 给各种 dispatch 使用的通用遍历方法
    public static <T> void forEachLogic(LivingEntity entity, Class<T> capabilityClass, BiConsumer<T, ItemStack> action) {
        if (entity == null) return;
        List<ActiveLogic> logics = getLogics(entity, capabilityClass);
        for (ActiveLogic active : logics) {
            action.accept((T) active.logicItem(), active.slotResult().stack());
        }
    }
}