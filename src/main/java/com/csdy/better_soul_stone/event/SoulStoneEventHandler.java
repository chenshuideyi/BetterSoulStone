package com.csdy.better_soul_stone.event;

import com.csdy.better_soul_stone.BetterSoulStoneModMain;
import com.csdy.better_soul_stone.soul_stone.manager.SoulStoneManager;
import com.csdy.better_soul_stone.soul_stone.soul_stone_capability.ISoulStoneEquipmentChange;
import com.csdy.better_soul_stone.soul_stone.soul_stone_capability.ISoulStoneHit;
import com.csdy.better_soul_stone.soul_stone.soul_stone_capability.ISoulStoneTick;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.event.CurioChangeEvent;

import java.util.function.BiConsumer;

import static com.csdy.better_soul_stone.soul_stone.soul_stone_capability.ISoulStoneOnAttacked.dispatchAttackTrigger;

@Mod.EventBusSubscriber(modid = BetterSoulStoneModMain.MODID)
public class SoulStoneEventHandler {

    /**
     * 伤害结算：AfterHit
     * 使用 LivingDamageEvent 是因为此时伤害已经计算了防御和抗性
     * 适合用于吸血、击退或其他基于“最终造成的伤害值”的效果
     */
    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent event) {
        ISoulStoneHit.dispatchAfterHit(event);
    }

    @SubscribeEvent
    public static void onCurioChange(CurioChangeEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity == null || entity.level().isClientSide) return;

        var oldStones = SoulStoneManager.getStones(entity, ISoulStoneEquipmentChange.class);
        SoulStoneManager.refresh(entity);
        var newStones = SoulStoneManager.getStones(entity, ISoulStoneEquipmentChange.class);

        ISoulStoneEquipmentChange.dispatchChangeTrigger(entity, oldStones, newStones);
    }

    @SubscribeEvent
    public static void onLivingAttack(LivingAttackEvent event) {
        var target = event.getEntity();
        if (target == null) return;

        Entity attackerSource = event.getSource().getEntity();
        if (attackerSource instanceof LivingEntity attacker) {
            ISoulStoneHit.checkAttackFailure(event);
        }

        boolean shouldCancel = dispatchAttackTrigger(target, event, attackerSource);
        if (shouldCancel) event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity == null || entity.level().isClientSide) return;
        SoulStoneManager.getStones(entity, ISoulStoneTick.class).forEach(result -> {
            ItemStack stack = result.stack();
            if (stack.getItem() instanceof ISoulStoneTick ticker) {
                ticker.onTick(stack, entity);
            }
        });
    }


//    @SuppressWarnings("all")
//    private static <T> void dispatch(LivingEntity entity, Class<T> clazz, BiConsumer<T, ItemStack> action) {
//        CuriosApi.getCuriosHelper().getCuriosHandler(entity).ifPresent(handler -> {
//            handler.findCurios(stack -> clazz.isInstance(stack.getItem())).forEach(result -> {
//                ItemStack stack = result.stack();
//                action.accept(clazz.cast(stack.getItem()), stack);
//            });
//        });
//    }

}
