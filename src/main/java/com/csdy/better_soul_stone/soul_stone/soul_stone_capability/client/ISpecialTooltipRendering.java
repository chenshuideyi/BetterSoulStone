package com.csdy.better_soul_stone.soul_stone.soul_stone_capability.client;

import com.csdy.better_soul_stone.soul_stone.soul_stone_capability.ISoulStoneCapability;
import net.minecraft.world.item.ItemStack;

public interface ISpecialTooltipRendering extends ISoulStoneCapability {

    default boolean shouldRenderIconBackground(ItemStack stack) {
        return true;
    }

    default boolean shouldRenderAroundHolder(ItemStack stack) {
        return true;
    }

    default int getGlowColor(ItemStack stack) {
        return 0xFFFFFF;
    }

    /**
     * ToolTip颜色
     */
    default int getToolTipGlintColor(ItemStack stack) {
        return 0xFFFFFF;
    }

    /**
     * 是否启用自定义光泽效果
     */
    default boolean hasCustomToolTipGlint(ItemStack stack) {
        return false;
    }

}
