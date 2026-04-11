package com.csdy.better_soul_stone;

import com.csdy.better_soul_stone.event.client.SoulStoneClientEvents;
import com.csdy.better_soul_stone.network.BetterSoulStoneSyncing;
import com.csdy.better_soul_stone.register.SoulStoneItemRegister;
import com.csdy.better_soul_stone.register.SoulStoneRegistry;
import com.csdy.better_soul_stone.util.client.SoulStoneEntryRenderer;
import com.csdy.better_soul_stone.util.client.SoulStoneEntryTooltip;
import com.mojang.logging.LogUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import java.util.List;

@Mod(BetterSoulStoneModMain.MODID)
public class BetterSoulStoneModMain {
    public static final String MODID = "better_soul_stone";
    public static final Logger LOGGER = LogUtils.getLogger();
    //操你妈这个甚至不算常量 死吗了JAVA
    //public static final String[] NO_REQUIRED = {""};
    //操你妈JAVA为什么不能这么写？？？？
    //public static final String[] noRequired = [""];

    // TODO 一些弃用方法的更换，接口补全，tier和分组系统

    public BetterSoulStoneModMain(FMLJavaModLoadingContext context) {
        IEventBus bus = context.getModEventBus();
        SoulStoneRegistry.initialize();
        BetterSoulStoneSyncing.Init();
        SoulStoneItemRegister.autoRegisterSoulStones();
        SoulStoneItemRegister.ITEMS.register(bus);
        BetterSoulStoneTab.CREATIVE_MODE_TABS.register(bus);
//        bus.addListener(SoulStoneClientEvents::onRegisterTooltipFactories);

        MinecraftForge.EVENT_BUS.register(this);
    }

//    @SubscribeEvent
//    public static void onFMLCommonSetup(FMLCommonSetupEvent event) {
//        //网络包
//        BetterSoulStoneSyncing.Init();
//    }

//    @SubscribeEvent
//    public static void onRegisterTooltipComponent(RegisterClientTooltipComponentFactoriesEvent event) {
//        event.register(SoulStoneEntryTooltip.class, SoulStoneEntryRenderer::new);
//    }


}