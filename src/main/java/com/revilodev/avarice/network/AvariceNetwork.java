package com.revilodev.avarice.network;

import com.revilodev.avarice.Avarice;
import com.revilodev.avarice.devtools.items.ItemDefinitionService;
import com.revilodev.avarice.devtools.DevelopmentProfiler;
import com.revilodev.avarice.persistence.PlayerDevelopmentData;
import com.revilodev.avarice.inventory.AvariceInventoryMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

public final class AvariceNetwork {
    public static final String PROTOCOL_VERSION = "1";

    private AvariceNetwork() { }

    public static void register(RegisterPayloadHandlersEvent event) {
        event.registrar(PROTOCOL_VERSION)
                .playToClient(ItemDefinitionsPayload.TYPE, ItemDefinitionsPayload.STREAM_CODEC, AvariceNetwork::handleItemDefinitions)
                .playToClient(PlayerDevelopmentDataPayload.TYPE, PlayerDevelopmentDataPayload.STREAM_CODEC, AvariceNetwork::handlePlayerDevelopmentData)
                .playToServer(ItemDefinitionsRequestPayload.TYPE, ItemDefinitionsRequestPayload.STREAM_CODEC, AvariceNetwork::handleItemDefinitionsRequest)
                .playToServer(OpenAvariceInventoryPayload.TYPE, OpenAvariceInventoryPayload.STREAM_CODEC, AvariceNetwork::handleOpenInventory);
    }

    public static void sendItemDefinitions(ServerPlayer player) {
        PacketDistributor.sendToPlayer(player, new ItemDefinitionsPayload(ItemDefinitionService.serializeCurrent()));
    }

    public static void broadcastItemDefinitions(MinecraftServer server) {
        PacketDistributor.sendToAllPlayers(new ItemDefinitionsPayload(ItemDefinitionService.serializeCurrent()));
    }

    public static void sendPlayerDevelopmentData(ServerPlayer player, PlayerDevelopmentData data) {
        PacketDistributor.sendToPlayer(player, new PlayerDevelopmentDataPayload(data.playerId(), data.revision()));
    }

    private static void handleItemDefinitions(ItemDefinitionsPayload payload, net.neoforged.neoforge.network.handling.IPayloadContext context) {
        context.enqueueWork(() -> {
            long start = System.nanoTime();
            try {
                var candidate = ItemDefinitionService.parse(payload.json());
                ItemDefinitionService.validate(candidate);
                ItemDefinitionService.apply(candidate);
            } catch (RuntimeException exception) {
                Avarice.LOGGER.warn("Rejected synchronized item definitions", exception);
            } finally {
                DevelopmentProfiler.record("network.item_definitions", start);
            }
        });
    }

    private static void handleItemDefinitionsRequest(ItemDefinitionsRequestPayload payload, net.neoforged.neoforge.network.handling.IPayloadContext context) {
        context.enqueueWork(() -> {
            long start = System.nanoTime();
            if (context.player() instanceof ServerPlayer player) sendItemDefinitions(player);
            DevelopmentProfiler.record("network.item_definitions_request", start);
        });
    }

    private static void handlePlayerDevelopmentData(PlayerDevelopmentDataPayload payload, net.neoforged.neoforge.network.handling.IPayloadContext context) {
        context.enqueueWork(() -> {
            long start = System.nanoTime();
            ClientDevelopmentState.apply(payload.playerId(), payload.revision());
            DevelopmentProfiler.record("network.player_development", start);
        });
    }

    private static void handleOpenInventory(OpenAvariceInventoryPayload payload, net.neoforged.neoforge.network.handling.IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) {
                player.openMenu(new net.minecraft.world.SimpleMenuProvider((containerId, inventory, ignored) -> new AvariceInventoryMenu(containerId, inventory), Component.literal("Avarice Inventory")));
            }
        });
    }
}
