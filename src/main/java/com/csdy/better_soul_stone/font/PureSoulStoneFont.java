package com.csdy.better_soul_stone.font;

import net.minecraft.Util;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.font.FontSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

import java.util.function.Function;

public class PureSoulStoneFont extends Font {
    public PureSoulStoneFont(Function<ResourceLocation, FontSet> fontSetGetter, boolean useLegacy) {
        super(fontSetGetter, useLegacy);
    }

    @Override
    public int drawInBatch(@NotNull FormattedCharSequence sequence, float x, float y, int color, boolean hasShadow, @NotNull Matrix4f matrix, @NotNull MultiBufferSource buffer, @NotNull DisplayMode mode, int bgColor, int light) {
        StringBuilder builder = new StringBuilder();
        sequence.accept((index, style, codePoint) -> {
            builder.appendCodePoint(codePoint);
            return true;
        });
        return this.renderStrongText(builder.toString(), x, y, color, hasShadow, matrix, buffer, mode, bgColor, light);
    }

    @Override
    public int drawInBatch(@NotNull String text, float x, float y, int color, boolean hasShadow, @NotNull Matrix4f matrix, @NotNull MultiBufferSource buffer, @NotNull DisplayMode mode, int bgColor, int light) {
        return this.renderStrongText(text, x, y, color, hasShadow, matrix, buffer, mode, bgColor, light);
    }

    private int renderStrongText(String text, float x, float y, int color, boolean hasShadow, Matrix4f matrix, MultiBufferSource buffer, DisplayMode mode, int bgColor, int light) {
        float currentX = x;
        long time = Util.getMillis();

        for (int i = 0; i < text.length(); i++) {
            String character = String.valueOf(text.charAt(i));

            float offsetY = (float) (Math.sin((time / 100.0) + (i * 0.8)) * 0.6);
            float offsetX = (float) (Math.cos((time / 80.0) + (i * 0.8)) * 0.4);

            int strongColor = applyStrongEffect(i, time);

            if (hasShadow) {
                super.drawInBatch(character, currentX + offsetX + 0.5f, y + offsetY + 0.5f, (strongColor & 0xFFFFFF) | 0x44000000, false, matrix, buffer, mode, bgColor, light);
            }

            super.drawInBatch(character, currentX + offsetX, y + offsetY, strongColor, false, matrix, buffer, mode, bgColor, light);
            currentX += this.width(character);
        }

        return (int) currentX;
    }

    private int applyStrongEffect(int index, long time) {
        float f = (float) (Math.sin((time / 150.0) + (index * 0.4)) * 0.5 + 0.5);

        int r = (int) Mth.lerp(f, 255, 100);
        int g = (int) Mth.lerp(f, 215, 220);
        int b = (int) Mth.lerp(f, 0, 255);
        int a = 255;

        return (a << 24) | (r << 16) | (g << 8) | b;
    }
}
