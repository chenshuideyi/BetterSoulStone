package com.csdy.better_soul_stone.network.packet;

import com.csdy.better_soul_stone.soul_stone.manager.SoulStoneManager;
import com.csdy.better_soul_stone.soul_stone.soul_stone_capability.ISoulStoneLeftClick;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class LeftClickEmptyPacket {
    public LeftClickEmptyPacket() {}

    public void encode(FriendlyByteBuf buf) {}

    public static LeftClickEmptyPacket decode(FriendlyByteBuf buf) {
        return new LeftClickEmptyPacket();
    }

    public static void handle(LeftClickEmptyPacket msg, Supplier<NetworkEvent.Context> ctxGetter) {
        NetworkEvent.Context ctx = ctxGetter.get();
        ctx.enqueueWork(() -> {
            ServerPlayer player = ctx.getSender();
            if (player != null) {
                SoulStoneManager.forEachStone(player, ISoulStoneLeftClick.class, (logic, stack) ->
                        logic.onLeftClickEmpty(null, player, player.level(), stack));
            }
        });
        ctx.setPacketHandled(true);
    }
}
