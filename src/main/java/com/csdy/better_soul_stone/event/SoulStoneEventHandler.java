package com.csdy.better_soul_stone.event;

import com.csdy.better_soul_stone.BetterSoulStoneModMain;
import com.csdy.better_soul_stone.item.sponsor.PlayerVIISoulStone;
import com.csdy.better_soul_stone.network.BetterSoulStoneSyncing;
import com.csdy.better_soul_stone.network.packet.LeftClickEmptyPacket;
import com.csdy.better_soul_stone.soul_stone.manager.SoulStoneManager;
import com.csdy.better_soul_stone.soul_stone.soul_stone_capability.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import top.theillusivec4.curios.api.event.CurioChangeEvent;

@Mod.EventBusSubscriber(modid = BetterSoulStoneModMain.MODID)
public class SoulStoneEventHandler {


    @SubscribeEvent
    public static void onCurioChange(CurioChangeEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity == null || entity.level().isClientSide) return;

        SoulStoneManager.forEachLogic(entity, ISoulStoneEquipmentChange.class, (logic, stack) -> {
            logic.onUnequip(entity, stack);
        });

        SoulStoneManager.refresh(entity);


        SoulStoneManager.forEachLogic(entity, ISoulStoneEquipmentChange.class, (logic, stack) -> {
            logic.onEquip(entity, stack);
        });

    }

    @SubscribeEvent
    public static void beforeLivingHit(LivingHurtEvent event) {
        if (event.getSource().getEntity() instanceof LivingEntity attacker) {
            ISoulStoneHit.dispatchBeforeHit(event, attacker, event.getEntity());
        }
    }

    @SubscribeEvent
    public static void afterLivingHit(LivingDamageEvent event) {
        if (event.getSource().getEntity() instanceof LivingEntity attacker) {
            ISoulStoneHit.dispatchAfterHit(event, attacker, event.getEntity());
        }
    }

    @SubscribeEvent
    public static void onLeftClickEntity(AttackEntityEvent event) {
        ISoulStoneLeftClick.dispatchLeftClickEntity(event);
    }

    @SubscribeEvent
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        ISoulStoneLeftClick.dispatchLeftClickBlock(event);
    }

    @SubscribeEvent
    public static void onEntityItemPickup(EntityItemPickupEvent event) {
        ISoulStonePickUpItem.dispatch(event.getEntity(), event.getItem());
    }

    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingTickEvent event) {
        ISoulStoneTick.dispatch(event.getEntity());
        if (event.getEntity() instanceof Player player) {
            ISoulStoneHover.dispatchHoverTrigger(player);
        }
    }

    @SubscribeEvent
    public static void onLeftClickEmpty(PlayerInteractEvent.LeftClickEmpty event) {
        if (event.getLevel().isClientSide) {
            BetterSoulStoneSyncing.CHANNEL.sendToServer(new LeftClickEmptyPacket());
        }
    }

    @SubscribeEvent
    public static void onLivingAttack(LivingAttackEvent event) {
        LivingEntity victim = event.getEntity();
        Entity attacker = event.getSource().getEntity();

        if (ISoulStoneOnAttacked.dispatchAttackTrigger(victim, event, attacker)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {

        LivingEntity victim = event.getEntity();
        DamageSource source = event.getSource();
        float originalAmount = event.getAmount();
        float newAmount = originalAmount;

        if (source.getEntity() instanceof LivingEntity attacker && attacker != null) {
            newAmount = ISoulStoneDamage.dispatchDamageTrigger(attacker, event, victim, source, newAmount);
        }

        newAmount = ISoulStoneLivingHurt.dispatchHurtTrigger(victim, event, source, newAmount);

        if (newAmount != originalAmount) {
            event.setAmount(newAmount);
        }
    }

    @SubscribeEvent
    public static void onLivingHeal(LivingHealEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity == null) return;

        float originalAmount = event.getAmount();

        float newAmount = ISoulStoneOnHeal.dispatchHealTrigger(entity, event, originalAmount);

        if (newAmount <= 0 || event.isCanceled()) {
            event.setAmount(0);
            event.setCanceled(true);
        } else if (newAmount != originalAmount) {
            event.setAmount(newAmount);
        }
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        Player player = event.getEntity();
        if (player == null || player.level().isClientSide) return;

        ISoulStoneOnRespawn.dispatchRespawnTrigger(player);
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        if (event.getPlayer() != null && !event.getPlayer().level().isClientSide) {
            ISoulStoneOnBlockBreak.dispatch(event, event.getPlayer());
        }
    }

    @SubscribeEvent
    public static void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        if (event.getEntity() instanceof Player player && !player.level().isClientSide) {
            ISoulStoneOnBlockPlace.dispatch(event, player);
        }
    }

    @SubscribeEvent
    public static void onExperienceChange(PlayerXpEvent.XpChange event) {
        Player player = event.getEntity();
        if (player == null || player.level().isClientSide) return;

        ISoulStoneOnExperience.dispatch(event, player);
    }

    @SubscribeEvent
    public static void onPlayerJump(LivingEvent.LivingJumpEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity != null && !entity.level().isClientSide) {
            ISoulStoneOnJump.dispatchJumpTrigger(entity);
        }
    }

    @SubscribeEvent
    public static void onLivingFall(LivingFallEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity != null && !entity.level().isClientSide) {
            ISoulStoneOnFall.dispatchFallTrigger(entity, event.getDistance(), event.getDamageMultiplier());
        }
    }

    @SubscribeEvent
    public static void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
        Player player = event.getEntity();
        if (player == null || player.level().isClientSide) return;

        ItemStack crafted = event.getCrafting();
        Container craftMatrix = event.getInventory();

        Recipe<?> recipe = null;
        if (craftMatrix instanceof CraftingContainer craftingContainer) {
            recipe = player.level().getRecipeManager()
                    .getRecipeFor(RecipeType.CRAFTING, craftingContainer, player.level())
                    .orElse(null);
        }

        if (recipe != null) {
            ISoulStoneOnCraft.dispatchCraftTrigger(player, crafted, recipe);
        }
    }

    @SubscribeEvent
    public static void onChat(ServerChatEvent event) {
        Player player = event.getPlayer();
        if (player == null || player.level().isClientSide) return;

        ISoulStoneOnChat.dispatchChatTrigger(player, event.getMessage());
    }



    @SubscribeEvent
    public static void onPlayerFirstLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();

        CompoundTag persistentData = player.getPersistentData();
        if (!persistentData.contains(Player.PERSISTED_NBT_TAG)) {
            persistentData.put(Player.PERSISTED_NBT_TAG, new CompoundTag());
        }
        CompoundTag forgeData = persistentData.getCompound(Player.PERSISTED_NBT_TAG);

        if (!forgeData.getBoolean("has_received_starter_item")) {

            ResourceLocation itemId = ResourceLocation.fromNamespaceAndPath("better_soul_stone", "player_7_soul_stone");
            Item item = ForgeRegistries.ITEMS.getValue(itemId);

            if (item != null && item != Items.AIR) {
                ItemStack starterItem = new ItemStack(item);

                CompoundTag nbt = starterItem.getOrCreateTag();
                nbt.putString("Source", "NewbieGift");
                nbt.putInt("Level", 1);

                if (!player.getInventory().add(starterItem)) {
                    player.drop(starterItem, false);
                }

                forgeData.putBoolean("has_received_starter_item", true);
                persistentData.put(Player.PERSISTED_NBT_TAG, forgeData);
            } else {
                // 只有在没找到物品时才打印警告，避免报错
                BetterSoulStoneModMain.LOGGER.error("Not Found Item by Id {}", itemId);
            }
        }
    }

}