package com.csdy.better_soul_stone.event.client;

import com.csdy.better_soul_stone.BetterSoulStoneModMain;
import com.csdy.better_soul_stone.item.BaseSoulStone;
import com.csdy.better_soul_stone.soul_stone.soul_stone_capability.client.ISpecialTooltipRendering;
import com.csdy.better_soul_stone.util.SoulStoneUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.List;

@Mod.EventBusSubscriber(modid = BetterSoulStoneModMain.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class SoulStoneClientEvents {

    @SubscribeEvent
    public static void onRenderLivingPost(RenderLivingEvent.Post<LivingEntity, ?> event) {
        LivingEntity entity = event.getEntity();
        if (entity == null || entity.isInvisible()) return;

        List<ItemStack> soulStones = SoulStoneUtil.getUniqueSoulStonesForRender(entity);
        if (soulStones.isEmpty()) return;

        PoseStack poseStack = event.getPoseStack();
        float partialTicks = event.getPartialTick();
        MultiBufferSource bufferSource = event.getMultiBufferSource();

        float time = (entity.level().getGameTime() + partialTicks) / 20.0F;
        int maxLight = 15 << 4 | 15 << 20;

        for (int i = 0; i < soulStones.size(); i++) {
            ItemStack stack = soulStones.get(i);
            BaseSoulStone item = (BaseSoulStone) stack.getItem();
            int tier = item.getTier(stack);

            poseStack.pushPose();

            float angleOffset = (float) (i * Math.PI * 2.0 / soulStones.size());
            float currentAngle = time + angleOffset;
            float radius = 1.4F;
            float yOffset = entity.getBbHeight() * 0.6F + (float) Math.sin(time * 2.0F + i) * 0.1F;

            poseStack.translate(Math.cos(currentAngle) * radius, yOffset, Math.sin(currentAngle) * radius);
            poseStack.mulPose(Axis.YP.rotationDegrees(time * 50.0F));

            float scale = 2F;
            poseStack.scale(scale, scale, scale);

            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();

            float glowAlpha = tier >= 3 ? 0.3F : 0.1F;
            if (tier >= 3) {
                RenderSystem.setShaderColor(1.0F, 0.9F, 0.4F, glowAlpha); // 金色光晕
            } else {
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, glowAlpha); // 白色光晕
            }


            Minecraft.getInstance().getItemRenderer().renderStatic(
                    stack,
                    ItemDisplayContext.GROUND,
                    maxLight,
                    OverlayTexture.NO_OVERLAY,
                    poseStack,
                    bufferSource,
                    entity.level(),
                    entity.getId()
            );


            if (tier >= 3) {
                RenderSystem.setShaderColor(1.0F, 0.8F, 0.4F, 0.7F);
            } else {
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 0.7F);
            }

            Minecraft.getInstance().getItemRenderer().renderStatic(
                    stack,
                    ItemDisplayContext.GROUND,
                    maxLight,
                    OverlayTexture.NO_OVERLAY,
                    poseStack,
                    bufferSource,
                    entity.level(),
                    entity.getId()
            );

            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.disableBlend();

            poseStack.popPose();
        }
    }

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


        float targetSize = 64.0f;
        float scale = targetSize / 16.0f;
        graphics.pose().scale(scale, scale, 1.0F);
        graphics.renderFakeItem(stack, -8, -8);

        graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
        graphics.pose().popPose();
    }
}
