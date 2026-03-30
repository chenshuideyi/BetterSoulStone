package com.csdy.better_soul_stone.event.client;

import com.csdy.better_soul_stone.BetterSoulStoneModMain;
import com.csdy.better_soul_stone.item.BaseSoulStone;
import com.csdy.better_soul_stone.soul_stone.soul_stone_capability.client.ISpecialTooltipRendering;
import com.csdy.better_soul_stone.util.SoulStoneUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = BetterSoulStoneModMain.MODID, value = Dist.CLIENT)
public class SoulStoneClientEvents {

    private static final ResourceLocation TOOLTIP_BG = ResourceLocation.fromNamespaceAndPath("minecraft", "textures/gui/light_dirt_background.png");

    private static void renderStaticItem(ItemStack stack, int light, PoseStack ms, MultiBufferSource buffer, LivingEntity entity) {
        Minecraft.getInstance().getItemRenderer().renderStatic(
                stack,
                ItemDisplayContext.GROUND,
                light,
                OverlayTexture.NO_OVERLAY,
                ms,
                buffer,
                entity.level(),
                entity.getId()
        );
    }

//    public static void onRegisterTooltipFactories(RegisterClientTooltipComponentFactoriesEvent event) {
//        event.register(CustomFontTooltipComponent.class, component -> component);
//    }

    @SubscribeEvent
    public static void onRenderLivingPost(RenderLivingEvent.Post<LivingEntity, ?> event) {
        LivingEntity entity = event.getEntity();
        if (entity == null || entity.isInvisible()) return;

        List<ItemStack> soulStones = SoulStoneUtil.getUniqueSoulStonesForRender(entity);
        if (soulStones.isEmpty()) return;

        PoseStack poseStack = event.getPoseStack();
        float partialTicks = event.getPartialTick();
        MultiBufferSource bufferSource = event.getMultiBufferSource();

        // 在我们要搞事情之前，先把原版积压在缓冲区里的“玩家皮肤/模型”强制画完！
        // 这样我们的半透明颜色就绝对不会污染到玩家本体
        // 哎哎 byd渲染
        if (bufferSource instanceof MultiBufferSource.BufferSource sb) {
            sb.endBatch();
        }

        float time = (entity.level().getGameTime() + partialTicks) / 20.0F;
        int maxLight = 15 << 4 | 15 << 20; // 满亮度

        for (int i = 0; i < soulStones.size(); i++) {
            ItemStack stack = soulStones.get(i);
            if (!(stack.getItem() instanceof BaseSoulStone item)) continue;
            if (!(stack.getItem() instanceof ISpecialTooltipRendering renderer)) continue;
            if (!renderer.shouldRenderAroundHolder(stack)) continue;

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
                RenderSystem.setShaderColor(1.0F, 0.9F, 0.4F, glowAlpha);
            } else {
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, glowAlpha);
            }
            renderStaticItem(stack, maxLight, poseStack, bufferSource, entity);

            if (bufferSource instanceof MultiBufferSource.BufferSource sb) {
                sb.endBatch();
            }

            if (tier >= 3) {
                RenderSystem.setShaderColor(1.0F, 0.8F, 0.4F, 0.9F);
            } else {
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 0.9F);
            }
            renderStaticItem(stack, maxLight, poseStack, bufferSource, entity);

            if (bufferSource instanceof MultiBufferSource.BufferSource sb) {
                sb.endBatch();
            }

            poseStack.popPose();
        }

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableBlend();
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


        float targetSize = 96.0f;
        float scale = targetSize / 16.0f;
        graphics.pose().scale(scale, scale, 1.0F);
        graphics.renderFakeItem(stack, -8, -8);
        graphics.blit(TOOLTIP_BG, 0, 0, 0, 0, (int)targetSize, (int)targetSize, 16, 16);

//        MultiBufferSource bufferSource = event.getGraphics().bufferSource();
//        if (bufferSource instanceof MultiBufferSource.BufferSource sb) {
//            sb.endBatch();
//        }

        graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
        graphics.pose().popPose();
        graphics.bufferSource().endBatch();
    }

//    @SubscribeEvent
//    public static void onRenderTooltip(RenderTooltipEvent.Pre event) {
//        ItemStack stack = event.getItemStack();
//        if (!(stack.getItem() instanceof ISpecialTooltipRendering special)) return;
//        if (!special.shouldRenderIconBackground(stack)) return;
//
//        GuiGraphics graphics = event.getGraphics();
//
//        // 【关键修复 3】：绝对不要用鼠标实际坐标算位置！
//        // Tooltip 事件自带 x 和 y 坐标，这是经过 GUI 缩放处理的安全坐标
//        int x = event.getX();
//        int y = event.getY();
//
//        graphics.pose().pushPose();
//
//        // 开启混合并把深度往后推，确保它在字体的“下层”
//        RenderSystem.enableBlend();
//        RenderSystem.defaultBlendFunc();
//        // 不要关 DepthTest，用 translate 的 Z 轴来控制层级
//        graphics.pose().translate(x - 8, y - 8, -50);
//
//        // 中心点偏移（为了让旋转围绕中心点）
//        float targetSize = 32.0f; // 我们画个 32x32 的背景
//        graphics.pose().translate(targetSize / 2f, targetSize / 2f, 0);
//
//        // 旋转动画
//        float time = (float) (System.currentTimeMillis() % 10000L) / 1000.0F;
//        graphics.pose().mulPose(Axis.ZP.rotationDegrees(time * 36.0F));
//
//        // 移回去
//        graphics.pose().translate(-targetSize / 2f, -targetSize / 2f, 0);
//
//        // 渲染贴图 (半透明红色为例，你可以自己改颜色或改回 1.0F)
//        graphics.setColor(1.0F, 0.5F, 0.5F, 0.6F);
//
//        // blit 用法：材质，屏幕X，屏幕Y，材质U偏移，材质V偏移，绘制宽，绘制高，材质总宽，材质总高
//        graphics.blit(TOOLTIP_BG, 0, 0, 0, 0, (int)targetSize, (int)targetSize, 16, 16);
//
//        // 【关键修复 4】：画完立刻还原颜色，否则整个 GUI 里的字和原版物品都会变红变透明！
//        graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
//        RenderSystem.disableBlend();
//
//        graphics.pose().popPose();
//    }
}