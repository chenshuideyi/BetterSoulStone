package com.csdy.better_soul_stone.item;

import com.csdy.better_soul_stone.annotation.SoulStoneItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.SlotContext;

@SoulStoneItems(id = "blank_soul_stone")
public class BlankSoulStone extends BaseSoulStone {

    public BlankSoulStone() {
        super(new Item.Properties().stacksTo(64));
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        return true;
    }

    @Override
    public boolean isFoil(@NotNull ItemStack stack) {
        return false;
    }

    @Override
    public boolean shouldRenderIconBackground(ItemStack stack) {
        return false;
    }

}
