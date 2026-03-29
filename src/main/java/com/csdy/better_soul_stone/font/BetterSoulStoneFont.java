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

public class BetterSoulStoneFont extends Font {
    public BetterSoulStoneFont(Function<ResourceLocation, FontSet> fontSetGetter, boolean useLegacy) {
        super(fontSetGetter, useLegacy);
    }

    @Override
    public int drawInBatch(@NotNull FormattedCharSequence sequence, float x, float y, int color, boolean hasShadow, @NotNull Matrix4f matrix, @NotNull MultiBufferSource buffer, @NotNull DisplayMode mode, int bgColor, int light) {
        StringBuilder builder = new StringBuilder();
        sequence.accept((index, style, codePoint) -> {
            builder.appendCodePoint(codePoint);
            return true;
        });
        return this.renderUltimateText(builder.toString(), x, y, color, hasShadow, matrix, buffer, mode, bgColor, light);
    }

    private int renderUltimateText(String text, float x, float y, int color, boolean hasShadow, Matrix4f matrix, MultiBufferSource buffer, DisplayMode mode, int bgColor, int light) {
        float currentX = x;
        long time = Util.getMillis();

        for (int i = 0; i < text.length(); i++) {
            String character = String.valueOf(text.charAt(i));

            float offsetX = (float) (Math.random() * 0.5 - 0.25);
            float offsetY = (float) (Math.random() * 0.5 - 0.25);

            float hue = (float) ((time / 1000.0 + i * 0.1) % 1.0);
            int rainbowColor = Mth.hsvToRgb(hue, 0.8F, 1.0F) | (255 << 24);

            float ghostAlpha = (float) (Math.sin(time / 200.0) * 0.2 + 0.3);
            int ghostColor = (rainbowColor & 0x00FFFFFF) | ((int)(ghostAlpha * 255) << 24);
            super.drawInBatch(character, currentX + 1.0f, y + 1.0f, ghostColor, false, matrix, buffer, mode, bgColor, light);

            super.drawInBatch(character, currentX + offsetX, y + offsetY, rainbowColor, hasShadow, matrix, buffer, mode, bgColor, light);

            currentX += this.width(character);
        }
        return (int) currentX;
    }
}
