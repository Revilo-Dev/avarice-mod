package com.revilodev.avarice.persistence;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.UUID;
import net.minecraft.world.item.ItemStack;

public record PlayerData(UUID playerId, int version, int revision, ItemStack magnetSlot, long coins) {
    public static final int CURRENT_VERSION = 3;
    private static final Codec<UUID> UUID_CODEC = Codec.STRING.xmap(UUID::fromString, UUID::toString);
    public static final Codec<PlayerData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            UUID_CODEC.fieldOf("player_id").forGetter(PlayerData::playerId),
            Codec.INT.optionalFieldOf("version", CURRENT_VERSION).forGetter(PlayerData::version),
            Codec.INT.optionalFieldOf("revision", 0).forGetter(PlayerData::revision),
            ItemStack.OPTIONAL_CODEC.optionalFieldOf("magnet_slot", ItemStack.EMPTY).forGetter(PlayerData::magnetSlot),
            Codec.LONG.optionalFieldOf("coins", 0L).forGetter(PlayerData::coins)
    ).apply(instance, PlayerData::new));

    public PlayerData {
        if (playerId == null || version < 0 || version > CURRENT_VERSION || revision < 0 || magnetSlot == null || coins < 0) throw new IllegalArgumentException("Invalid player development data");
        magnetSlot = magnetSlot.copy();
    }

    public PlayerData(UUID playerId, int version, int revision) { this(playerId, version, revision, ItemStack.EMPTY, 0); }
    public PlayerData(UUID playerId, int version, int revision, ItemStack magnetSlot) { this(playerId, version, revision, magnetSlot, 0); }

    public static PlayerData defaults(UUID playerId) {
        return new PlayerData(playerId, CURRENT_VERSION, 0, ItemStack.EMPTY, 0);
    }

    public PlayerData nextRevision() {
        return new PlayerData(playerId, CURRENT_VERSION, Math.incrementExact(revision), magnetSlot, coins);
    }

    public PlayerData withMagnetSlot(ItemStack itemStack) { return new PlayerData(playerId, CURRENT_VERSION, revision, itemStack, coins); }
    public PlayerData withCoins(long amount) { return new PlayerData(playerId, CURRENT_VERSION, revision, magnetSlot, amount); }
}
