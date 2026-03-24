package com.csdy.better_soul_stone.event;

import com.csdy.better_soul_stone.BetterSoulStoneModMain;
import com.csdy.better_soul_stone.soul_stone.manager.SoulStoneManager;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.EntityLeaveLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.event.CurioChangeEvent;

@Mod.EventBusSubscriber(modid = BetterSoulStoneModMain.MODID)
public class SoulStoneSyncHandler {

    @SubscribeEvent
    public static void onCurioChange(CurioChangeEvent event) {
        SoulStoneManager.refresh(event.getEntity());
    }

    @SubscribeEvent
    public static void onEntityJoin(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof LivingEntity living) {
            SoulStoneManager.refresh(living);
        }
    }

    @SubscribeEvent
    public static void onEntityExit(EntityLeaveLevelEvent event) {
        if (event.getEntity() instanceof LivingEntity living) {
            SoulStoneManager.clear(living.getUUID());
        }
    }

}
