package com.csdy.better_soul_stone.data.provider;

import com.csdy.better_soul_stone.BetterSoulStoneModMain;
import com.csdy.better_soul_stone.annotation.SoulStoneItems;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.ModFileScanData;
import org.objectweb.asm.Type;

import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class CuriosEntitySlotProvider implements DataProvider {
    private final PackOutput output;
    private final GatherDataEvent event;

    private static final String SLOT_ID = "soul_stone_slot";

    public CuriosEntitySlotProvider(PackOutput output, GatherDataEvent event) {
        this.output = output;
        this.event = event;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        ModFileScanData scanData = ModList.get()
                .getModFileById(BetterSoulStoneModMain.MODID)
                .getFile()
                .getScanResult();

        String annotationDescriptor = Type.getDescriptor(SoulStoneItems.class);
        Set<String> targetEntities = new TreeSet<>();

        scanData.getAnnotations().stream()
                .filter(anno -> annotationDescriptor.equals(anno.annotationType().getDescriptor()))
                .forEach(anno -> {
                    Map<String, Object> data = anno.annotationData();

                    Object droppedByObj = data.get("droppedBy");

                    if (droppedByObj instanceof String entityId) {
                        if (!entityId.isEmpty() && !"none".equals(entityId)) {
                            targetEntities.add(entityId);
                        }
                    }
                    // 兜底处理：如果某些情况下它被识别成了 List（虽然 droppedBy 定义是 String）
                    else if (droppedByObj instanceof List<?> list && !list.isEmpty()) {
                        String entityId = list.get(0).toString();
                        if (!entityId.isEmpty() && !"none".equals(entityId)) {
                            targetEntities.add(entityId);
                        }
                    }
                });

        // 3. 构建 JSON 结构
        if (targetEntities.isEmpty() && !BetterSoulStoneModMain.MODID.equals("better_soul_stone")) {
            return CompletableFuture.completedFuture(null);
        }

        JsonObject root = new JsonObject();
        JsonArray entityArray = new JsonArray();

        targetEntities.add("minecraft:player");
        targetEntities.forEach(entityArray::add);

        root.add("entities", entityArray);

        JsonArray slotsArray = new JsonArray();
        slotsArray.add(SLOT_ID);
        root.add("slots", slotsArray);

        //data/curios/curios/entities/soul_stone_slot.json
        Path path = this.output.getOutputFolder()
                .resolve("data/curios/curios/entities/" + SLOT_ID + ".json");

        return DataProvider.saveStable(cache, root, path);
    }

    @Override
    public String getName() {
        return "Curios Entity Slot Provider: " + SLOT_ID;
    }
}