package com.csdy.better_soul_stone.item;

import com.csdy.better_soul_stone.annotation.SoulStoneItems;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import top.theillusivec4.curios.api.SlotContext;

@SoulStoneItems(id = "blank_end_soul_stone")
public class BlankEndSoulStone extends BaseSoulStone {

    public BlankEndSoulStone() {
        super(new Properties().stacksTo(64));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public String getSoulStoneType() {
        return "end";
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
