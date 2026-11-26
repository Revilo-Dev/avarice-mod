package com.revilodev.avarice.dungeon;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Set;
import java.util.UUID;
import net.minecraft.core.UUIDUtil;

public record DungeonRun(
        UUID runId,
        DungeonRunStatus status,
        int currentFloor,
        long startedTick,
        long lastUpdatedTick,
        Set<UUID> participants,
        UUID instanceId,
        int allocationSlot,
        DungeonLocation startLocation,
        DungeonLocation returnLocation,
        int transitionTargetFloor
) {
    public static final int NO_TRANSITION = -1;
    public static final Codec<DungeonRun> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            UUIDUtil.STRING_CODEC.fieldOf("run_id").forGetter(DungeonRun::runId),
            DungeonRunStatus.CODEC.fieldOf("status").forGetter(DungeonRun::status),
            Codec.INT.fieldOf("current_floor").forGetter(DungeonRun::currentFloor),
            Codec.LONG.fieldOf("started_tick").forGetter(DungeonRun::startedTick),
            Codec.LONG.fieldOf("last_updated_tick").forGetter(DungeonRun::lastUpdatedTick),
            UUIDUtil.CODEC_SET.fieldOf("participants").forGetter(DungeonRun::participants),
            UUIDUtil.STRING_CODEC.fieldOf("instance_id").forGetter(DungeonRun::instanceId),
            Codec.INT.fieldOf("allocation_slot").forGetter(DungeonRun::allocationSlot),
            DungeonLocation.CODEC.fieldOf("start_location").forGetter(DungeonRun::startLocation),
            DungeonLocation.CODEC.fieldOf("return_location").forGetter(DungeonRun::returnLocation),
            Codec.INT.optionalFieldOf("transition_target_floor", NO_TRANSITION).forGetter(DungeonRun::transitionTargetFloor)
    ).apply(instance, DungeonRun::new));

    public DungeonRun {
        participants = Set.copyOf(participants);
        if (currentFloor < 0) throw new IllegalArgumentException("current floor cannot be negative");
        if (allocationSlot < 0) throw new IllegalArgumentException("allocation slot cannot be negative");
        if (participants.isEmpty()) throw new IllegalArgumentException("a run requires a participant");
    }

    public static DungeonRun create(UUID playerId, int allocationSlot, DungeonLocation start, DungeonLocation returnLocation, long tick) {
        return new DungeonRun(
                UUID.randomUUID(), DungeonRunStatus.CREATED, 0, tick, tick, Set.of(playerId), UUID.randomUUID(), allocationSlot,
                start, returnLocation, NO_TRANSITION
        );
    }

    public DungeonRun changeStatus(DungeonRunStatus target, long tick) {
        if (!status.canTransitionTo(target)) throw new IllegalStateException("invalid run transition " + status + " to " + target);
        return copy(target, currentFloor, tick, transitionTargetFloor);
    }

    public DungeonRun beginFloorTransition(int targetFloor, long tick) {
        if (targetFloor < 1) throw new IllegalArgumentException("target floor must be positive");
        DungeonRun changed = changeStatus(DungeonRunStatus.TRANSITIONING, tick);
        return changed.copy(changed.status, changed.currentFloor, tick, targetFloor);
    }

    public DungeonRun completeFloorTransition(long tick) {
        if (status != DungeonRunStatus.TRANSITIONING || transitionTargetFloor < 1) {
            throw new IllegalStateException("run is not transitioning to a floor");
        }
        return copy(DungeonRunStatus.ACTIVE, transitionTargetFloor, tick, NO_TRANSITION);
    }

    public DungeonRun recoverInterruptedTransition(long tick) {
        if (status != DungeonRunStatus.TRANSITIONING) throw new IllegalStateException("run is not transitioning");
        DungeonRunStatus recovered = currentFloor > 0 ? DungeonRunStatus.ACTIVE : DungeonRunStatus.PREPARING;
        return copy(recovered, currentFloor, tick, NO_TRANSITION);
    }

    public DungeonRun terminate(DungeonRunStatus target, long tick) {
        if (!target.isTerminal()) throw new IllegalArgumentException("target status is not terminal");
        if (!status.canTransitionTo(target)) throw new IllegalStateException("invalid run transition " + status + " to " + target);
        return copy(target, currentFloor, tick, NO_TRANSITION);
    }

    private DungeonRun copy(DungeonRunStatus newStatus, int floor, long tick, int targetFloor) {
        return new DungeonRun(
                runId, newStatus, floor, startedTick, tick, participants, instanceId, allocationSlot,
                startLocation, returnLocation, targetFloor
        );
    }
}
