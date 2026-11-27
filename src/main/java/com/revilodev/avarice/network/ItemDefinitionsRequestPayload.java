package com.revilodev.avarice.network;

import com.revilodev.avarice.Avarice;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record ItemDefinitionsRequestPayload() implements CustomPacketPayload {
    public static final Type<ItemDefinitionsRequestPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath(Avarice.MODID, "item_definitions_request"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ItemDefinitionsRequestPayload> STREAM_CODEC = StreamCodec.unit(new ItemDefinitionsRequestPayload());

    @Override
    public Type<ItemDefinitionsRequestPayload> type() {
        return TYPE;
    }
}
