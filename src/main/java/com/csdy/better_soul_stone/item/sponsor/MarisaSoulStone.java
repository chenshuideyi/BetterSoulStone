package com.csdy.better_soul_stone.item.sponsor;

import com.csdy.better_soul_stone.annotation.SoulStoneItems;
import com.csdy.better_soul_stone.item.BaseSoulStone;
import com.csdy.better_soul_stone.soul_stone.soul_stone_capability.ISoulStoneHit;
import com.csdy.better_soul_stone.util.SoulStoneUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDamageEvent;

@SoulStoneItems(id = "marisa_soul_stone",isSponsor = true,sponsorName = "KiRi34")
public class MarisaSoulStone extends BaseSoulStone implements ISoulStoneHit {


    @Override
    public void afterHit(LivingDamageEvent event, LivingEntity attacker, LivingEntity target, ItemStack stack) {
        if (attacker.level().isClientSide || !(attacker instanceof Player player)) return;
        SoulStoneUtil.dropStealLoot(target,player);
    }


}
