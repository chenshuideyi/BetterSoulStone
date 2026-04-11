package com.csdy.better_soul_stone.event.client;

import com.csdy.better_soul_stone.BetterSoulStoneModMain;
import com.csdy.better_soul_stone.util.client.SoulStoneEntryRenderer;
import com.csdy.better_soul_stone.util.client.SoulStoneEntryTooltip;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = BetterSoulStoneModMain.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SoulStoneClientSetup {
    @SubscribeEvent
    public static void onRegisterTooltipComponent(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(SoulStoneEntryTooltip.class, SoulStoneEntryRenderer::new);
    }
}
