package com.csdy.better_soul_stone.event;

import com.csdy.better_soul_stone.BetterSoulStoneModMain;
import com.csdy.better_soul_stone.soul_stone.ISoulStoneOnAttacked;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.function.BiConsumer;

import static com.csdy.better_soul_stone.soul_stone.ISoulStoneOnAttacked.dispatchAttackTrigger;

@Mod.EventBusSubscriber(modid = BetterSoulStoneModMain.MODID)
public class SoulStoneEventHandler {


    @SubscribeEvent
    public static void onLivingAttack(LivingAttackEvent event) {
        var target = event.getEntity();
        if (target == null) return;
        Entity attacker = event.getSource().getEntity();
        boolean shouldCancel = dispatchAttackTrigger(target, event, attacker);
        if (shouldCancel) event.setCanceled(true);
    }


    @SuppressWarnings("all")
    private static <T> void dispatch(LivingEntity entity, Class<T> clazz, BiConsumer<T, ItemStack> action) {
        CuriosApi.getCuriosHelper().getCuriosHandler(entity).ifPresent(handler -> {
            handler.findCurios(stack -> clazz.isInstance(stack.getItem())).forEach(result -> {
                ItemStack stack = result.stack();
                action.accept(clazz.cast(stack.getItem()), stack);
            });
        });
    }

}
