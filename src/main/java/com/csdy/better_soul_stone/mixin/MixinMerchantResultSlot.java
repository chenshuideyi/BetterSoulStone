package com.csdy.better_soul_stone.mixin;

import com.csdy.better_soul_stone.BetterSoulStoneModMain;
import com.csdy.better_soul_stone.soul_stone.manager.SoulStoneManager;
import com.csdy.better_soul_stone.soul_stone.soul_stone_capability.ISoulStoneTrade;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MerchantContainer;
import net.minecraft.world.inventory.MerchantResultSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.Merchant;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MerchantResultSlot.class)
public class MixinMerchantResultSlot {

    @Shadow @Final private Player player;
    @Shadow @Final private Merchant merchant;
    @Shadow @Final private MerchantContainer slots;

    @Inject(method = "onTake", at = @At("TAIL"))
    private void afterOnTake(Player player, ItemStack stack, CallbackInfo ci) {
        BetterSoulStoneModMain.LOGGER.debug("afterOnTake is already ");
        ISoulStoneTrade.dispatchOnTradeSuccess(this.player, this.slots, this.merchant.getOffers(),stack);
    }
}
