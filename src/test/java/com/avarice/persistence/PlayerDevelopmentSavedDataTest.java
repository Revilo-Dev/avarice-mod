package com.avarice.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import com.mojang.serialization.JsonOps;
import com.revilodev.avarice.persistence.PlayerDevelopmentSavedData;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class PlayerDevelopmentSavedDataTest {
    @Test void roundTripsVersionedPlayerData() {
        PlayerDevelopmentSavedData data = new PlayerDevelopmentSavedData();
        UUID playerId = UUID.randomUUID();
        data.revise(playerId);
        var encoded = PlayerDevelopmentSavedData.CODEC.encodeStart(JsonOps.INSTANCE, data).getOrThrow();
        PlayerDevelopmentSavedData decoded = PlayerDevelopmentSavedData.CODEC.parse(JsonOps.INSTANCE, encoded).getOrThrow();
        assertEquals(playerId, decoded.all().getFirst().playerId());
        assertEquals(1, decoded.all().getFirst().revision());
    }
}
