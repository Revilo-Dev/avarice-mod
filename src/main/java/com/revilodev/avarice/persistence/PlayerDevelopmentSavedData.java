package com.revilodev.avarice.persistence;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.revilodev.avarice.Avarice;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import net.minecraft.world.item.ItemStack;

public final class PlayerDevelopmentSavedData extends SavedData {
    public static final Codec<PlayerDevelopmentSavedData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            PlayerDevelopmentData.CODEC.listOf().optionalFieldOf("players", List.of()).forGetter(PlayerDevelopmentSavedData::all)
    ).apply(instance, PlayerDevelopmentSavedData::new));
    public static final SavedDataType<PlayerDevelopmentSavedData> TYPE = new SavedDataType<>(
            Identifier.fromNamespaceAndPath(Avarice.MODID, "player_development"), PlayerDevelopmentSavedData::new, CODEC
    );

    private final Map<UUID, PlayerDevelopmentData> players = new LinkedHashMap<>();

    public PlayerDevelopmentSavedData() { }

    private PlayerDevelopmentSavedData(List<PlayerDevelopmentData> values) {
        for (PlayerDevelopmentData value : values) {
            if (players.putIfAbsent(value.playerId(), value) != null) throw new IllegalArgumentException("Duplicate player data: " + value.playerId());
        }
    }

    public static PlayerDevelopmentSavedData get(MinecraftServer server) {
        return server.getDataStorage().computeIfAbsent(TYPE);
    }

    public List<PlayerDevelopmentData> all() {
        return List.copyOf(players.values());
    }

    public PlayerDevelopmentData getOrCreate(UUID playerId) {
        PlayerDevelopmentData current = players.get(playerId);
        if (current != null) return current;
        PlayerDevelopmentData created = PlayerDevelopmentData.defaults(playerId);
        players.put(playerId, created);
        setDirty();
        return created;
    }

    public PlayerDevelopmentData revise(UUID playerId) {
        PlayerDevelopmentData next = getOrCreate(playerId).nextRevision();
        players.put(playerId, next);
        setDirty();
        return next;
    }

    public ItemStack magnetSlot(UUID playerId) {
        return getOrCreate(playerId).magnetSlot().copy();
    }

    public void setMagnetSlot(UUID playerId, ItemStack itemStack) {
        players.put(playerId, getOrCreate(playerId).withMagnetSlot(itemStack));
        setDirty();
    }
}
