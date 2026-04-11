package com.csdy.better_soul_stone.item.sponsor;

import com.csdy.better_soul_stone.BetterSoulStoneModMain;
import com.csdy.better_soul_stone.annotation.SoulStoneItems;
import com.csdy.better_soul_stone.item.BaseSoulStone;
import com.csdy.better_soul_stone.soul_stone.soul_stone_capability.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

@SoulStoneItems(id = "player_7_soul_stone", isSponsor = true, sponsorName = "PlayerVII")
public class PlayerVIISoulStone extends BaseSoulStone implements ISoulStoneLivingHurt, ISoulStoneDamage, ISoulStoneOnHeal, ISoulStoneDoubleClick, ISoulStoneEquipmentChange {

    @Override
    public float livingHurt(LivingHurtEvent event, LivingEntity wearer, DamageSource source, float amount, ItemStack stack) {
        return amount * 4.0F;
    }

    @Override
    public float onDealingDamage(LivingHurtEvent event, LivingEntity attacker, LivingEntity target, DamageSource source, float amount, ItemStack stack) {
        float multiplier = 1.0F;
        if (attacker.getHealth() >= attacker.getMaxHealth()) {
            multiplier *= 0.5F;
        }
        if (attacker.getHealth() <= 1.0F) {
            multiplier = 2.0F;
        }
        return amount * multiplier;
    }

    @Override
    public void onEquip(LivingEntity wearer, ItemStack stack) {
        wearer.setHealth(1);
    }

    @Override
    public float onHeal(LivingHealEvent event, LivingEntity entity, float amount, ItemStack stack) {
        return 0;
    }

    @Override
    public void onDoubleClick(ItemStack stack, Player player, String keyType) {
        double speed = 2.8;
        float yaw = player.getYRot();
        Vec3 moveVec = Vec3.ZERO;

        Vec3 forward = Vec3.directionFromRotation(0, yaw);
        Vec3 right = Vec3.directionFromRotation(0, yaw + 90);

        switch (keyType) {
            case "key.forward" ->
                    moveVec = forward.scale(speed);
            case "key.back" ->
                    moveVec = forward.scale(-speed * 0.8);
            case "key.left" ->
                    moveVec = right.scale(-speed * 0.8);
            case "key.right" ->
                    moveVec = right.scale(speed * 0.8);
        }

        if (moveVec != Vec3.ZERO) {
            player.setDeltaMovement(moveVec.x, player.getDeltaMovement().y + 0.1, moveVec.z);
            player.hurtMarked = true;
        }
    }

}