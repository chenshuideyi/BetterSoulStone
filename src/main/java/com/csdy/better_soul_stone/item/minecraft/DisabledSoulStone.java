package com.csdy.better_soul_stone.item.minecraft;

import com.csdy.better_soul_stone.item.BaseSoulStone;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DisabledSoulStone extends BaseSoulStone {
    private final String[] missingMods;

    public DisabledSoulStone(String[] missingMods) {
        super();
        this.missingMods = missingMods;
        this.setDisabled(missingMods);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.better_soul_stone.disabled_title")
                .withStyle(ChatFormatting.RED, ChatFormatting.BOLD));
        for (String mod : missingMods) {
            tooltip.add(Component.literal("  - " + mod).withStyle(ChatFormatting.GRAY));
        }
        tooltip.add(Component.translatable("tooltip.better_soul_stone.disabled_desc")
                .withStyle(ChatFormatting.DARK_RED, ChatFormatting.ITALIC));
    }


    @Override public boolean shouldRenderIconBackground(ItemStack stack) { return false; }
    @Override public boolean shouldRenderAroundHolder(ItemStack stack) { return false; }
    @Override public boolean hasCustomToolTipGlint(ItemStack stack) {
        return super.hasCustomToolTipGlint(stack);
    }
}