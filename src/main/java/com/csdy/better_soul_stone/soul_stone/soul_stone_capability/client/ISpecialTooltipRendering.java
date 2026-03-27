package com.csdy.better_soul_stone.soul_stone.soul_stone_capability.client;

import net.minecraft.world.item.ItemStack;

public interface ISpecialTooltipRendering {

    default boolean shouldRenderIconBackground(ItemStack stack) {
        return true;
    }

}
