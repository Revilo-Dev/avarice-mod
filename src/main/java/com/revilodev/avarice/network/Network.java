package com.revilodev.avarice.network;

import com.revilodev.avarice.Avarice;
import com.revilodev.avarice.devtools.items.ItemDefs;
import com.revilodev.avarice.devtools.Profiler;
import com.revilodev.avarice.persistence.PlayerData;
import com.revilodev.avarice.inventory.AvariceInventoryMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

public final class Network {
    public static final String PROTOCOL_VERSION = "2";

    private Network() { }

    public static void register(RegisterPayloadHandlersEvent event) {
        event.registrar(PROTOCOL_VERSION)
                .playToClient(ItemDefinitionsPayload.TYPE, ItemDefinitionsPayload.STREAM_CODEC, Network::handleItemDefinitions)
                .playToClient(PlayerDataPayload.TYPE, PlayerDataPayload.STREAM_CODEC, Network::handlePlayerData)
                .playToServer(ItemDefinitionsRequestPayload.TYPE, ItemDefinitionsRequestPayload.STREAM_CODEC, Network::handleItemDefinitionsRequest)
                .playToServer(OpenAvariceInventoryPayload.TYPE, OpenAvariceInventoryPayload.STREAM_CODEC, Network::handleOpenInventory);
    }

    public static void sendItemDefinitions(ServerPlayer player) {
        PacketDistributor.sendToPlayer(player, new ItemDefinitionsPayload(ItemDefs.serializeCurrent()));
    }

    public static void broadcastItemDefinitions(MinecraftServer server) {
        PacketDistributor.sendToAllPlayers(new ItemDefinitionsPayload(ItemDefs.serializeCurrent()));
    }

    public static void sendPlayerData(ServerPlayer player, PlayerData data) {
        PacketDistributor.sendToPlayer(player, new PlayerDataPayload(data.playerId(), data.revision(), data.coins()));
    }

    private static void handleItemDefinitions(ItemDefinitionsPayload payload, net.neoforged.neoforge.network.handling.IPayloadContext context) {
        context.enqueueWork(() -> {
            long start = System.nanoTime();
            try {
                var candidate = ItemDefs.parse(payload.json());
                ItemDefs.validate(candidate);
                ItemDefs.apply(candidate);
            } catch (RuntimeException exception) {
                Avarice.LOGGER.warn("Rejected synchronized item definitions", exception);
            } finally {
                Profiler.record("network.item_definitions", start);
            }
        });
    }

    private static void handleItemDefinitionsRequest(ItemDefinitionsRequestPayload payload, net.neoforged.neoforge.network.handling.IPayloadContext context) {
        context.enqueueWork(() -> {
            long start = System.nanoTime();
            if (context.player() instanceof ServerPlayer player) sendItemDefinitions(player);
            Profiler.record("network.item_definitions_request", start);
        });
    }

    private static void handlePlayerData(PlayerDataPayload payload, net.neoforged.neoforge.network.handling.IPayloadContext context) {
        context.enqueueWork(() -> {
            long start = System.nanoTime();
            ClientDevelopmentState.apply(payload.playerId(), payload.revision(), payload.coins());
            Profiler.record("network.player_development", start);
        });
    }

    private static void handleOpenInventory(OpenAvariceInventoryPayload payload, net.neoforged.neoforge.network.handling.IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) {
                sendPlayerData(player, com.revilodev.avarice.persistence.PlayerDataStore.get(player.level().getServer()).getOrCreate(player.getUUID()));
                player.openMenu(new net.minecraft.world.SimpleMenuProvider((containerId, inventory, ignored) -> new AvariceInventoryMenu(containerId, inventory), Component.literal("Avarice Inventory")));
            }
        });
    }
}
