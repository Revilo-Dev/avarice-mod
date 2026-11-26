package com.revilodev.avarice.dungeon;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.revilodev.avarice.world.AvariceDimensions;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import org.junit.jupiter.api.Test;

final class DungeonRunTest {
    @Test
    void followsValidFloorLifecycle() {
        DungeonRun run = newRun();
        run = run.changeStatus(DungeonRunStatus.PREPARING, 2);
        run = run.beginFloorTransition(1, 3).completeFloorTransition(4);
        run = run.beginFloorTransition(2, 5).completeFloorTransition(6);
        run = run.beginFloorTransition(3, 7).completeFloorTransition(8);
        run = run.terminate(DungeonRunStatus.COMPLETED, 9);

        assertEquals(DungeonRunStatus.COMPLETED, run.status());
        assertEquals(3, run.currentFloor());
        assertEquals(DungeonRun.NO_TRANSITION, run.transitionTargetFloor());
    }

    @Test
    void rejectsTerminalAndDuplicateTransitions() {
        DungeonRun active = newRun().changeStatus(DungeonRunStatus.PREPARING, 2)
                .beginFloorTransition(1, 3).completeFloorTransition(4);
        DungeonRun completed = active.terminate(DungeonRunStatus.COMPLETED, 5);

        assertThrows(IllegalStateException.class, () -> completed.beginFloorTransition(2, 6));
        assertThrows(IllegalStateException.class, () -> active.completeFloorTransition(6));
        assertThrows(IllegalArgumentException.class, () -> active.beginFloorTransition(0, 6));
    }

    @Test
    void recoversInterruptedTransitionWithoutAdvancingFloor() {
        DungeonRun transitioning = newRun().changeStatus(DungeonRunStatus.PREPARING, 2).beginFloorTransition(1, 3);
        DungeonRun recovered = transitioning.recoverInterruptedTransition(4);

        assertEquals(DungeonRunStatus.PREPARING, recovered.status());
        assertEquals(0, recovered.currentFloor());
        assertEquals(DungeonRun.NO_TRANSITION, recovered.transitionTargetFloor());
    }

    private static DungeonRun newRun() {
        DungeonLocation location = DungeonLocation.at(AvariceDimensions.ABYSS, new BlockPos(0, 65, 0), 0.0F);
        return DungeonRun.create(UUID.randomUUID(), 0, location, location, 1);
    }
}
