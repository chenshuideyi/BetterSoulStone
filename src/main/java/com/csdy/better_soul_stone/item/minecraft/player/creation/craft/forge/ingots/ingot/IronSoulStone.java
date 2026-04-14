package com.csdy.better_soul_stone.item.minecraft.player.creation.craft.forge.ingots.ingot;

import com.csdy.better_soul_stone.annotation.SoulStoneItems;
import com.csdy.better_soul_stone.item.BaseSoulStone;
import com.csdy.better_soul_stone.soul_stone.soul_stone_capability.ISoulStoneLivingHurt;
import com.csdy.better_soul_stone.soul_stone.soul_stone_capability.ISoulStoneOnBlockBreak;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.level.BlockEvent;

import static com.csdy.better_soul_stone.util.SoulStoneUtil.dropItemAt;

@SoulStoneItems(id = "iron_soul_stone")
public class IronSoulStone extends BaseSoulStone implements ISoulStoneLivingHurt {

    @Override
    public float livingHurt(LivingHurtEvent event, LivingEntity wearer, DamageSource source, float amount, ItemStack stack) {
        Entity attacker = source.getEntity();

        if (attacker != null) {
            if (isBackAttack(wearer, attacker)) {
                wearer.level().playSound(null, wearer.getX(), wearer.getY(), wearer.getZ(),
                        SoundEvents.ZOMBIE_ATTACK_IRON_DOOR, SoundSource.PLAYERS, 0.5F, 1.5F);

                return amount * 0.7F;
            }
        }

        return amount;
    }

    private boolean isBackAttack(LivingEntity victim, Entity attacker) {
        Vec3 lookVec = victim.getViewVector(1.0F);
        Vec3 attackVec = attacker.position().subtract(victim.position()).normalize();
        return lookVec.dot(attackVec) < 0;
    }

}
