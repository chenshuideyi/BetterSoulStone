package com.csdy.better_soul_stone.util.client;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public record SoulStoneEntryTooltip(ItemStack stack, Component text) implements net.minecraft.world.inventory.tooltip.TooltipComponent {}
