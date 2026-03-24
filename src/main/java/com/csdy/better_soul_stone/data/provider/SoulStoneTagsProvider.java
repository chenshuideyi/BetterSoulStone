package com.csdy.better_soul_stone.data.provider;

import com.csdy.better_soul_stone.BetterSoulStoneModMain;
import com.csdy.better_soul_stone.item.BaseSoulStone;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class SoulStoneTagsProvider extends ItemTagsProvider {

    public SoulStoneTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, CompletableFuture.completedFuture(null), BetterSoulStoneModMain.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {

        TagKey<Item> SOUL_STONE_TAG = TagKey.create(Registries.ITEM,
                ResourceLocation.fromNamespaceAndPath("better_soul_stone", "soul_stone_slot"));

        IntrinsicTagAppender<Item> appender = this.tag(SOUL_STONE_TAG);

        ForgeRegistries.ITEMS.getValues().stream()
                .filter(item -> item instanceof BaseSoulStone)
                .forEach(appender::add);

        TagKey<Item> CURIOS_TAG = TagKey.create(Registries.ITEM,
                ResourceLocation.fromNamespaceAndPath("curios", "soul_stone_slot"));

        this.tag(CURIOS_TAG).addTag(SOUL_STONE_TAG);
    }
}