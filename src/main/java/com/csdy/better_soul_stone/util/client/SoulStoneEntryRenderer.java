package com.csdy.better_soul_stone.util.client;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import org.joml.Matrix4f;

public record SoulStoneEntryRenderer(SoulStoneEntryTooltip data) implements net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent {
    @Override
    public int getHeight() { return 18; } // 物品图标高16，加点间距

    @Override
    public int getWidth(Font font) {
        return 20 + font.width(data.text()); // 图标20像素宽度
    }

    @Override
    public void renderImage(Font font, int x, int y, GuiGraphics guiGraphics) {
        // 画出魂石贴图
        guiGraphics.renderItem(data.stack(), x, y);
    }

    @Override
    public void renderText(Font font, int x, int y, Matrix4f matrix4f, MultiBufferSource.BufferSource bufferSource) {
        // 在图标右侧画出描述文字
        font.drawInBatch(data.text(), x + 20, y + 4, -1, true, matrix4f, bufferSource, Font.DisplayMode.NORMAL, 0, 15728880);
    }
}
