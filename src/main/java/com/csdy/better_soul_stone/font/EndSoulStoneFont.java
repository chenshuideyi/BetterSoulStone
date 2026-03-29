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

public class EndSoulStoneFont extends Font {
    public EndSoulStoneFont(Function<ResourceLocation, FontSet> fontSetGetter, boolean useLegacy) {
        super(fontSetGetter, useLegacy);
    }

    @Override
    public int drawInBatch(@NotNull FormattedCharSequence sequence, float x, float y, int color, boolean hasShadow, @NotNull Matrix4f matrix, @NotNull MultiBufferSource buffer, @NotNull DisplayMode mode, int bgColor, int light) {
        StringBuilder builder = new StringBuilder();
        sequence.accept((index, style, codePoint) -> {
            builder.appendCodePoint(codePoint);
            return true;
        });
        return this.renderEndText(builder.toString(), x, y, color, hasShadow, matrix, buffer, mode, bgColor, light);
    }

    @Override
    public int drawInBatch(@NotNull String text, float x, float y, int color, boolean hasShadow, @NotNull Matrix4f matrix, @NotNull MultiBufferSource buffer, @NotNull DisplayMode mode, int bgColor, int light) {
        return this.renderEndText(text, x, y, color, hasShadow, matrix, buffer, mode, bgColor, light);
    }

    private int renderEndText(String text, float x, float y, int color, boolean hasShadow, Matrix4f matrix, MultiBufferSource buffer, DisplayMode mode, int bgColor, int light) {
        float currentX = x;
        long time = Util.getMillis();

        for (int i = 0; i < text.length(); i++) {
            String character = String.valueOf(text.charAt(i));

            float offsetY = (float) (Math.sin((time / 300.0) + (i * 0.4)) * 0.5);
            float offsetX = 0;

            if ((time + i * 100L) % 2000 < 100) {
                offsetX = (float) (Math.random() * 0.8 - 0.4);
            }

            int endColor = applyEndEffect(i, time);

            super.drawInBatch(character, currentX + offsetX, y + offsetY, endColor, hasShadow, matrix, buffer, mode, bgColor, light);
            currentX += this.width(character);
        }

        return (int) currentX;
    }

    private int applyEndEffect(int index, long time) {
        float f = (float) (Math.sin((time / 400.0) + (index * 0.5)) * 0.5 + 0.5);

        int r = (int) (120 + f * 60);
        int g = (int) (20 + f * 40);
        int b = (int) (180 + f * 75);
        int a = 255;

        return (a << 24) | (r << 16) | (g << 8) | b;
    }
}
