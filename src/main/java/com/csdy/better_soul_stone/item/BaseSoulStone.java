package com.csdy.better_soul_stone.item;

import com.csdy.better_soul_stone.soul_stone.soul_stone_capability.client.ISpecialTooltipRendering;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;
import java.util.UUID;

@SuppressWarnings({"all", "removal"})
public abstract class BaseSoulStone extends Item implements ICurioItem, ISpecialTooltipRendering {

    public BaseSoulStone() {
        this(new Item.Properties().stacksTo(1).fireResistant());
    }

    public BaseSoulStone(Item.Properties properties) {
        super(properties);
    }


    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        return CuriosApi.getCuriosHelper().findFirstCurio(slotContext.entity(), stack.getItem()).isEmpty();
    }

    @Override
    public boolean isFoil(@NotNull ItemStack stack) {
        return true;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        return HashMultimap.create();
    }


    public int getTier(ItemStack stack) {
        if (!stack.hasTag()) return 1;
        return stack.getTag().getInt("SoulStoneTier");
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
