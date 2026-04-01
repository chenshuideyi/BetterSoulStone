package com.csdy.better_soul_stone.network.packet;

import com.csdy.better_soul_stone.soul_stone.soul_stone_capability.ISoulStoneDoubleClick;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class DoubleClickPacket {
    private final String translationKey;

    public DoubleClickPacket(String translationKey) {
        this.translationKey = translationKey;
    }

    public static void encode(DoubleClickPacket msg, FriendlyByteBuf buffer) {
        buffer.writeUtf(msg.translationKey);
    }

    public static DoubleClickPacket decode(FriendlyByteBuf buffer) {
        return new DoubleClickPacket(buffer.readUtf());
    }

    public static void handle(DoubleClickPacket msg, Supplier<NetworkEvent.Context> ctxGetter) {
        NetworkEvent.Context ctx = ctxGetter.get();
        ctx.enqueueWork(() -> {
            ServerPlayer player = ctx.getSender();
            if (player != null) {
                //传入的是按键的翻译键名，如 "key.forward"
                ISoulStoneDoubleClick.dispatch(player, msg.translationKey);
            }
        });
        ctx.setPacketHandled(true);
    }
}
