package com.csdy.better_soul_stone.register;

import com.csdy.better_soul_stone.annotation.SoulStoneItems;
import com.csdy.better_soul_stone.item.BaseSoulStone;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.loading.FMLLoader;

import java.util.*;

public class SoulStoneRegistry {

    private static final Map<String, Class<? extends BaseSoulStone>> SOUL_STONE_CLASSES = new HashMap<>();
    private static final Map<String, String> PARENT_MAP = new HashMap<>();
    private static volatile boolean initialized = false;

    public static void registerSoulStone(String id, Class<? extends BaseSoulStone> clazz, String parentId) {
        SOUL_STONE_CLASSES.put(id, clazz);
        if (parentId != null && !parentId.isEmpty()) {
            PARENT_MAP.put(id, parentId);
        }
        initialized = false;
    }

    public static void initialize() {
        if (initialized) return;

        synchronized (SoulStoneRegistry.class) {
            if (initialized) return;

            SOUL_STONE_CLASSES.clear();
            PARENT_MAP.clear();

            String packageName = "com.csdy.better_soul_stone.item";
            List<String> classNames = getClassNames(packageName);

            for (String className : classNames) {
                try {
                    Class<?> clazz = Class.forName(className, false, SoulStoneRegistry.class.getClassLoader());

                    if (clazz.isInterface() || java.lang.reflect.Modifier.isAbstract(clazz.getModifiers())) continue;
                    if (!clazz.isAnnotationPresent(SoulStoneItems.class)) continue;

                    if (BaseSoulStone.class.isAssignableFrom(clazz)) {
                        @SuppressWarnings("unchecked")
                        Class<? extends BaseSoulStone> soulStoneClass = (Class<? extends BaseSoulStone>) clazz;
                        
                        SoulStoneItems annotation = clazz.getAnnotation(SoulStoneItems.class);
                        String registryId = annotation.id();
                        String parentId = annotation.parentId();

                        SOUL_STONE_CLASSES.put(registryId, soulStoneClass);
                        if (parentId != null && !parentId.isEmpty()) {
                            PARENT_MAP.put(registryId, parentId);
                        }
                    }
                } catch (Exception | NoClassDefFoundError ignored) {
                    BetterSoulStoneModMain.LOGGER.error("Failed to load class {} for SoulStoneRegistry", className, ignored);
                }
            }
            initialized = true;
        }
    }

    public static String getParentId(String id) {
        return PARENT_MAP.get(id);
    }

    public static List<String> getParentChain(String id) {
        List<String> chain = new ArrayList<>();
        String current = id;
        Set<String> visited = new HashSet<>();

        while (current != null && !visited.contains(current)) {
            visited.add(current);
            chain.add(current);
            current = getParentId(current);
        }

        return chain;
    }

    public static List<String> getChildIds(String parentId) {
        List<String> children = new ArrayList<>();
        for (Map.Entry<String, String> entry : PARENT_MAP.entrySet()) {
            if (parentId.equals(entry.getValue())) {
                children.add(entry.getKey());
            }
        }
        return children;
    }

    public static Class<? extends BaseSoulStone> getSoulStoneClass(String id) {
        return SOUL_STONE_CLASSES.get(id);
    }

    public static Set<String> getAllSoulStoneIds() {
        return new HashSet<>(SOUL_STONE_CLASSES.keySet());
    }

    private static List<String> getClassNames(String packageName) {
        List<String> classNames = new ArrayList<>();
        String path = packageName.replace('.', '/');

        var modFileInfo = FMLLoader.getLoadingModList().getModFileById("better_soul_stone");
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
            }
        }
        return classNames;
    }
}