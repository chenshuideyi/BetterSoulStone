package com.csdy.better_soul_stone.font.factory;

import com.csdy.better_soul_stone.font.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.font.FontManager;
import net.minecraft.client.gui.font.FontSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

@OnlyIn(Dist.CLIENT)
public class SoulStoneFontFactory {

    private static final Map<String, Font> FONT_CACHE = new HashMap<>();
    private static final Map<String, BiFunction<Function<ResourceLocation, FontSet>, Boolean, Font>> REGISTRY = new HashMap<>();

    //TODO 后面记得改自动化
    static {
        REGISTRY.put("blank", BlankSoulStoneFont::new);
        REGISTRY.put("nether", NetherSoulStoneFont::new);
        REGISTRY.put("end", EndSoulStoneFont::new);
        REGISTRY.put("pure", PureSoulStoneFont::new);
        REGISTRY.put("theorem", TheoremSoulStoneFont::new);
        REGISTRY.put("absolute", AbsoluteSoulStoneFont::new);
        REGISTRY.put("better", BetterSoulStoneFont::new);
    }

    public static Font getOrCreateFont(String type) {
        return FONT_CACHE.computeIfAbsent(type, t -> {
            FontManager fm = Minecraft.getInstance().fontManager;
            Function<ResourceLocation, FontSet> fontSetGetter = (id) -> fm.fontSets.getOrDefault(id, fm.missingFontSet);

            var constructor = REGISTRY.getOrDefault(t, (getter, legacy) -> null);
            return constructor.apply(fontSetGetter, false);
        });
    }
}
