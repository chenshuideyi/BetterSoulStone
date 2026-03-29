package com.csdy.better_soul_stone.font;

import net.minecraft.Util;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.font.FontSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

import java.util.function.Function;

public class NetherSoulStoneFont extends Font {
    public NetherSoulStoneFont(Function<ResourceLocation, FontSet> fontSetGetter, boolean useLegacy) {
        super(fontSetGetter, useLegacy);
    }

    @Override
    public int drawInBatch(@NotNull FormattedCharSequence sequence, float x, float y, int color, boolean hasShadow, @NotNull Matrix4f matrix, @NotNull MultiBufferSource buffer, @NotNull DisplayMode mode, int bgColor, int light) {
        StringBuilder builder = new StringBuilder();
        sequence.accept((index, style, codePoint) -> {
            builder.appendCodePoint(codePoint);
            return true;
        });
        return this.renderNetherText(builder.toString(), x, y, color, hasShadow, matrix, buffer, mode, bgColor, light);
    }

    @Override
    public int drawInBatch(@NotNull String text, float x, float y, int color, boolean hasShadow, @NotNull Matrix4f matrix, @NotNull MultiBufferSource buffer, @NotNull DisplayMode mode, int bgColor, int light) {
        return this.renderNetherText(text, x, y, color, hasShadow, matrix, buffer, mode, bgColor, light);
    }


    private int renderNetherText(String text, float x, float y, int color, boolean hasShadow, Matrix4f matrix, MultiBufferSource buffer, DisplayMode mode, int bgColor, int light) {
        float currentX = x;
        long time = Util.getMillis();

        for (int i = 0; i < text.length(); i++) {
            String character = String.valueOf(text.charAt(i));

            float offsetY = (float) (Math.sin((time / 150.0) + (i * 0.5)) * 0.4);
            float offsetX = (float) (Math.cos((time / 200.0) + (i * 0.5)) * 0.2);

            int netherColor = applyNetherEffect(i, time);

            super.drawInBatch(character, currentX + offsetX, y + offsetY, netherColor, hasShadow, matrix, buffer, mode, bgColor, light);
            currentX += this.width(character);
        }

        return (int) currentX;
    }

    private int applyNetherEffect(int index, long time) {
        float pulse = (float) (Math.sin((time / 200.0) + (index * 0.3)) * 0.5 + 0.5);

        int r = 255;
        int g = (int) (40 + pulse * 100);
        int b = 30;
        int a = 255;

        return (a << 24) | (r << 16) | (g << 8) | b;
    }
}