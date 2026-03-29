package com.csdy.better_soul_stone.item;

import com.csdy.better_soul_stone.font.factory.SoulStoneFontFactory;
import com.csdy.better_soul_stone.soul_stone.soul_stone_capability.client.ISpecialTooltipRendering;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
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

    public BaseSoulStone() {
        this(new Item.Properties().stacksTo(1).fireResistant());
    }

    public BaseSoulStone(Item.Properties properties) {
        super(properties);
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
    public boolean isFoil(@NotNull ItemStack stack) {
        return false;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        return HashMultimap.create();
    }


    public int getTier(ItemStack stack) {
        if (stack.hasTag()) {
            return stack.getOrCreateTag().getInt("SoulStoneTier");
        }
        return 1;
    }


    public void setTier(ItemStack stack, int tier) {
        stack.getOrCreateTag().putInt("SoulStoneTier", tier);
    }

    @Override
    public void onCraftedBy(ItemStack stack, Level level, Player player) {
        if (!stack.hasTag()) {
            this.setTier(stack, 1);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        String FlavorId = this.getDescriptionId().replace("item.", "flavor.");
        String descriptionId = this.getDescriptionId().replace("item.", "text.");
        tooltip.add(Component.translatable(FlavorId).withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY));
        tooltip.add(Component.translatable(descriptionId).withStyle(ChatFormatting.GRAY));
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }


}
