package com.revilodev.avarice.network;

import com.revilodev.avarice.Avarice;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record ItemDefinitionsPayload(String json) implements CustomPacketPayload {
    public static final Type<ItemDefinitionsPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath(Avarice.MODID, "item_definitions"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ItemDefinitionsPayload> STREAM_CODEC = ByteBufCodecs.stringUtf8(131_072).map(ItemDefinitionsPayload::new, ItemDefinitionsPayload::json).cast();

    @Override
    public Type<ItemDefinitionsPayload> type() {
        return TYPE;
    }
}
