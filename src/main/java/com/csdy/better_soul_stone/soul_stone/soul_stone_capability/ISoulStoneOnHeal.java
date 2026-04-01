package com.csdy.better_soul_stone.soul_stone.soul_stone_capability;

import com.csdy.better_soul_stone.soul_stone.manager.SoulStoneManager;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHealEvent;

public interface ISoulStoneOnHeal extends ISoulStoneCapability {

    /**
     * 当生物尝试回血时触发
     * @return 修改后的治疗量。如果返回 0 或取消事件，则不回血。
     */
    float onHeal(LivingHealEvent event, LivingEntity entity, float amount, ItemStack stack);

    static float dispatchHealTrigger(LivingEntity entity, LivingHealEvent event, float amount) {
        final float[] finalAmount = {amount};

        SoulStoneManager.forEachStone(entity, ISoulStoneOnHeal.class, (logic, stack) -> {
            finalAmount[0] = logic.onHeal(event, entity, finalAmount[0], stack);
        });

        return finalAmount[0];
    }
}
