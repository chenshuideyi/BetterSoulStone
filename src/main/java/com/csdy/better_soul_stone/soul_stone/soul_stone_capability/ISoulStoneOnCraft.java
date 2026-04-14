package com.csdy.better_soul_stone.soul_stone.soul_stone_capability;

import com.csdy.better_soul_stone.soul_stone.manager.SoulStoneManager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.event.entity.player.PlayerEvent;
import org.jetbrains.annotations.Nullable;

public interface ISoulStoneOnCraft extends ISoulStoneCapability {

    void onCraft(Player player, ItemStack crafted, @Nullable Recipe<?> recipe, ItemStack soulStone);

    static void dispatchCraftTrigger(Player player, ItemStack crafted, @Nullable Recipe<?> recipe) {
        if (crafted.isEmpty()) return;
        SoulStoneManager.forEachLogic(player, ISoulStoneOnCraft.class, (capability, stack) -> {
            capability.onCraft(player, crafted, recipe, stack);
        });
    }
}