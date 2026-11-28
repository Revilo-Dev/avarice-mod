package com.revilodev.avarice.network;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public final class ClientDevelopmentState {
    private static final AtomicReference<Snapshot> ACTIVE = new AtomicReference<>();

    private ClientDevelopmentState() { }

    public static void apply(UUID playerId, int revision, long coins) {
        ACTIVE.set(new Snapshot(playerId, revision, coins));
    }

    public static Optional<Snapshot> current() {
        return Optional.ofNullable(ACTIVE.get());
    }

    public record Snapshot(UUID playerId, int revision, long coins) { }
}
