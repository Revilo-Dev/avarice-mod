package com.revilodev.avarice.network;

import com.revilodev.avarice.Avarice;
import java.util.UUID;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record PlayerDataPayload(UUID playerId, int revision, long coins) implements CustomPacketPayload {
    public static final Type<PlayerDataPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath(Avarice.MODID, "player_development_data"));
    public static final StreamCodec<RegistryFriendlyByteBuf, PlayerDataPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.stringUtf8(36), payload -> payload.playerId().toString(),
            ByteBufCodecs.VAR_INT, PlayerDataPayload::revision,
            ByteBufCodecs.VAR_LONG, PlayerDataPayload::coins,
            (playerId, revision, coins) -> new PlayerDataPayload(UUID.fromString(playerId), revision, coins)
    ).cast();

    public PlayerDataPayload {
        if (playerId == null || revision < 0 || coins < 0) throw new IllegalArgumentException("Invalid player development payload");
    }

    @Override
    public Type<PlayerDataPayload> type() {
        return TYPE;
    }
}
