package com.csdy.better_soul_stone.event.client;

import com.csdy.better_soul_stone.BetterSoulStoneModMain;
import com.csdy.better_soul_stone.item.BaseSoulStone;
import com.csdy.better_soul_stone.soul_stone.soul_stone_capability.client.ISpecialTooltipRendering;
import com.mojang.math.Axis;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

@Mod.EventBusSubscriber(modid = BetterSoulStoneModMain.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class SoulStoneClientEvents {

    @SubscribeEvent
    public static void onRenderTooltipPre(RenderTooltipEvent.Pre event) {
        ItemStack stack = event.getItemStack();
        if (!(stack.getItem() instanceof ISpecialTooltipRendering special)) return;
        if (!special.shouldRenderIconBackground(stack)) return;

        GuiGraphics graphics = event.getGraphics();
        Minecraft mc = Minecraft.getInstance();

        //直接抓鼠标得了，不知道这byd文本框咋算的
        double mouseX = mc.mouseHandler.xpos() * (double) mc.getWindow().getGuiScaledWidth() / (double) mc.getWindow().getScreenWidth();
        double mouseY = mc.mouseHandler.ypos() * (double) mc.getWindow().getGuiScaledHeight() / (double) mc.getWindow().getScreenHeight();


        graphics.pose().pushPose();

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableDepthTest();
        graphics.setColor(1.0F, 1.0F, 1.0F, 1F);


        graphics.pose().translate((float) mouseX, (float) mouseY, -150);
        float time = (float) (System.currentTimeMillis() % 100000L) / 1000.0F;
        float rotationAngle = time * 20.0F;
        graphics.pose().mulPose(Axis.ZP.rotationDegrees(rotationAngle));


        float targetSize = 32.0f;
        float scale = targetSize / 16.0f;
        graphics.pose().scale(scale, scale, 1.0F);
        graphics.renderFakeItem(stack, -8, -8);

        graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
        graphics.pose().popPose();
    }
}
