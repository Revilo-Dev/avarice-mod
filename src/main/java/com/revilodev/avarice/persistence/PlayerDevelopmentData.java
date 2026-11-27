package com.revilodev.avarice.persistence;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.UUID;
import net.minecraft.world.item.ItemStack;

public record PlayerDevelopmentData(UUID playerId, int version, int revision, ItemStack magnetSlot) {
    public static final int CURRENT_VERSION = 2;
    private static final Codec<UUID> UUID_CODEC = Codec.STRING.xmap(UUID::fromString, UUID::toString);
    public static final Codec<PlayerDevelopmentData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            UUID_CODEC.fieldOf("player_id").forGetter(PlayerDevelopmentData::playerId),
            Codec.INT.optionalFieldOf("version", CURRENT_VERSION).forGetter(PlayerDevelopmentData::version),
            Codec.INT.optionalFieldOf("revision", 0).forGetter(PlayerDevelopmentData::revision),
            ItemStack.OPTIONAL_CODEC.optionalFieldOf("magnet_slot", ItemStack.EMPTY).forGetter(PlayerDevelopmentData::magnetSlot)
    ).apply(instance, PlayerDevelopmentData::new));

    public PlayerDevelopmentData {
        if (playerId == null || version < 0 || version > CURRENT_VERSION || revision < 0 || magnetSlot == null) throw new IllegalArgumentException("Invalid player development data");
        magnetSlot = magnetSlot.copy();
    }

    public PlayerDevelopmentData(UUID playerId, int version, int revision) { this(playerId, version, revision, ItemStack.EMPTY); }

    public static PlayerDevelopmentData defaults(UUID playerId) {
        return new PlayerDevelopmentData(playerId, CURRENT_VERSION, 0, ItemStack.EMPTY);
    }

    public PlayerDevelopmentData nextRevision() {
        return new PlayerDevelopmentData(playerId, CURRENT_VERSION, Math.incrementExact(revision), magnetSlot);
    }

    public PlayerDevelopmentData withMagnetSlot(ItemStack itemStack) { return new PlayerDevelopmentData(playerId, CURRENT_VERSION, revision, itemStack); }
}
