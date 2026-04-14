package com.csdy.better_soul_stone.mixin;

import net.minecraft.world.inventory.MerchantContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MerchantContainer.class)
public interface MerchantContainerAccessor {
    @Accessor("selectionHint")
    int getSelectionHint();
}
