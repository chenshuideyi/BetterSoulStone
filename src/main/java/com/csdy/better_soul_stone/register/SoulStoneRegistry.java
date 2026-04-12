package com.csdy.better_soul_stone.register;

import com.csdy.better_soul_stone.BetterSoulStoneModMain;
import com.csdy.better_soul_stone.annotation.SoulStoneItems;
import com.csdy.better_soul_stone.item.BaseSoulStone;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.loading.FMLLoader;

import java.util.*;

public class SoulStoneRegistry {

    private static final Map<String, Class<? extends BaseSoulStone>> SOUL_STONE_CLASSES = new HashMap<>();
    private static final Map<String, List<String>> PARENT_MAP = new HashMap<>();
    private static volatile boolean initialized = false;

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
                        String[] parentIds = annotation.parentIds();

                        SOUL_STONE_CLASSES.put(registryId, soulStoneClass);
                        if (parentIds != null && parentIds.length > 0) {
                            PARENT_MAP.put(registryId, Arrays.asList(parentIds));
                        }

                    }
                } catch (Exception | NoClassDefFoundError ignored) {
                    BetterSoulStoneModMain.LOGGER.error("Failed to load class {} for SoulStoneRegistry", className, ignored);
                }
            }
            initialized = true;
        }
    }

    public static List<String> getParentIds(String id) {
        return PARENT_MAP.getOrDefault(id, Collections.emptyList());
    }

    public static List<String> getParentChain(String id) {
        List<String> chain = new ArrayList<>();
        Queue<String> queue = new LinkedList<>();
        Set<String> visited = new LinkedHashSet<>(); // LinkedHashSet 保持插入顺序并去重

        queue.add(id);
        while (!queue.isEmpty()) {
            String current = queue.poll();
            if (current != null && !visited.contains(current)) {
                visited.add(current);
                List<String> parents = PARENT_MAP.get(current);
                if (parents != null) {
                    queue.addAll(parents);
                }
            }
        }

        chain.addAll(visited);
        return chain;
    }

    public static List<String> getChildIds(String parentId) {
        List<String> children = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : PARENT_MAP.entrySet()) {
            if (entry.getValue().contains(parentId)) {
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