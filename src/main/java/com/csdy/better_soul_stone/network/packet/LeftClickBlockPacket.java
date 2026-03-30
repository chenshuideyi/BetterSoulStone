package com.csdy.better_soul_stone.network.packet;

import com.csdy.better_soul_stone.soul_stone.manager.SoulStoneManager;
import com.csdy.better_soul_stone.soul_stone.soul_stone_capability.ISoulStoneLeftClick;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class LeftClickBlockPacket {
    private final BlockPos pos;

    public LeftClickBlockPacket(BlockPos pos) {
        this.pos = pos;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(this.pos);
    }

    public static LeftClickBlockPacket decode(FriendlyByteBuf buf) {
        return new LeftClickBlockPacket(buf.readBlockPos());
    }

    public static void handle(LeftClickBlockPacket msg, Supplier<NetworkEvent.Context> ctxGetter) {
        NetworkEvent.Context ctx = ctxGetter.get();
        ctx.enqueueWork(() -> {
            ServerPlayer player = ctx.getSender();
            if (player != null) {
                Level level = player.level();
                BlockPos pos = msg.pos;
                BlockState state = level.getBlockState(pos);

                SoulStoneManager.forEachStone(player, ISoulStoneLeftClick.class, (logic, stack) ->
                        logic.onLeftClickBlock(null, player, level, pos, state, stack));
            }
        });
        ctx.setPacketHandled(true);
    }
}
