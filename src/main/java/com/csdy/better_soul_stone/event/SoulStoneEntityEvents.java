package com.csdy.better_soul_stone.event;

import com.csdy.better_soul_stone.BetterSoulStoneModMain;
import com.csdy.better_soul_stone.soul_stone.manager.SoulStoneDropManager;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import top.theillusivec4.curios.api.CuriosApi;

@Mod.EventBusSubscriber(modid = BetterSoulStoneModMain.MODID)
public class SoulStoneEntityEvents {

    private static final String NBT_CHECKED_FLAG = "SoulStoneGenerated";

    @SubscribeEvent
    public static void onEntityJoin(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide) return;

        BetterSoulStoneModMain.LOGGER.info("DEBUG: Entity Join World: " + event.getEntity().getType());

        if (!(event.getEntity() instanceof LivingEntity livingEntity)) return;

//        CompoundTag persistentData = livingEntity.getPersistentData();
//        if (persistentData.getBoolean(NBT_CHECKED_FLAG)) return;

        String entityName = ForgeRegistries.ENTITY_TYPES.getKey(livingEntity.getType()).toString();
        // BetterSoulStoneModMain.LOGGER.info("正在检查生物生成: " + entityName);

        // 3. 判定是否应该拥有魂石
        ItemStack stoneToEquip = SoulStoneDropManager.rollForMob(livingEntity);

        if (!stoneToEquip.isEmpty()) {

            BetterSoulStoneModMain.LOGGER.info("Need Equip: " + stoneToEquip);

//            persistentData.putBoolean(NBT_CHECKED_FLAG, true);

            CuriosApi.getCuriosInventory(livingEntity).ifPresent(handler -> {

                var stacksHandler = handler.getStacksHandler("soul_stone_slot").orElse(null);

                if (stacksHandler == null) {
                    BetterSoulStoneModMain.LOGGER.warn("Entity {} Has No soul_stone_slot", entityName);
                    return;
                }

                if (stacksHandler.getSlots() <= 0) {
                    stacksHandler.grow(1);
                }

                stacksHandler.getStacks().setStackInSlot(0, stoneToEquip);
                BetterSoulStoneModMain.LOGGER.info("!!! [Success] Living: {} Add: {}", entityName, stoneToEquip.getHoverName().getString());
            });
        }
    }
}
