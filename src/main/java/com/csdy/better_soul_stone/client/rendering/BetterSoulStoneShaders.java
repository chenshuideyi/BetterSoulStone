package com.csdy.better_soul_stone.client.rendering;

import com.csdy.better_soul_stone.BetterSoulStoneModMain;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.io.IOException;

public class BetterSoulStoneShaders {
    private static ShaderInstance better;
    private static final int CycleTime = 200;
    private static float timeNormalized = 0;

    public static ShaderInstance setupBetter() {
        var time = better.getUniform("Time");
        if (time != null) time.set(timeNormalized);
        return better;
    }

    @Mod.EventBusSubscriber(modid = BetterSoulStoneModMain.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ShaderRegister {
        @SubscribeEvent
        public static void registerShaders(RegisterShadersEvent event) throws IOException {
            event.registerShader(new ShaderInstance(
                            event.getResourceProvider(),
                            ResourceLocation.fromNamespaceAndPath(BetterSoulStoneModMain.MODID, "better"),
                            DefaultVertexFormat.NEW_ENTITY),
                    shader -> better = shader
            );
        }
    }

    @Mod.EventBusSubscriber(modid = BetterSoulStoneModMain.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
    public static class UniformUpdater {
        @SubscribeEvent
        public static void onRenderTick(TickEvent.RenderTickEvent e) {
            var partialTick = e.renderTickTime;
            long tick = 0;
            if (Minecraft.getInstance().level != null) {
                tick = Minecraft.getInstance().level.getGameTime();
            }
            var time = Math.floorMod(tick, CycleTime) + partialTick;
            timeNormalized = time / CycleTime;
        }
    }
}
