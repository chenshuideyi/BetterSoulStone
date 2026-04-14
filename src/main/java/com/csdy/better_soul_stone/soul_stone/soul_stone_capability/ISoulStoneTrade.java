package com.csdy.better_soul_stone.soul_stone.soul_stone_capability;

import com.csdy.better_soul_stone.BetterSoulStoneModMain;
import com.csdy.better_soul_stone.mixin.MerchantContainerAccessor;
import com.csdy.better_soul_stone.soul_stone.manager.SoulStoneManager;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MerchantContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;

public interface ISoulStoneTrade extends ISoulStoneCapability{

    void onTradeSuccess(MerchantOffer offer, LivingEntity entity, ItemStack stack);

    static void dispatchOnTradeSuccess(Player player, MerchantContainer container, MerchantOffers offers, ItemStack takenStack) {
        if (player == null || container == null || offers == null) return;

        int index = ((MerchantContainerAccessor) container).getSelectionHint();

        if (index >= 0 && index < offers.size()) {
            MerchantOffer activeOffer = offers.get(index);

            SoulStoneManager.forEachLogic(player, ISoulStoneTrade.class, (logic, itemStack) ->
                    logic.onTradeSuccess(activeOffer, player, takenStack)
            );
        }
    }

}
