package com.csdy.better_soul_stone.item.minecraft.world.level.overworld.terra.ores.ore;

import com.csdy.better_soul_stone.BetterSoulStoneModMain;
import com.csdy.better_soul_stone.annotation.SoulStoneItems;
import com.csdy.better_soul_stone.item.BaseSoulStone;
import com.csdy.better_soul_stone.soul_stone.soul_stone_capability.ISoulStoneDamage;
import com.csdy.better_soul_stone.soul_stone.soul_stone_capability.ISoulStoneOnAttacked;
import com.csdy.better_soul_stone.util.SoulStoneUtil;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

@SoulStoneItems(id = "diamond_soul_stone")
public class DiamondSoulStone extends BaseSoulStone implements ISoulStoneDamage, ISoulStoneOnAttacked {

    @Override
    public float onDealingDamage(LivingHurtEvent event, LivingEntity attacker, LivingEntity target, DamageSource source, float amount, ItemStack stack) {
        return amount * 5;
    }

    @Override
    public boolean onAttacked(LivingAttackEvent event, LivingEntity wearer, Entity attacker, ItemStack stack) {
        if (!wearer.level().isClientSide) {
            // 使用封装的方法
            if (SoulStoneUtil.destroySoulStone(wearer, stack)) {
                BetterSoulStoneModMain.LOGGER.info("钻石魂石已物理销毁并抵消伤害");

                wearer.level().playSound(null, wearer.getX(), wearer.getY(), wearer.getZ(),
                        net.minecraft.sounds.SoundEvents.ITEM_BREAK,
                        net.minecraft.sounds.SoundSource.PLAYERS, 1.0F, 1.0F);
            }
        }
        // 返回 false 取消伤害事件
        return false;
    }
}
