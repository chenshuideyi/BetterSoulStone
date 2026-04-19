package com.csdy.better_soul_stone.item.minecraft.world.entity.living.undead.undead;

import com.csdy.better_soul_stone.annotation.SoulStoneItems;
import com.csdy.better_soul_stone.item.BaseSoulStone;
import com.csdy.better_soul_stone.soul_stone.soul_stone_capability.*;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.WitherSkull;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import static com.csdy.better_soul_stone.util.SoulStoneUtil.dropItemAt;

@SoulStoneItems(id = "wither_soul_stone", droppedBy = "minecraft:wither", chance = 1)
public class WitherSoulStone extends BaseSoulStone implements ISoulStoneLivingHurt, ISoulStoneOnDeath, ISoulStoneLeftClick {

    @Override
    public float livingHurt(LivingHurtEvent event, LivingEntity wearer, DamageSource source, float amount, ItemStack stack) {
        if (wearer.level().isClientSide || source.getDirectEntity() == null) return amount;

        Entity attacker = source.getDirectEntity();

        if (attacker instanceof LivingEntity livingAttacker) {
            livingAttacker.addEffect(new MobEffectInstance(MobEffects.WITHER, 100, 1));
        }

//        if (wearer.getRandom().nextFloat() < 0.20f) {
//            double d0 = attacker.getX() - wearer.getX();
//            double d1 = attacker.getY(0.5D) - wearer.getY(0.5D);
//            double d2 = attacker.getZ() - wearer.getZ();
//
//            WitherSkull skull = new WitherSkull(wearer.level(), wearer, d0, d1, d2);
//            skull.setDangerous(wearer instanceof WitherBoss);
//            skull.setPos(wearer.getX(), wearer.getY() + wearer.getEyeHeight(), wearer.getZ());
//
//            wearer.level().addFreshEntity(skull);
//        }

        return amount;
    }

//    @Override
//    public void onLeftClickEmpty(PlayerInteractEvent.LeftClickEmpty event, Player player, Level level, ItemStack soulStone) {
//        if (!level.isClientSide) {
//            Vec3 look = player.getLookAngle();
//            double speedMultiplier = 3.0D;
//            WitherSkull skull = new WitherSkull(level, player, look.x, look.y, look.z);
//
//            skull.setPos(
//                    player.getX() + look.x * 1.5D,
//                    player.getY() + player.getEyeHeight() + look.y * 1.5D,
//                    player.getZ() + look.z * 1.5D
//            );
//            skull.setDeltaMovement(look.scale(speedMultiplier));
//            skull.setDangerous(true);
//            level.addFreshEntity(skull);
//        }
//    }

    @Override
    public void onLeftClickEmpty(PlayerInteractEvent.LeftClickEmpty event, Player player, Level level, ItemStack soulStone) {
        if (!level.isClientSide) {
            Vec3 look = player.getLookAngle();
            double speedMultiplier = 2.5D;

            Vec3 rightVec = look.cross(new Vec3(0, 1, 0)).normalize().scale(0.5);

            WitherSkull blueSkull = new WitherSkull(level, player, look.x, look.y, look.z);
            blueSkull.setPos(
                    player.getX() + look.x * 1.5D + rightVec.x,
                    player.getY() + player.getEyeHeight() + look.y * 1.5D,
                    player.getZ() + look.z * 1.5D + rightVec.z
            );
            blueSkull.setDeltaMovement(look.scale(speedMultiplier));
            blueSkull.setDangerous(true); // 蓝
            level.addFreshEntity(blueSkull);

            WitherSkull blackSkull = new WitherSkull(level, player, look.x, look.y, look.z);
            blackSkull.setPos(
                    player.getX() + look.x * 1.5D - rightVec.x,
                    player.getY() + player.getEyeHeight() + look.y * 1.5D,
                    player.getZ() + look.z * 1.5D - rightVec.z
            );
            blackSkull.setDeltaMovement(look.scale(speedMultiplier));
            blackSkull.setDangerous(false); // 黑
            level.addFreshEntity(blackSkull);

            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.WITHER_SHOOT, SoundSource.PLAYERS, 0.7F, 1.2F);
        }
    }

    @Override
    public void onDeath(LivingEntity living, DamageSource source, ItemStack soulStone) {
        if (living.level().isClientSide) return;
        dropItemAt(living.level(), living.blockPosition(), new ItemStack(Items.WITHER_SKELETON_SKULL, 3));

    }

}
