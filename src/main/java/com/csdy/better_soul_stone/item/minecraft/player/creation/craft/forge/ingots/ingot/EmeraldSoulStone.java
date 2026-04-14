package com.csdy.better_soul_stone.item.minecraft.player.creation.craft.forge.ingots.ingot;

import com.csdy.better_soul_stone.annotation.SoulStoneItems;
import com.csdy.better_soul_stone.item.BaseSoulStone;
import com.csdy.better_soul_stone.soul_stone.soul_stone_capability.ISoulStoneHit;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.living.LivingDamageEvent;

import static com.csdy.better_soul_stone.util.SoulStoneUtil.CSDY_RANDOM;
import static com.csdy.better_soul_stone.util.SoulStoneUtil.spawnItemFromEntity;

@SoulStoneItems(id = "emerald_soul_stone")
public class EmeraldSoulStone extends BaseSoulStone implements ISoulStoneHit{

    @Override
    public void afterHit(LivingDamageEvent event, LivingEntity attacker, LivingEntity target, ItemStack stack){
        if (target.level().isClientSide) return;

        if (target instanceof Villager) {
            spawnItemFromEntity(target, new ItemStack(Items.EMERALD, CSDY_RANDOM.nextInt(1,3)));
        }
    }

}