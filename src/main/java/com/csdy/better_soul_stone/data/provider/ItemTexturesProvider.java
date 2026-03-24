package com.csdy.better_soul_stone.data.provider;

import com.csdy.better_soul_stone.BetterSoulStoneModMain;
import net.minecraft.data.PackOutput;
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
                    String name = entry.getKey().location().getPath();

                    // 生成{"parent": "item/generated", "textures": {"layer0": "modid:item/name"}}
                    // 对应src/main/resources/assets/better_soul_stone/textures/item/name.png
                    simpleItem(item);
                });
    }

    private void simpleItem(Item item) {
        String name = ForgeRegistries.ITEMS.getKey(item).getPath();

        withExistingParent(name, ResourceLocation.fromNamespaceAndPath("minecraft", "item/generated"))
                .texture("layer0", ResourceLocation.fromNamespaceAndPath(BetterSoulStoneModMain.MODID, "item/" + name))
        ;
    }
}
