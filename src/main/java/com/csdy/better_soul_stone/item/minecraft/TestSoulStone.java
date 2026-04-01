package com.csdy.better_soul_stone.item.minecraft;

import com.csdy.better_soul_stone.BetterSoulStoneModMain;
import com.csdy.better_soul_stone.annotation.SoulStoneItems;
import com.csdy.better_soul_stone.item.BaseSoulStone;
import com.csdy.better_soul_stone.soul_stone.soul_stone_capability.*;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

@SoulStoneItems(id = "test_soul_stone")
public class TestSoulStone extends BaseSoulStone implements ISoulStoneOnAttacked, ISoulStoneEquipmentChange, ISoulStoneHit, ISoulStoneLeftClick, ISoulStoneDoubleClick {


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

    public boolean hasCustomToolTipGlint(ItemStack stack) {
        return true;
    }

    @Override
    public int getToolTipGlintColor(ItemStack stack) {
        long time = Util.getMillis();
        float alpha = (float) (Math.sin(time / 500.0) * 0.5 + 0.5);
        int r = (int) (0x88 + 0x77 * alpha);
        return (0xFF << 24) | (r << 16) | (0 << 8) | 0;
    }

    @Override
    public void onDoubleClick(ItemStack stack, Player player, String keyType) {
        // 基础冲刺强度
        double speed = 6.5;
        var level = player.level();
        // 获取实体当前的朝向（偏航角），用于计算左/右/后退的方向
        float yaw = player.getYRot();
        Vec3 moveVec = Vec3.ZERO;

        switch (keyType) {
            case "key.forward" -> {
                // 向前冲刺：直接取视线方向（去掉 Y 轴影响，防止飞天或钻地）
                moveVec = player.getLookAngle().multiply(1, 0, 1).normalize().scale(speed);
            }
            case "key.back" -> {
                // 向后冲刺：视线方向反向
                moveVec = player.getLookAngle().multiply(1, 0, 1).normalize().scale(-speed * 0.8);
            }
            case "key.left" -> {
                // 向左冲刺：偏航角 -90 度
                moveVec = Vec3.directionFromRotation(0, yaw - 90).scale(speed * 0.8);
            }
            case "key.right" -> {
                // 向右冲刺：偏航角 +90 度
                moveVec = Vec3.directionFromRotation(0, yaw + 90).scale(speed * 0.8);
            }

        }

        if (!level.isClientSide) {
            player.displayClientMessage(net.minecraft.network.chat.Component.literal("双击成功！发动魂石冲刺"), true);
        }

        if (moveVec != Vec3.ZERO) {
            // 应用冲刺速度
            player.setDeltaMovement(moveVec.x, player.getDeltaMovement().y + 0.1, moveVec.z);
            // 标记客户端需要更新运动状态（防止拉回）
            player.hurtMarked = true;
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
