package com.csdy.better_soul_stone.util.client;

import com.csdy.better_soul_stone.network.BetterSoulStoneSyncing;
import com.csdy.better_soul_stone.network.packet.DoubleClickPacket;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Options;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenericKeyDetector {
    private static final Map<KeyMapping, Long> LAST_PRESS_MAP = new HashMap<>();
    private static final Map<KeyMapping, Boolean> KEY_PUMP_LOCK = new HashMap<>();
    private static final long THRESHOLD = 300;

    private static final List<KeyMapping> WATCH_LIST = new ArrayList<>();

    public static void init(Options options) {
        WATCH_LIST.add(options.keyUp);
        WATCH_LIST.add(options.keyDown);
        WATCH_LIST.add(options.keyLeft);
        WATCH_LIST.add(options.keyRight);
        WATCH_LIST.add(options.keyJump);
        WATCH_LIST.add(options.keyShift);

        for (KeyMapping key : WATCH_LIST) {
            KEY_PUMP_LOCK.put(key, false);
        }
    }

    public static void tick() {
        for (KeyMapping key : WATCH_LIST) {
            boolean isDown = key.isDown();
            boolean wasLocked = KEY_PUMP_LOCK.getOrDefault(key, false);

            if (isDown) {
                if (!wasLocked) {
                    long now = System.currentTimeMillis();
                    long lastTime = LAST_PRESS_MAP.getOrDefault(key, 0L);

                    if (now - lastTime < THRESHOLD) {
                        BetterSoulStoneSyncing.CHANNEL.sendToServer(
                                new DoubleClickPacket(key.getName())
                        );
                        LAST_PRESS_MAP.put(key, 0L); // 触发后重置
                    } else {
                        LAST_PRESS_MAP.put(key, now);
                    }
                    KEY_PUMP_LOCK.put(key, true);
                }
            } else {
                // 【松开瞬间】解锁
                KEY_PUMP_LOCK.put(key, false);
            }
        }
    }
}
