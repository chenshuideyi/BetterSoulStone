package com.csdy.better_soul_stone.item.minecraft.player.creation.craft.forge.ingots.ingot;

import com.csdy.better_soul_stone.annotation.SoulStoneItems;
import com.csdy.better_soul_stone.item.BaseSoulStone;
import com.csdy.better_soul_stone.soul_stone.soul_stone_capability.ISoulStoneDailyLimit;
import com.csdy.better_soul_stone.soul_stone.soul_stone_capability.ISoulStoneLivingHurt;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;


@SoulStoneItems(id = "gold_soul_stone")
public class GoldSoulStone extends BaseSoulStone implements ISoulStoneLivingHurt, ISoulStoneDailyLimit {

    private static final int INVULNERABLE_TICKS = 60;

    @Override
    public String getAbilityId() { return "gold_body_protect"; }

    @Override
    public int getMaxDailyUses() { return 3; }

    @Override
    public float livingHurt(LivingHurtEvent event, LivingEntity wearer, DamageSource source, float amount, ItemStack stack) {
        if (wearer.level().isClientSide) return amount;

        this.checkAndResetDailyLimit(wearer, stack);

        if (this.isOnCooldown(wearer, stack)) return amount;

        int remains = getRemainingUses(stack);

        if (remains > 0 && amount >= wearer.getHealth()) {
            this.consumeUse(stack);
            int nextCount = getRemainingUses(stack);

            applyGoldenBody(event, wearer);

            if (nextCount <= 0) {
                this.setManualCooldown(wearer, stack, 24000);
            } else {
                this.setManualCooldown(wearer, stack, INVULNERABLE_TICKS);
            }

            return 0;
        }
        return amount;
    }

    private void applyGoldenBody(LivingHurtEvent event, LivingEntity wearer) {
        wearer.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, INVULNERABLE_TICKS, 4, false, false, true));
        wearer.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, INVULNERABLE_TICKS, 255, false, false, false));
        wearer.addEffect(new MobEffectInstance(MobEffects.GLOWING, INVULNERABLE_TICKS, 0, false, false, false));

        wearer.level().playSound(null, wearer.blockPosition(), SoundEvents.TOTEM_USE, SoundSource.PLAYERS, 1.0F, 1.5F);
        if (wearer.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.WAX_OFF, wearer.getX(), wearer.getY() + 1.0, wearer.getZ(), 20, 0.3, 0.3, 0.3, 0.1);
        }
    }

    private void setManualCooldown(LivingEntity entity, ItemStack stack, int ticks) {
        long unlockTime = entity.level().getGameTime() + ticks;
        CompoundTag nbt = stack.getOrCreateTag();
        if (!nbt.contains("SoulStoneCooldowns")) nbt.put("SoulStoneCooldowns", new CompoundTag());
        nbt.getCompound("SoulStoneCooldowns").putLong(getAbilityId(), unlockTime);
    }
}