package com.csdy.better_soul_stone.item.minecraft.player.creation.craft.forge.ingots.ingot;

import com.csdy.better_soul_stone.annotation.SoulStoneItems;
import com.csdy.better_soul_stone.item.BaseSoulStone;
import com.csdy.better_soul_stone.soul_stone.soul_stone_capability.ISoulStoneHit;
import com.csdy.better_soul_stone.soul_stone.soul_stone_capability.ISoulStoneTick;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingDamageEvent;

import static com.csdy.better_soul_stone.util.SoulStoneUtil.CSDY_RANDOM;
import static com.csdy.better_soul_stone.util.SoulStoneUtil.spawnItemFromEntity;

@SoulStoneItems(id = "coal_soul_stone")
public class CoalSoulStone extends BaseSoulStone implements ISoulStoneTick{

    private static final int PRODUCTION_COOLDOWN = 12000;

    @Override
    public void onTick(ItemStack stack, LivingEntity entity,Level level) {
        if (level.isClientSide || !(entity instanceof Player player)) return;

        if (!this.isOnCooldown(player, stack)) {

            ItemStack diamond = new ItemStack(Items.DIAMOND);
            if (!player.getInventory().add(diamond)) {
                player.drop(diamond, false);
            }

            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.PLAYERS, 1.0F, 1.5F);

            if (level instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.ENCHANT,
                        player.getX(), player.getY() + 2.0, player.getZ(),
                        15, 0.5, 0.5, 0.5, 0.02);
            }

            this.setCooldown(player, stack);
        }
    }

    @Override
    public int getCooldownTicks() {
        return PRODUCTION_COOLDOWN;
    }

    @Override
    public String getAbilityId() {
        return "coal_to_diamond_production";
    }


}
