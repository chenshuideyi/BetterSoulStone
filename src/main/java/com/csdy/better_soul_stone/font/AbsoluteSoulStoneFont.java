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

public class AbsoluteSoulStoneFont extends Font {
    public AbsoluteSoulStoneFont(Function<ResourceLocation, FontSet> fontSetGetter, boolean useLegacy) {
        super(fontSetGetter, useLegacy);
    }

    @Override
    public int drawInBatch(@NotNull FormattedCharSequence sequence, float x, float y, int color, boolean hasShadow, @NotNull Matrix4f matrix, @NotNull MultiBufferSource buffer, @NotNull DisplayMode mode, int bgColor, int light) {
        StringBuilder builder = new StringBuilder();
        sequence.accept((index, style, codePoint) -> {
            builder.appendCodePoint(codePoint);
            return true;
        });
        return this.renderAbsoluteText(builder.toString(), x, y, color, hasShadow, matrix, buffer, mode, bgColor, light);
    }

    @Override
    public int drawInBatch(@NotNull String text, float x, float y, int color, boolean hasShadow, @NotNull Matrix4f matrix, @NotNull MultiBufferSource buffer, @NotNull DisplayMode mode, int bgColor, int light) {
        return this.renderAbsoluteText(text, x, y, color, hasShadow, matrix, buffer, mode, bgColor, light);
    }

    private int renderAbsoluteText(String text, float x, float y, int color, boolean hasShadow, Matrix4f matrix, MultiBufferSource buffer, DisplayMode mode, int bgColor, int light) {
        float currentX = x;
        long time = Util.getMillis();

        float scale = (float) (1.0 + Math.sin(time / 1500.0) * 0.05);

        for (int i = 0; i < text.length(); i++) {
            String character = String.valueOf(text.charAt(i));

            Matrix4f charMatrix = new Matrix4f(matrix);
            charMatrix.translate(currentX + width(character) / 2f, y + 4f, 0);
            charMatrix.scale(scale, scale, 1.0f);
            charMatrix.translate(-(currentX + width(character) / 2f), -(y + 4f), 0);

            int goldEdge = 0xFFD4AF37;
            int coreBlack = 0xFF111111;

            super.drawInBatch(character, currentX + 0.6f, y + 0.6f, goldEdge, false, charMatrix, buffer, mode, bgColor, light);
            super.drawInBatch(character, currentX - 0.6f, y - 0.6f, goldEdge, false, charMatrix, buffer, mode, bgColor, light);

            super.drawInBatch(character, currentX, y, coreBlack, false, charMatrix, buffer, mode, bgColor, light);

            currentX += this.width(character);
        }
        return (int) currentX;
    }
}
