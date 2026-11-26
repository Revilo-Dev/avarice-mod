package com.revilodev.avarice.dungeon;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.revilodev.avarice.world.AvariceDimensions;
import java.util.List;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import org.junit.jupiter.api.Test;

final class DungeonAreaAllocatorTest {
    @Test
    void activeRunsReceiveIsolatedSlots() {
        DungeonAreaAllocator allocator = new DungeonAreaAllocator();
        DungeonRun first = activeRun(0);
        int secondSlot = allocator.allocate(List.of(first)).orElseThrow();

        assertEquals(1, secondSlot);
        assertNotEquals(allocator.area(0).floorOrigin(1), allocator.area(secondSlot).floorOrigin(1));
        assertNotEquals(allocator.area(0).floorOrigin(1), allocator.area(0).floorOrigin(2));
    }

    @Test
    void terminalSlotsAreReusedWithoutUnboundedGrowth() {
        DungeonAreaAllocator allocator = new DungeonAreaAllocator();
        DungeonRun completed = activeRun(0).terminate(DungeonRunStatus.COMPLETED, 10);

        assertEquals(0, allocator.allocate(List.of(completed)).orElseThrow());
        assertTrue(allocator.isValidSlot(DungeonAreaAllocator.CAPACITY - 1));
    }

    private static DungeonRun activeRun(int slot) {
        DungeonLocation location = DungeonLocation.at(AvariceDimensions.ABYSS, new BlockPos(0, 65, 0), 0.0F);
        return DungeonRun.create(UUID.randomUUID(), slot, location, location, 1)
                .changeStatus(DungeonRunStatus.PREPARING, 2)
                .beginFloorTransition(1, 3)
                .completeFloorTransition(4);
    }
}
