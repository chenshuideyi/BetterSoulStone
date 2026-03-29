package com.csdy.better_soul_stone.item;

import com.csdy.better_soul_stone.annotation.SoulStoneItems;
import com.csdy.better_soul_stone.font.BlankSoulStoneFont;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.font.FontManager;
import net.minecraft.client.gui.font.FontSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;

import java.util.function.Consumer;
import java.util.function.Function;

@SoulStoneItems(id = "blank_nether_soul_stone")
public class BlankNetherSoulStone extends BaseSoulStone {

    public BlankNetherSoulStone() {
        super(new Properties().stacksTo(64));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public String getSoulStoneType() {
        return "nether";
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        return true;
    }

    @Override
    public boolean shouldRenderIconBackground(ItemStack stack) {
        return false;
    }



}
