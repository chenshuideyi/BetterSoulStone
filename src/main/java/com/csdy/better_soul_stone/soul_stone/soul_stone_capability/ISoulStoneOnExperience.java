package com.csdy.better_soul_stone.soul_stone.soul_stone_capability;

import com.csdy.better_soul_stone.soul_stone.manager.SoulStoneManager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerXpEvent;

public interface ISoulStoneOnExperience extends ISoulStoneCapability {

    void onExperienceAdd(PlayerXpEvent.XpChange event, Player player, int amount, ItemStack stack);

    static void dispatch(PlayerXpEvent.XpChange event, Player player) {
        int amount = event.getAmount();
        SoulStoneManager.forEachStone(player, ISoulStoneOnExperience.class, (logic, stack) ->
                logic.onExperienceAdd(event, player, amount, stack));
    }
}