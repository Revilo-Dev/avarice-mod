package com.revilodev.avarice.dungeon;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import com.revilodev.avarice.world.AvariceDimensions;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import org.junit.jupiter.api.Test;

final class DungeonRunSavedDataTest {
    @Test
    void codecPreservesRunIdentityAllocationAndParticipants() {
        UUID playerId = UUID.randomUUID();
        DungeonLocation location = DungeonLocation.at(AvariceDimensions.ABYSS, new BlockPos(0, 65, 0), 180.0F);
        DungeonRun run = DungeonRun.create(playerId, 7, location, location, 25)
                .changeStatus(DungeonRunStatus.PREPARING, 26)
                .beginFloorTransition(1, 27)
                .completeFloorTransition(28);
        DungeonRunSavedData original = new DungeonRunSavedData();
        original.put(run);
        original.markAbyssInitialized();
        original.markAbyssEnvironmentSanitized();

        JsonElement encoded = DungeonRunSavedData.CODEC.encodeStart(JsonOps.INSTANCE, original).getOrThrow();
        DungeonRunSavedData decoded = DungeonRunSavedData.CODEC.parse(JsonOps.INSTANCE, encoded).getOrThrow();
        DungeonRun restored = decoded.get(run.runId()).orElseThrow();

        assertEquals(run.runId(), restored.runId());
        assertEquals(run.instanceId(), restored.instanceId());
        assertEquals(7, restored.allocationSlot());
        assertTrue(restored.participants().contains(playerId));
        assertTrue(decoded.abyssInitialized());
        assertTrue(decoded.abyssEnvironmentSanitized());
    }
}
