package com.csdy.better_soul_stone.util;

import com.csdy.better_soul_stone.item.BaseSoulStone;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraftforge.registries.ForgeRegistries;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;

public class SoulStoneUtil {
    //孩子们，util类不需要写单例

    public static final Random CSDY_RANDOM = new Random();

    public static String makeModifierName(Item item, Attribute attribute) {
        ResourceLocation itemKey = ForgeRegistries.ITEMS.getKey(item);
        ResourceLocation attrKey = ForgeRegistries.ATTRIBUTES.getKey(attribute);

        String itemName = (itemKey != null) ? itemKey.toString() : "unknown_item";
        String attrName = (attrKey != null) ? attrKey.toString() : "unknown_attribute";

        return itemName + "." + attrName;
    }

    @SuppressWarnings("all")
    public static void forEachActiveSoulStone(LivingEntity entity, BiConsumer<BaseSoulStone, ItemStack> action) {
        if (entity == null) return;

        CuriosApi.getCuriosHelper().getEquippedCurios(entity).ifPresent(handler -> {
            for (int i = 0; i < handler.getSlots(); i++) {
                ItemStack stack = handler.getStackInSlot(i);
                if (stack.getItem() instanceof BaseSoulStone soulStone) {
                    action.accept(soulStone, stack);
                }
            }
        });
    }

    public static List<ItemStack> getUniqueSoulStonesForRender(LivingEntity entity) {
        List<ItemStack> list = new ArrayList<>();
        forEachActiveSoulStone(entity, (item, stack) -> {
            list.add(stack);
        });
        return list;
    }

    /**
     * 物理销毁玩家饰品栏中的特定魂石并触发更新
     * @return 是否成功销毁
     */
    @SuppressWarnings("all")
    public static boolean destroySoulStone(LivingEntity entity, ItemStack targetStack) {
        if (entity == null || entity.level().isClientSide || targetStack.isEmpty()) return false;

        return CuriosApi.getCuriosHelper().getCuriosHandler(entity).map(handler -> {
            for (var entry : handler.getCurios().entrySet()) {
                IDynamicStackHandler stackHandler = entry.getValue().getStacks();

                for (int i = 0; i < stackHandler.getSlots(); i++) {
                    ItemStack stackInSlot = stackHandler.getStackInSlot(i);

                    if (stackInSlot == targetStack ||
                            (!stackInSlot.isEmpty() && stackInSlot.getItem() == targetStack.getItem())) {

                        stackHandler.setStackInSlot(i, ItemStack.EMPTY);
                        return true;
                    }
                }
            }
            return false;
        }).orElse(false);
    }

    //这样可以破封装 防止你覆写了dropAllDeathLoot
    public static void dropStealLoot(LivingEntity target, Player player) {
        if (target.level() instanceof ServerLevel serverLevel) {

            ResourceLocation lootTableLocation = target.getLootTable();
            LootTable lootTable = serverLevel.getServer().getLootData().getLootTable(lootTableLocation);

            if (lootTable == LootTable.EMPTY) return;

            LootParams params = new LootParams.Builder(serverLevel)
                    .withParameter(LootContextParams.THIS_ENTITY, target)
                    .withParameter(LootContextParams.ORIGIN, target.position())
                    .withParameter(LootContextParams.DAMAGE_SOURCE, target.damageSources().playerAttack(player))
                    .withParameter(LootContextParams.KILLER_ENTITY, player)
                    .withOptionalParameter(LootContextParams.DIRECT_KILLER_ENTITY, player)
                    .create(LootContextParamSets.ENTITY);

            lootTable.getRandomItems(params).forEach(target::spawnAtLocation);
        }
    }

    public static void spawnItemFromEntity(LivingEntity entity, ItemStack dropStack) {
        if (entity == null || entity.level().isClientSide || dropStack.isEmpty()) return;

        ItemEntity itemEntity = new ItemEntity(
                entity.level(),
                entity.getX(), entity.getY() + 0.5, entity.getZ(), // 稍微抬高一点防止掉进地板
                dropStack
        );

        itemEntity.setDeltaMovement(
                entity.level().random.nextGaussian() * 0.05D,
                0.2D,
                entity.level().random.nextGaussian() * 0.05D
        );

        entity.level().addFreshEntity(itemEntity);
    }

    public static void dropItemAt(Level level, BlockPos pos, ItemStack stack) {
        if (level.isClientSide || stack.isEmpty()) return;

        double x = pos.getX() + 0.5D;
        double y = pos.getY() + 0.5D;
        double z = pos.getZ() + 0.5D;

        ItemEntity itementity = new ItemEntity(level, x, y, z, stack);
        itementity.setDefaultPickUpDelay();
        level.addFreshEntity(itementity);
    }

    public static void repairAllArmor(LivingEntity entity, int repairAmount) {
        // 遍历盔甲栏（通常是 4 格）
        entity.getArmorSlots().forEach(armorStack -> {
            if (!armorStack.isEmpty() && armorStack.isDamageableItem()) {
                // setDamage 减去修复值，即增加耐久（0 为满耐久）
                int newDamage = Math.max(0, armorStack.getDamageValue() - repairAmount);
                armorStack.setDamageValue(newDamage);
            }
        });
    }


}
