package com.avarice.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import com.mojang.serialization.JsonOps;
import com.revilodev.avarice.persistence.PlayerDataStore;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class PlayerDataStoreTest {
    @Test void roundTripsVersionedPlayerData() {
        PlayerDataStore data = new PlayerDataStore();
        UUID playerId = UUID.randomUUID();
        data.revise(playerId);
        var encoded = PlayerDataStore.CODEC.encodeStart(JsonOps.INSTANCE, data).getOrThrow();
        PlayerDataStore decoded = PlayerDataStore.CODEC.parse(JsonOps.INSTANCE, encoded).getOrThrow();
        assertEquals(playerId, decoded.all().getFirst().playerId());
        assertEquals(1, decoded.all().getFirst().revision());
    }
}
