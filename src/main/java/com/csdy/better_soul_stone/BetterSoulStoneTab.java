package com.csdy.better_soul_stone;

import com.csdy.better_soul_stone.register.SoulStoneItemRegister;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.EndRodBlock;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static com.csdy.better_soul_stone.BetterSoulStoneModMain.MODID;

public class BetterSoulStoneTab {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final RegistryObject<CreativeModeTab> TAB = CREATIVE_MODE_TABS.register(MODID, () -> CreativeModeTab.builder()
            .title(Component.translatable("better_soul_stone_tab"))
            .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
            .icon(() -> new ItemStack(Items.END_ROD))
            .displayItems((enabledFeatures, output) -> {
                for(RegistryObject<Item> item : SoulStoneItemRegister.ITEMS.getEntries()){
                    output.accept(item.get());
                }
            })
            .build());


}
