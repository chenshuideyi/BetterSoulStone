package com.csdy.better_soul_stone.soul_stone.manager;

import com.csdy.better_soul_stone.soul_stone.ISoulStoneAttack;
import com.csdy.better_soul_stone.soul_stone.ISoulStoneOnAttacked;
import com.csdy.better_soul_stone.soul_stone.ISoulStoneTick;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

import java.util.*;

@SuppressWarnings("all")
public class SoulStoneManager {

    private static final Map<UUID, Map<Class<?>, List<SlotResult>>> ABILITY_CACHE = new WeakHashMap<>();

    /**
     * 只在 CurioChangeEvent 和 EntityJoinLevelEvent时调用
     */
    public static void refresh(LivingEntity entity) {
        Map<Class<?>, List<SlotResult>> playerAbilities = new HashMap<>();

        CuriosApi.getCuriosHelper().getCuriosHandler(entity).ifPresent(handler -> {
            Map<String, ICurioStacksHandler> curios = handler.getCurios();
            for (Map.Entry<String, ICurioStacksHandler> entry : curios.entrySet()) {
                String identifier = entry.getKey();
                IItemHandlerModifiable stacks = entry.getValue().getStacks();

                for (int i = 0; i < stacks.getSlots(); i++) {
                    ItemStack stack = stacks.getStackInSlot(i);
                    if (stack.isEmpty()) continue;

                    Item item = stack.getItem();
                    SlotContext slotContext = new SlotContext(identifier, entity, i, false, true);
                    SlotResult result = new SlotResult(slotContext, stack);

                    registerIfMatch(item, ISoulStoneOnAttacked.class, result, playerAbilities);
                    registerIfMatch(item, ISoulStoneAttack.class, result, playerAbilities);
                    registerIfMatch(item, ISoulStoneTick.class, result, playerAbilities);
                }
            }
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
}
