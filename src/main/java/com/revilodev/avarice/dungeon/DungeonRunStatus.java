package com.revilodev.avarice.dungeon;

import com.mojang.serialization.Codec;
import java.util.Locale;
import java.util.Set;

public enum DungeonRunStatus {
    CREATED,
    PREPARING,
    ACTIVE,
    TRANSITIONING,
    COMPLETED,
    ABANDONED,
    FAILED;

    public static final Codec<DungeonRunStatus> CODEC = Codec.STRING.xmap(
            value -> valueOf(value.toUpperCase(Locale.ROOT)),
            value -> value.name().toLowerCase(Locale.ROOT)
    );

    public boolean isTerminal() {
        return this == COMPLETED || this == ABANDONED || this == FAILED;
    }

    public boolean canTransitionTo(DungeonRunStatus target) {
        return switch (this) {
            case CREATED -> Set.of(PREPARING, ABANDONED, FAILED).contains(target);
            case PREPARING -> Set.of(TRANSITIONING, ABANDONED, FAILED).contains(target);
            case ACTIVE -> Set.of(TRANSITIONING, COMPLETED, ABANDONED, FAILED).contains(target);
            case TRANSITIONING -> Set.of(ACTIVE, COMPLETED, ABANDONED, FAILED).contains(target);
            case COMPLETED, ABANDONED, FAILED -> false;
        };
    }
}
