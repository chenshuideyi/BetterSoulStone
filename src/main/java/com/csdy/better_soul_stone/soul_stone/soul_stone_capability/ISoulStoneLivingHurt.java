package com.csdy.better_soul_stone.soul_stone.soul_stone_capability;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public interface ISoulStoneLivingHurt extends ISoulStoneCapability {

    /**
     * 当持有者受到伤害时触发
     * @param event 原生事件（如果需要取消事件可以调用）
     * @param wearer 饰品持有者
     * @param source 伤害来源
     * @param amount 当前已经过其他魂石计算后的伤害数值
     * @param stack 饰品自身的堆栈（用于读取NBT等级等）
     * @return 修改后的伤害数值
     */
    float livingHurt(LivingHurtEvent event, LivingEntity wearer, DamageSource source, float amount, ItemStack stack);

}
