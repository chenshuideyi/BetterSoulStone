package com.csdy.better_soul_stone.data;

import com.csdy.better_soul_stone.BetterSoulStoneModMain;
import com.csdy.better_soul_stone.data.provider.CuriosSlotProvider;
import com.csdy.better_soul_stone.data.provider.ItemTexturesProvider;
import com.csdy.better_soul_stone.data.provider.SoulStoneRecipeProvider;
import com.csdy.better_soul_stone.data.provider.SoulStoneTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = BetterSoulStoneModMain.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(event.includeServer(),
                new SoulStoneTagsProvider(packOutput, lookupProvider, existingFileHelper));

        generator.addProvider(event.includeServer(),
                new CuriosSlotProvider(packOutput));

        generator.addProvider(event.includeClient(),
                new ItemTexturesProvider(packOutput,existingFileHelper));

        generator.addProvider(event.includeServer(),
                new SoulStoneRecipeProvider(packOutput));

    }
}
