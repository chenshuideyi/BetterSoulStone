package com.csdy.better_soul_stone.item;

import com.csdy.better_soul_stone.annotation.SoulStoneItems;
import com.csdy.better_soul_stone.font.factory.SoulStoneFontFactory;
import com.csdy.better_soul_stone.register.SoulStoneRegistry;
import com.csdy.better_soul_stone.soul_stone.manager.SoulStoneManager;
import com.csdy.better_soul_stone.soul_stone.soul_stone_capability.client.ISpecialTooltipRendering;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

@SuppressWarnings({"all", "removal"})
public abstract class BaseSoulStone extends Item implements ICurioItem, ISpecialTooltipRendering {

    protected final boolean isSponsor;
    protected final String sponsorName;

    public BaseSoulStone() {
        this(new Item.Properties().stacksTo(1).fireResistant());
    }

    public BaseSoulStone(Item.Properties properties) {
        super(properties);
        SoulStoneItems anno = this.getClass().getAnnotation(SoulStoneItems.class);
        if (anno != null) {
            this.isSponsor = anno.isSponsor();
            this.sponsorName = (isSponsor && !anno.sponsorName().isEmpty()) ? anno.sponsorName() : null;
        } else {
            this.isSponsor = false;
            this.sponsorName = null;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public String getSoulStoneType() {
        return null;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public @Nullable Font getFont(ItemStack stack, FontContext context) {
                if (stack.getItem() instanceof BaseSoulStone soulStone) {
                    String type = soulStone.getSoulStoneType();
                    if (type != null) {
                        return SoulStoneFontFactory.getOrCreateFont(type);
                    }
                }
                return null;
            }
        });
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        return CuriosApi.getCuriosHelper().findFirstCurio(slotContext.entity(), stack.getItem()).isEmpty();
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> modifiers = LinkedHashMultimap.create();

        LivingEntity entity = slotContext.entity();
        if (entity == null) {
            return modifiers;
        }

        List<SoulStoneManager.ActiveLogic> logics = SoulStoneManager.getLogics(entity, ICurioItem.class);

        for (SoulStoneManager.ActiveLogic active : logics) {
            if (active.slotResult().stack() == stack) {
                Object logicItem = active.logicItem();
                if (logicItem instanceof ICurioItem curioLogic) {
                    modifiers.putAll(curioLogic.getAttributeModifiers(slotContext, uuid, stack));
                }
            }
        }
        return modifiers;
    }

    @Override
    public int getFortuneLevel(SlotContext slotContext, net.minecraft.world.level.storage.loot.LootContext lootContext, ItemStack stack) {
        int totalFortune = 0;
        List<SoulStoneManager.ActiveLogic> logics = SoulStoneManager.getLogics(slotContext.entity(), ICurioItem.class);

        for (SoulStoneManager.ActiveLogic active : logics) {
            if (active.slotResult().stack() == stack) {
                if (active.logicItem() instanceof ICurioItem curioLogic) {
                    totalFortune += curioLogic.getFortuneLevel(slotContext, lootContext, stack);
                }
            }
        }
        return totalFortune;
    }

    @Override
    public int getLootingLevel(SlotContext slotContext, DamageSource source, LivingEntity target, int baseLooting, ItemStack stack) {
        int totalLooting = 0;
        List<SoulStoneManager.ActiveLogic> logics = SoulStoneManager.getLogics(slotContext.entity(), ICurioItem.class);

        for (SoulStoneManager.ActiveLogic active : logics) {
            if (active.slotResult().stack() == stack) {
                if (active.logicItem() instanceof ICurioItem curioLogic) {

                    totalLooting += curioLogic.getLootingLevel(slotContext, source, target, 0, stack);
                }
            }
        }
        return baseLooting + totalLooting;
    }

    public String getSoulStoneId() {
        SoulStoneItems anno = this.getClass().getAnnotation(SoulStoneItems.class);
        return anno != null ? anno.id() : "";
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        String flavorKey = "flavor.better_soul_stone." + getSoulStoneId();
        if (I18n.exists(flavorKey)) {
            tooltip.add(Component.translatable(flavorKey).withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
        }
        if (isSponsor && sponsorName != null) {
            tooltip.add(Component.translatable("better_soul_stone.sponsor.format", sponsorName).withStyle(ChatFormatting.GOLD));
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void appendTierDescription(List<Component> tooltip, String id, boolean isInherited) {
        String baseKey = "text.better_soul_stone." + id;

        addLinesIfExist(tooltip, baseKey, isInherited);
        
        int i = 1;
        while (true) {
            String nextKey = baseKey + i;
            if (net.minecraft.client.resources.language.I18n.exists(nextKey)) {
                addLinesIfExist(tooltip, nextKey, isInherited);
                i++;
            } else {
                break;
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void addLinesIfExist(List<Component> tooltip, String key, boolean indent) {
        if (net.minecraft.client.resources.language.I18n.exists(key)) {
            MutableComponent text = Component.translatable(key).withStyle(ChatFormatting.GRAY);
            if (indent) {
                tooltip.add(Component.literal("  ").append(text));
            } else {
                tooltip.add(text);
            }
        }
    }















}