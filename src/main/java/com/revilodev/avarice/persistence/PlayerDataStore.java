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

public final class PlayerDataStore extends SavedData {
    public static final Codec<PlayerDataStore> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            PlayerData.CODEC.listOf().optionalFieldOf("players", List.of()).forGetter(PlayerDataStore::all)
    ).apply(instance, PlayerDataStore::new));
    public static final SavedDataType<PlayerDataStore> TYPE = new SavedDataType<>(
            Identifier.fromNamespaceAndPath(Avarice.MODID, "player_development"), PlayerDataStore::new, CODEC
    );

    private final Map<UUID, PlayerData> players = new LinkedHashMap<>();

    public PlayerDataStore() { }

    private PlayerDataStore(List<PlayerData> values) {
        for (PlayerData value : values) {
            if (players.putIfAbsent(value.playerId(), value) != null) throw new IllegalArgumentException("Duplicate player data: " + value.playerId());
        }
    }

    public static PlayerDataStore get(MinecraftServer server) {
        return server.getDataStorage().computeIfAbsent(TYPE);
    }

    public List<PlayerData> all() {
        return List.copyOf(players.values());
    }

    public PlayerData getOrCreate(UUID playerId) {
        PlayerData current = players.get(playerId);
        if (current != null) return current;
        PlayerData created = PlayerData.defaults(playerId);
        players.put(playerId, created);
        setDirty();
        return created;
    }

    public PlayerData revise(UUID playerId) {
        PlayerData next = getOrCreate(playerId).nextRevision();
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

    public long coins(UUID playerId) { return getOrCreate(playerId).coins(); }
    public void setCoins(UUID playerId, long amount) { players.put(playerId, getOrCreate(playerId).withCoins(amount)); setDirty(); }
}
