package com.csdy.better_soul_stone.item.minecraft.player.creation.craft.forge.ingots.ingot;

import com.csdy.better_soul_stone.BetterSoulStoneModMain;
import com.csdy.better_soul_stone.annotation.SoulStoneItems;
import com.csdy.better_soul_stone.item.BaseSoulStone;
import com.csdy.better_soul_stone.soul_stone.soul_stone_capability.ISoulStoneHit;
import com.csdy.better_soul_stone.soul_stone.soul_stone_capability.ISoulStoneTick;
import com.csdy.better_soul_stone.soul_stone.soul_stone_capability.ISoulStoneTrade;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MerchantMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingDamageEvent;

import static com.csdy.better_soul_stone.util.SoulStoneUtil.CSDY_RANDOM;
import static com.csdy.better_soul_stone.util.SoulStoneUtil.spawnItemFromEntity;

@SoulStoneItems(id = "emerald_soul_stone")
public class EmeraldSoulStone extends BaseSoulStone implements ISoulStoneTrade {

    @Override
    public void onTradeSuccess(MerchantOffer offer, LivingEntity entity,ItemStack stack) {
        if (entity.level().isClientSide || !(entity instanceof Player player)) return;

        ItemStack bonus = offer.getResult().copy();

        if (!player.getInventory().add(bonus)) {
            BetterSoulStoneModMain.LOGGER.debug("EmeraldSoul is already ");
            stack.setCount(stack.getCount() * 2);
        }

        player.level().playSound(null, player.blockPosition(),
                SoundEvents.VILLAGER_YES, SoundSource.PLAYERS, 1.0F, 1.0F);
    }

}