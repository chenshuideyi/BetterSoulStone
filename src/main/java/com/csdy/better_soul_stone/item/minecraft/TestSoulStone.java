package com.csdy.better_soul_stone.item.minecraft;

import com.csdy.better_soul_stone.BetterSoulStoneModMain;
import com.csdy.better_soul_stone.annotation.SoulStoneItems;
import com.csdy.better_soul_stone.item.BaseSoulStone;
import com.csdy.better_soul_stone.soul_stone.soul_stone_capability.ISoulStoneEquipmentChange;
import com.csdy.better_soul_stone.soul_stone.soul_stone_capability.ISoulStoneHit;
import com.csdy.better_soul_stone.soul_stone.soul_stone_capability.ISoulStoneLeftClick;
import com.csdy.better_soul_stone.soul_stone.soul_stone_capability.ISoulStoneOnAttacked;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

@SoulStoneItems(id = "test_soul_stone")
public class TestSoulStone extends BaseSoulStone implements ISoulStoneOnAttacked, ISoulStoneEquipmentChange, ISoulStoneHit, ISoulStoneLeftClick {


    @Override
    public boolean onAttacked(LivingAttackEvent event, LivingEntity attacker, Entity target, ItemStack stack) {
        return true;
    }

    @Override
    public void onEquip(LivingEntity wearer, ItemStack stack) {
        BetterSoulStoneModMain.LOGGER.info("onEquip!!!!!!!!" + wearer.toString() + "!!!!!!!!!!" + stack.toString());
    }

    @Override
    public void onUnequip(LivingEntity wearer, ItemStack stack) {
        BetterSoulStoneModMain.LOGGER.info("onUnequip!!!!!!!!!!" + wearer.toString() + "!!!!!!!!!!" + stack.toString());
    }

    @Override
    public void beforeHit(LivingHurtEvent event, LivingEntity attacker, LivingEntity target, ItemStack stack) {
        target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 600, 0));
    }

    @Override
    public void afterHit(LivingDamageEvent event, LivingEntity attacker, LivingEntity target, ItemStack stack) {
        attacker.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 600, 0));
    }

    @Override
    public void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event, Player player, Level level, BlockPos pos, BlockState state, ItemStack stack) {
        BetterSoulStoneModMain.LOGGER.info("onLeftClick!!!!!!!!!!" + player.toString() + "!!!!!!!!!!" + stack.toString());
        player.addEffect(new MobEffectInstance(MobEffects.LUCK, 600, 0));
    }

    @Override
    public void onLeftClickEmpty(PlayerInteractEvent.LeftClickEmpty event, Player player, Level level, ItemStack stack) {
        BetterSoulStoneModMain.LOGGER.info("onLeftEmpty!!!!!!!!!!" + player.toString() + "!!!!!!!!!!" + stack.toString());
        player.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 600, 0));
        if (!level.isClientSide) {
            player.displayClientMessage(net.minecraft.network.chat.Component.literal("你挥动了空气中的魂石"), true);
        }
    }

    @Override
    public void onLeftClickEntity(AttackEntityEvent event, Player player, Entity target, ItemStack stack) {
        if (event.getTarget() instanceof LivingEntity livingEntity) {
            livingEntity.setHealth(0);
        }
    }

//    @Override
//    public void afterHit(LivingHurtEvent event, LivingEntity attacker, LivingEntity target, ItemStack stack) {
//        target.addEffect(new MobEffectInstance(MobEffects.WITHER));
//    }
//
//    @Override
//    public void failedMeleeHit(LivingEntity attacker, LivingEntity target, ItemStack stack, float damageAttempted) {
//        target.setHealth(0);
//    }

}
