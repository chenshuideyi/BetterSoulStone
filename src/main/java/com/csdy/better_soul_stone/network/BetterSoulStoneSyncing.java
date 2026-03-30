package com.csdy.better_soul_stone.network;

import com.csdy.better_soul_stone.BetterSoulStoneModMain;
import com.csdy.better_soul_stone.network.packet.LeftClickBlockPacket;
import com.csdy.better_soul_stone.network.packet.LeftClickEmptyPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;

@Mod.EventBusSubscriber(modid = BetterSoulStoneModMain.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BetterSoulStoneSyncing {

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            ResourceLocation.fromNamespaceAndPath(BetterSoulStoneModMain.MODID, "better_soul_stone"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );


    public static void Init() {
        int packetId = 0;
        CHANNEL.registerMessage(
                packetId++,
                LeftClickBlockPacket.class,
                LeftClickBlockPacket::encode,
                LeftClickBlockPacket::decode,
                LeftClickBlockPacket::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER)
        );
        CHANNEL.registerMessage(
                packetId++,
                LeftClickEmptyPacket.class,
                LeftClickEmptyPacket::encode,
                LeftClickEmptyPacket::decode,
                LeftClickEmptyPacket::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER)
        );
    }
}
