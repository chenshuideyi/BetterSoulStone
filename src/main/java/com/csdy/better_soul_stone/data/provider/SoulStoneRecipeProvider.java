package com.csdy.better_soul_stone.data.provider;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.Item;

import java.util.function.Consumer;


import com.csdy.better_soul_stone.annotation.SoulStoneItems;
import net.minecraftforge.registries.ForgeRegistries;

public class SoulStoneRecipeProvider extends RecipeProvider {

    public SoulStoneRecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {

        ForgeRegistries.ITEMS.getEntries().forEach(entry -> {
            Item item = entry.getValue();
            Class<?> clazz = item.getClass();

            // 匹配你的自动注册逻辑：检查是否有 SoulStoneItems 注解
            if (clazz.isAnnotationPresent(SoulStoneItems.class)) {
                SoulStoneItems annotation = clazz.getAnnotation(SoulStoneItems.class);

                // 生成“1变2”的复制配方
                ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, item, 2)
                        .requires(item)
                        .unlockedBy("has_" + annotation.id(), has(item))
                        .save(consumer, "better_soul_stone:soul_stone_doubling_" + annotation.id());
            }
        });
    }
}
