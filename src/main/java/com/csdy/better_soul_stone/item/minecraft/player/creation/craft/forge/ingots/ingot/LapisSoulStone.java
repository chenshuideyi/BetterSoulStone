package com.csdy.better_soul_stone.item.minecraft.player.creation.craft.forge.ingots.ingot;

import com.csdy.better_soul_stone.annotation.SoulStoneItems;
import com.csdy.better_soul_stone.item.BaseSoulStone;
import com.csdy.better_soul_stone.soul_stone.soul_stone_capability.ISoulStoneOnBlockBreak;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.level.BlockEvent;
import top.theillusivec4.curios.api.SlotContext;

import static com.csdy.better_soul_stone.util.SoulStoneUtil.dropItemAt;

@SoulStoneItems(id = "lapis_soul_stone")
public class LapisSoulStone extends BaseSoulStone {


    @Override
    public int getLootingLevel(SlotContext slotContext, DamageSource source, LivingEntity target, int baseLooting, ItemStack stack) {
        return 3;
    }

    @Override
    public int getFortuneLevel(SlotContext slotContext, net.minecraft.world.level.storage.loot.LootContext lootContext, ItemStack stack) {
        return 3;
    }


}
