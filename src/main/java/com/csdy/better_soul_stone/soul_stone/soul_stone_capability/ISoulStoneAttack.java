package com.csdy.better_soul_stone.soul_stone.soul_stone_capability;

import net.minecraftforge.event.entity.living.LivingHurtEvent;

public interface ISoulStoneAttack extends ISoulStoneCapability {

    void attack(LivingHurtEvent event);

}
