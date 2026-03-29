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

public class TheoremSoulStoneFont extends Font {
    public TheoremSoulStoneFont(Function<ResourceLocation, FontSet> fontSetGetter, boolean useLegacy) {
        super(fontSetGetter, useLegacy);
    }

    @Override
    public int drawInBatch(@NotNull FormattedCharSequence sequence, float x, float y, int color, boolean hasShadow, @NotNull Matrix4f matrix, @NotNull MultiBufferSource buffer, @NotNull DisplayMode mode, int bgColor, int light) {
        StringBuilder builder = new StringBuilder();
        sequence.accept((index, style, codePoint) -> {
            builder.appendCodePoint(codePoint);
            return true;
        });
        return this.renderTheoremText(builder.toString(), x, y, color, hasShadow, matrix, buffer, mode, bgColor, light);
    }

    private int renderTheoremText(String text, float x, float y, int color, boolean hasShadow, Matrix4f matrix, MultiBufferSource buffer, DisplayMode mode, int bgColor, int light) {
        float currentX = x;
        long time = Util.getMillis();

        for (int i = 0; i < text.length(); i++) {
            String character = String.valueOf(text.charAt(i));

            float scanLine = (float) (Math.sin(time / 500.0 + i * 0.2) * 0.5 + 0.5);

            int r = (int) Mth.lerp(scanLine, 0, 200);
            int g = (int) Mth.lerp(scanLine, 255, 255);
            int b = (int) Mth.lerp(scanLine, 200, 255);
            int theoremColor = (255 << 24) | (r << 16) | (g << 8) | b;

            float offsetX = (float) (Math.sin(time / 1000.0) * 0.2);

            super.drawInBatch(character, currentX + offsetX, y, theoremColor, hasShadow, matrix, buffer, mode, bgColor, light);
            currentX += this.width(character);
        }
        return (int) currentX;
    }
}