package com.csdy.better_soul_stone;

import com.csdy.better_soul_stone.register.SoulStoneItemRegister;
import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(BetterSoulStoneModMain.MODID)
public class BetterSoulStoneModMain {
    public static final String MODID = "better_soul_stone";
    public static final Logger LOGGER = LogUtils.getLogger();

    public BetterSoulStoneModMain() {
        LOGGER.info("\n\n\n\n\nn\n\n\n\n\nn\n\n\n\n\nn\n\n\n\n +  HEELLLLLO");

        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        SoulStoneItemRegister.autoRegisterSoulStones();
        SoulStoneItemRegister.ITEMS.register(bus);
        BetterSoulStoneTab.CREATIVE_MODE_TABS.register(bus);

        MinecraftForge.EVENT_BUS.register(this);
    }
}