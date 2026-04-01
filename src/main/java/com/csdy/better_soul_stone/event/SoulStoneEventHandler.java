package com.csdy.better_soul_stone.event;

import com.csdy.better_soul_stone.BetterSoulStoneModMain;
import com.csdy.better_soul_stone.network.BetterSoulStoneSyncing;
import com.csdy.better_soul_stone.network.packet.LeftClickEmptyPacket;
import com.csdy.better_soul_stone.soul_stone.manager.SoulStoneManager;
import com.csdy.better_soul_stone.soul_stone.soul_stone_capability.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.event.CurioChangeEvent;

@Mod.EventBusSubscriber(modid = BetterSoulStoneModMain.MODID)
public class SoulStoneEventHandler {


    @SubscribeEvent
    public static void onCurioChange(CurioChangeEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity == null || entity.level().isClientSide) return;

        var oldStones = SoulStoneManager.getStones(entity, ISoulStoneEquipmentChange.class);
        SoulStoneManager.refresh(entity);
        var newStones = SoulStoneManager.getStones(entity, ISoulStoneEquipmentChange.class);

        ISoulStoneEquipmentChange.dispatchChangeTrigger(entity, oldStones, newStones);
    }

    @SubscribeEvent
    public static void beforeLivingHit(LivingHurtEvent event) {
        if (event.getSource().getEntity() instanceof LivingEntity attacker) {
            ISoulStoneHit.dispatchBeforeHit(event, attacker, event.getEntity());
        }
    }

    @SubscribeEvent
    public static void afterLivingHit(LivingDamageEvent event) {
        if (event.getSource().getEntity() instanceof LivingEntity attacker) {
            ISoulStoneHit.dispatchAfterHit(event, attacker, event.getEntity());
        }
    }

    @SubscribeEvent
    public static void onLeftClickEntity(AttackEntityEvent event) {
        ISoulStoneLeftClick.dispatchLeftClickEntity(event);
    }

    @SubscribeEvent
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        ISoulStoneLeftClick.dispatchLeftClickBlock(event);
    }

    @SubscribeEvent
    public static void onEntityItemPickup(EntityItemPickupEvent event) {
        ISoulStonePickUpItem.dispatch(event.getEntity(), event.getItem());
    }

    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingTickEvent event) {
        ISoulStoneTick.dispatch(event.getEntity());
        if (event.getEntity() instanceof Player player) {
            ISoulStoneHover.dispatchHoverTrigger(player);
        }
    }

    @SubscribeEvent
    public static void onLeftClickEmpty(PlayerInteractEvent.LeftClickEmpty event) {
        if (event.getLevel().isClientSide) {
            BetterSoulStoneSyncing.CHANNEL.sendToServer(new LeftClickEmptyPacket());
        }
    }

    @SubscribeEvent
    public static void onLivingAttack(LivingAttackEvent event) {
        LivingEntity victim = event.getEntity();
        Entity attacker = event.getSource().getEntity();

        if (ISoulStoneOnAttacked.dispatchAttackTrigger(victim, event, attacker)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {

        LivingEntity victim = event.getEntity();
        DamageSource source = event.getSource();
        float originalAmount = event.getAmount();
        float newAmount = originalAmount;

        if (source.getEntity() instanceof LivingEntity attacker && attacker != null) {
            newAmount = ISoulStoneDamage.dispatchDamageTrigger(attacker, event, victim, source, newAmount);
        }

        newAmount = ISoulStoneLivingHurt.dispatchHurtTrigger(victim, event, source, newAmount);

        if (newAmount != originalAmount) {
            event.setAmount(newAmount);
        }
    }

    @SubscribeEvent
    public static void onLivingHeal(LivingHealEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity == null) return;

        float originalAmount = event.getAmount();

        float newAmount = ISoulStoneOnHeal.dispatchHealTrigger(entity, event, originalAmount);

        if (newAmount <= 0 || event.isCanceled()) {
            event.setAmount(0);
            event.setCanceled(true);
        } else if (newAmount != originalAmount) {
            event.setAmount(newAmount);
        }
    }

}
