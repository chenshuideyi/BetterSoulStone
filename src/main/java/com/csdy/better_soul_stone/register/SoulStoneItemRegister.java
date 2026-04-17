package com.csdy.better_soul_stone.register;

import com.csdy.better_soul_stone.BetterSoulStoneModMain;
import com.csdy.better_soul_stone.annotation.SoulStoneItems;
import com.csdy.better_soul_stone.item.BaseSoulStone;
import com.csdy.better_soul_stone.item.minecraft.DisabledSoulStone;
import com.csdy.better_soul_stone.soul_stone.manager.SoulStoneDropManager;
import com.csdy.better_soul_stone.soul_stone.manager.SoulStoneManager;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SoulStoneItemRegister {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, BetterSoulStoneModMain.MODID);

    public static void autoRegisterSoulStones() {
        String packageName = "com.csdy.better_soul_stone.item";
        List<String> classNames = getClassNames(packageName);

        for (String className : classNames) {
            try {
                /* 使用 initialize = false 加载类。
                // 这样 JVM 只会加载类结构，而不会去验证它实现的接口 @ISpecialTooltipRendering
                // 从而避开了服务端缺失客户端类的问题
                // MC服务端真是一坨狗屎
                */
                Class<?> clazz = Class.forName(className, false, SoulStoneItemRegister.class.getClassLoader());

                if (clazz.isInterface() || java.lang.reflect.Modifier.isAbstract(clazz.getModifiers())) continue;
                if (!clazz.isAnnotationPresent(SoulStoneItems.class)) continue;

                SoulStoneItems annotation = clazz.getAnnotation(SoulStoneItems.class);
                String[] requiredMods = annotation.requiredMod();

                boolean allModsLoaded = requiredMods.length == 0 ||
                        Arrays.stream(requiredMods).allMatch(ModList.get()::isLoaded);

                String registryId = annotation.id();

                RegistryObject<Item> registeredItem = ITEMS.register(registryId, () -> {
                    if (!allModsLoaded) {
                        return new DisabledSoulStone(requiredMods);
                    }
                    try {
                        return (BaseSoulStone) clazz.getDeclaredConstructor().newInstance();
                    } catch (Exception e) {
                        return new DisabledSoulStone(requiredMods);
                    }
                });

                if (annotation.droppedBy().length > 0 && annotation.chance() > 0) {
                    for (String mobId : annotation.droppedBy()) {
                        SoulStoneDropManager.addEntry(mobId, registeredItem, annotation.chance());
                    }
                }

                SoulStoneManager.markInterfaceAsActive(clazz);

            } catch (ClassNotFoundException | NoClassDefFoundError e) {
                BetterSoulStoneModMain.LOGGER.warn("跳过类加载（可能是客户端专属类）: " + className);
            } catch (Exception e) {
                BetterSoulStoneModMain.LOGGER.error("扫描类时发生未知错误: " + className, e);
            }
        }
    }

    private static List<String> getClassNames(String packageName) {
        List<String> classNames = new ArrayList<>();
        String path = packageName.replace('.', '/');

        net.minecraftforge.fml.loading.moddiscovery.ModFileInfo modFileInfo =
                net.minecraftforge.fml.loading.FMLLoader.getLoadingModList().getModFileById(BetterSoulStoneModMain.MODID);

        if (modFileInfo == null) return classNames;

        java.nio.file.Path rootPath = modFileInfo.getFile().findResource(path);

        if (java.nio.file.Files.exists(rootPath)) {
            try (java.util.stream.Stream<java.nio.file.Path> walk = java.nio.file.Files.walk(rootPath)) {
                walk.filter(p -> p.toString().endsWith(".class")).forEach(p -> {
                    String relativePath = rootPath.relativize(p).toString()
                            .replace(java.io.File.separatorChar, '.')
                            .replace('/', '.');

                    String className = packageName + "." + relativePath.substring(0, relativePath.length() - 6);
                    classNames.add(className);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return classNames;
    }
}