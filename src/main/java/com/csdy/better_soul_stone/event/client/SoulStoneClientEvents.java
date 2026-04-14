package com.csdy.better_soul_stone.event.client;

import com.csdy.better_soul_stone.BetterSoulStoneModMain;
import com.csdy.better_soul_stone.item.BaseSoulStone;
import com.csdy.better_soul_stone.register.SoulStoneRegistry;
import com.csdy.better_soul_stone.soul_stone.soul_stone_capability.client.ISpecialTooltipRendering;
import com.csdy.better_soul_stone.util.SoulStoneUtil;
import com.csdy.better_soul_stone.util.client.GenericKeyDetector;
import com.csdy.better_soul_stone.util.client.SoulStoneEntryTooltip;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Either;
import com.mojang.math.Axis;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.event.TickEvent;
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

    private static boolean keysInitialized = false;
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.screen != null) return;

        if (!keysInitialized) {
            GenericKeyDetector.init(mc.options);
            keysInitialized = true;
        }

        GenericKeyDetector.tick();
    }

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

            poseStack.pushPose();

            float angleOffset = (float) (i * Math.PI * 2.0 / soulStones.size());
            float currentAngle = time + angleOffset;
            float radius = 1.4F;
            float yOffset = entity.getBbHeight() * 0.6F + (float) Math.sin(time * 2.0F + i) * 0.1F;

            poseStack.translate(Math.cos(currentAngle) * radius, yOffset, Math.sin(currentAngle) * radius);
            poseStack.mulPose(Axis.YP.rotationDegrees(time * 50.0F));

            float baseScale = 2.0F;
            poseStack.scale(baseScale, baseScale, baseScale);

            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();

            float glowAlpha = 0.3F;

            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, glowAlpha);
            renderStaticItem(stack, maxLight, poseStack, bufferSource, entity);

            if (bufferSource instanceof MultiBufferSource.BufferSource sb) {
                sb.endBatch();
            }


            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 0.9F);

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

    @SubscribeEvent
    public static void onTooltipColor(RenderTooltipEvent.Color event) {
        ItemStack stack = event.getItemStack();
        if (stack.getItem() instanceof ISpecialTooltipRendering special && special.hasCustomToolTipGlint(stack)) {
            int color = special.getToolTipGlintColor(stack);

            event.setBorderStart(color);
            event.setBorderEnd(color);
        }
    }

    @SubscribeEvent
    public static void onTooltipGather(RenderTooltipEvent.GatherComponents event) {
        ItemStack itemStack = event.getItemStack();
        if (!(itemStack.getItem() instanceof BaseSoulStone soulStone)) return;

        java.util.Set<String> handledKeys = new java.util.HashSet<>();

        String currentId = soulStone.getSoulStoneId();
        List<String> chain = SoulStoneRegistry.getParentChain(currentId);

        for (String id : chain) {
            Item item = net.minecraftforge.registries.ForgeRegistries.ITEMS.getValue(
                    ResourceLocation.fromNamespaceAndPath("better_soul_stone", id)
            );
            if (item == null) continue;

            String baseKey = "text.better_soul_stone." + id;
            ItemStack iconStack = new ItemStack(item);

            if (!handledKeys.contains(baseKey) && net.minecraft.client.resources.language.I18n.exists(baseKey)) {
                Component title = Component.translatable(baseKey).withStyle(ChatFormatting.GRAY);
                // 每一行都用 Either.right 包装
                event.getTooltipElements().add(Either.right(new SoulStoneEntryTooltip(iconStack, title)));
                handledKeys.add(baseKey);

                int i = 1;
                while (true) {
                    String nextKey = baseKey + i;
                    if (net.minecraft.client.resources.language.I18n.exists(nextKey)) {
                        if (!handledKeys.contains(nextKey)) {
                            Component extraText = Component.translatable(nextKey).withStyle(ChatFormatting.GRAY);
                            event.getTooltipElements().add(Either.right(new SoulStoneEntryTooltip(iconStack, extraText)));
                            handledKeys.add(nextKey);
                        }
                        i++;
                    } else {
                        break;
                    }
                }
            }
        }
    }

}