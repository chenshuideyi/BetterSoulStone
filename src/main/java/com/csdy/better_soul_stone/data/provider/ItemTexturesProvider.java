package com.csdy.better_soul_stone.data.provider;

import com.csdy.better_soul_stone.BetterSoulStoneModMain;
import com.csdy.better_soul_stone.annotation.SoulStoneItems;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;


import com.csdy.better_soul_stone.item.BaseSoulStone;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemTexturesProvider extends ItemModelProvider {

    public ItemTexturesProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, BetterSoulStoneModMain.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        ForgeRegistries.ITEMS.getEntries().stream()
                .filter(entry -> entry.getValue() instanceof BaseSoulStone)
                .forEach(entry -> {
                    Item item = entry.getValue();

                    SoulStoneItems annotation = item.getClass().getAnnotation(SoulStoneItems.class);

                    if (annotation != null && !Float.isNaN(annotation.scale())) {
                        bigItem(item, annotation.scale());
                    }

                    else {
                        simpleItem(item);
                    }

                });
    }

    private void simpleItem(Item item) {
        String name = ForgeRegistries.ITEMS.getKey(item).getPath();
        withExistingParent(name, mcLoc("item/generated"))
                .texture("layer0", modLoc("item/" + name));
    }

    private void bigItem(Item item, float scale) {
        String name = ForgeRegistries.ITEMS.getKey(item).getPath();

        ItemModelBuilder builder = withExistingParent(name, mcLoc("item/generated"))
                .texture("layer0", modLoc("item/" + name));

        // 这对应 JSON 中的 "display": { "gui": { "scale": [s, s, s] } }
        builder.transforms()
                .transform(ItemDisplayContext.GUI)
                .scale(scale, scale, scale)
                .translation(0, 0, 0)
                .rotation(0, 0, 0)
                .end();

        builder.transforms()
                .transform(ItemDisplayContext.GUI).scale(scale).end()
                .transform(ItemDisplayContext.GROUND).scale(scale).end();

    }
}
