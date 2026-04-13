package com.csdy.better_soul_stone.item.minecraft;

import com.csdy.better_soul_stone.annotation.SoulStoneItems;
import com.csdy.better_soul_stone.item.BaseSoulStone;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import top.theillusivec4.curios.api.SlotContext;

@SoulStoneItems(id = "minecraft_soul_stone")
public class MinecraftSoulStone extends BaseSoulStone {

    @Override
    @OnlyIn(Dist.CLIENT)
    public String getSoulStoneType() {
        return "better";
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        return true;
    }

}
