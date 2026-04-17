package com.csdy.better_soul_stone.event;

import com.csdy.better_soul_stone.BetterSoulStoneModMain;
import com.csdy.better_soul_stone.soul_stone.manager.SoulStoneDropManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

@Mod.EventBusSubscriber(modid = BetterSoulStoneModMain.MODID)
public class SoulStoneEntityEvents {

    private static final String NBT_CHECKED_FLAG = "SoulStoneGenerated";

    @SubscribeEvent
    public static void onEntityJoin(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide) return;

        BetterSoulStoneModMain.LOGGER.info("DEBUG: Entity Join World: " + event.getEntity().getType());

        if (!(event.getEntity() instanceof LivingEntity livingEntity)) return;

        if (livingEntity instanceof net.minecraft.world.entity.decoration.ArmorStand) return;

        CompoundTag persistentData = livingEntity.getPersistentData();
        if (persistentData.getBoolean(NBT_CHECKED_FLAG)) return;

        String entityName = ForgeRegistries.ENTITY_TYPES.getKey(livingEntity.getType()).toString();
        // BetterSoulStoneModMain.LOGGER.info("正在检查生物生成: " + entityName);

        // 3. 判定是否应该拥有魂石
        ItemStack stoneToEquip = SoulStoneDropManager.rollForMob(livingEntity);

        if (!stoneToEquip.isEmpty()) {
            persistentData.putBoolean(NBT_CHECKED_FLAG, true);

            CuriosApi.getCuriosInventory(livingEntity).ifPresent(handler -> {
                handler.getStacksHandler("soul_stone").ifPresentOrElse(soulHandler -> {
                    if (soulHandler.getSlots() <= 0) {
                        soulHandler.grow(1);
                    }

                    soulHandler.getStacks().setStackInSlot(0, stoneToEquip);

                    BetterSoulStoneModMain.LOGGER.info("!!! [Success] Living: {} Add: {}",
                            entityName, stoneToEquip.getHoverName().getString());

                }, () -> {
                    BetterSoulStoneModMain.LOGGER.error("!!! [Failed] living: {} err", entityName);
                });
            });
        }
    }
}
