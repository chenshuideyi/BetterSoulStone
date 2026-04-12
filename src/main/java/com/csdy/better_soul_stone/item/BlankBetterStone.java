package com.csdy.better_soul_stone.item;

import com.csdy.better_soul_stone.BetterSoulStoneModMain;
import com.csdy.better_soul_stone.annotation.SoulStoneItems;
import com.csdy.better_soul_stone.client.rendering.BetterSoulStoneRenderTypes;
import com.csdy.better_soul_stone.client.rendering.BetterSoulStoneWrappedBakedModel;
import com.ibm.icu.impl.Pair;
import com.mojang.logging.LogUtils;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import top.theillusivec4.curios.api.SlotContext;

import java.util.ArrayList;
import java.util.List;

@SoulStoneItems(id = "blank_better_soul_stone")
public class BlankBetterStone extends BaseSoulStone {

    public BlankBetterStone() {
        super(new Properties().stacksTo(64));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public String getSoulStoneType() {
        return "better";
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        return true;
    }

    @Override
    public boolean shouldRenderIconBackground(ItemStack stack) {
        return false;
    }


    @Mod.EventBusSubscriber(modid = BetterSoulStoneModMain.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModelWrapper {
        @SubscribeEvent
        public static void onModelBake(ModelEvent.ModifyBakingResult event) {

            List<Pair<ResourceLocation, BakedModel>> replaced = new ArrayList<>();

            event.getModels().forEach((id, model) -> {
                var item = ForgeRegistries.ITEMS.getValue(ResourceLocation.fromNamespaceAndPath(id.getNamespace(), id.getPath()));
                if (item instanceof BaseSoulStone soulStone) {
                    if ("better".equals(soulStone.getSoulStoneType())) {
                        replaced.add(Pair.of(id, new BetterSoulStoneWrappedBakedModel(model, BetterSoulStoneRenderTypes.getBetter())));
                    }
                }
            });

            replaced.forEach(p -> {
                event.getModels().put(p.first, p.second);
                LogUtils.getLogger().debug("VVVVV Replaced: {}", p.first);
            });
        }
    }
}

