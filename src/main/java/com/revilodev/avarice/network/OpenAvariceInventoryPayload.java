package com.revilodev.avarice.network;

import com.revilodev.avarice.Avarice;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record OpenAvariceInventoryPayload() implements CustomPacketPayload {
    public static final Type<OpenAvariceInventoryPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath(Avarice.MODID, "open_inventory"));
    public static final StreamCodec<RegistryFriendlyByteBuf, OpenAvariceInventoryPayload> STREAM_CODEC = StreamCodec.unit(new OpenAvariceInventoryPayload());

    @Override
    public Type<OpenAvariceInventoryPayload> type() {
        return TYPE;
    }
}
