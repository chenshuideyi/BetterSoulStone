package com.csdy.better_soul_stone.data.provider;

import com.csdy.better_soul_stone.BetterSoulStoneModMain;
import com.csdy.better_soul_stone.item.BaseSoulStone;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public class CuriosSlotProvider implements DataProvider {
    private final PackOutput output;

    public CuriosSlotProvider(PackOutput output) {
        this.output = output;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        JsonObject root = new JsonObject();
        JsonArray values = new JsonArray();

        ForgeRegistries.ITEMS.getEntries().stream()
                .filter(entry -> entry.getValue() instanceof BaseSoulStone)
                .forEach(entry -> {
                    JsonObject itemObj = new JsonObject();
                    itemObj.addProperty("id", entry.getKey().location().toString());
                    itemObj.addProperty("required", false);
                    values.add(itemObj);
                });

        root.add("values", values);

        //data/curios/tags/items/soul_stone_slot.json
        Path path = this.output.getOutputFolder().resolve("data/curios/tags/items/soul_stone_slot.json");
        return DataProvider.saveStable(cache, root, path);
    }

    @Override
    public String getName() {
        return "Curios Slot Definition: soul_stone_slot";
    }
}
