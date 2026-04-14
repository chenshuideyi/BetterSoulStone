package com.csdy.better_soul_stone.item.minecraft.player.creation.craft.forge.ingots.ingot;

import com.csdy.better_soul_stone.annotation.SoulStoneItems;
import com.csdy.better_soul_stone.item.BaseSoulStone;
import com.csdy.better_soul_stone.soul_stone.soul_stone_capability.ISoulStoneLivingHurt;
import com.csdy.better_soul_stone.soul_stone.soul_stone_capability.ISoulStonePickUpItem;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import static com.csdy.better_soul_stone.util.SoulStoneUtil.repairAllArmor;

@SoulStoneItems(id = "copper_soul_stone")
public class CopperSoulStone extends BaseSoulStone implements ISoulStoneLivingHurt {

    @Override
    public float livingHurt(LivingHurtEvent event, LivingEntity wearer, DamageSource source, float amount, ItemStack stack) {
        if (source.is(DamageTypes.LIGHTNING_BOLT)) {

            if (!wearer.level().isClientSide) {
                repairAllArmor(wearer, 50);

                if (wearer.level() instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(ParticleTypes.ELECTRIC_SPARK,
                            wearer.getX(), wearer.getY() + 1.0, wearer.getZ(),
                            20, 0.5, 0.5, 0.5, 0.1);
                }
            }

            return 0.0F;
        }

        return amount;
    }

}
