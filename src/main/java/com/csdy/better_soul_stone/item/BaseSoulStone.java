package com.csdy.better_soul_stone.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.UUID;

import static net.minecraftforge.fml.loading.FMLConfig.getConfigValue;

@SuppressWarnings({"all", "removal"})
public abstract class BaseSoulStone extends Item implements ICurioItem {

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
}
