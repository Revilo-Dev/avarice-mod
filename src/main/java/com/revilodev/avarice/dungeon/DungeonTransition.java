package com.revilodev.avarice.dungeon;

import com.revilodev.avarice.Avarice;
import com.revilodev.avarice.world.AvariceDimensions;
import com.revilodev.avarice.world.AvariceTeleporter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public final class DungeonTransition {
    private final MinecraftServer server;
    private final DungeonRunSavedData data;
    private final DungeonAreaAllocator allocator;
    private final FloorBuilder floors;

    public DungeonTransition(
            MinecraftServer server,
            DungeonRunSavedData data,
            DungeonAreaAllocator allocator,
            FloorBuilder floors
    ) {
        this.server = server;
        this.data = data;
        this.allocator = allocator;
        this.floors = floors;
    }

    public DungeonOperationResult transition(UUID runId, int targetFloor) {
        DungeonRun run = data.get(runId).orElse(null);
        if (run == null) return DungeonOperationResult.failure("message.avarice.run.missing", null);
        if (!validTarget(run, targetFloor)) return reject(run, "message.avarice.run.invalid_floor");
        DungeonRun transitioning;
        try {
            transitioning = run.beginFloorTransition(targetFloor, tick());
        } catch (IllegalStateException exception) {
            return reject(run, "message.avarice.run.invalid_transition");
        }
        data.put(transitioning);
        return finishTransition(transitioning);
    }

    public DungeonOperationResult resume(DungeonRun run) {
        if (run.status() != DungeonRunStatus.TRANSITIONING || !floors.supports(run.transitionTargetFloor())) {
            return reject(run, "message.avarice.run.recovery_failed");
        }
        return finishTransition(run);
    }

    public boolean restorePlayer(ServerPlayer player, DungeonRun run) {
        if (run.status() != DungeonRunStatus.ACTIVE || !floors.supports(run.currentFloor()) || !allocator.isValidSlot(run.allocationSlot())) {
            return false;
        }
        ServerLevel level = server.getLevel(AvariceDimensions.DUNGEON);
        if (level == null) return false;
        DungeonArea area = allocator.area(run.allocationSlot());
        floors.ensureFloor(level, area, run.currentFloor());
        return AvariceTeleporter.teleport(player, level, floorLocation(area, run.currentFloor()));
    }

    private DungeonOperationResult finishTransition(DungeonRun transitioning) {
        if (!allocator.isValidSlot(transitioning.allocationSlot())) return recover(transitioning, "message.avarice.run.recovery_failed");
        ServerLevel targetLevel = server.getLevel(AvariceDimensions.DUNGEON);
        if (targetLevel == null) return recover(transitioning, "message.avarice.dimension.missing");
        DungeonArea area = allocator.area(transitioning.allocationSlot());
        List<ServerPlayer> participants = onlineParticipants(transitioning);
        if (participants.isEmpty()) return recover(transitioning, "message.avarice.run.no_online_participants");
        List<ServerPlayer> moved = new ArrayList<>();
        try {
            floors.ensureFloor(targetLevel, area, transitioning.transitionTargetFloor());
            DungeonLocation destination = floorLocation(area, transitioning.transitionTargetFloor());
            for (ServerPlayer participant : participants) {
                if (!AvariceTeleporter.teleport(participant, targetLevel, destination)) {
                    rollbackPlayers(moved, transitioning, area);
                    return recover(transitioning, "message.avarice.run.teleport_failed");
                }
                moved.add(participant);
            }
            DungeonRun active = transitioning.completeFloorTransition(tick());
            data.put(active);
            return DungeonOperationResult.success("message.avarice.run.floor_entered", active);
        } catch (RuntimeException exception) {
            rollbackPlayers(moved, transitioning, area);
            Avarice.LOGGER.error("Dungeon floor transition failed for run {} target {}", transitioning.runId(), transitioning.transitionTargetFloor(), exception);
            return recover(transitioning, "message.avarice.run.teleport_failed");
        }
    }

    private void rollbackPlayers(List<ServerPlayer> moved, DungeonRun run, DungeonArea area) {
        DungeonLocation location = run.currentFloor() > 0 ? floorLocation(area, run.currentFloor()) : run.startLocation();
        ServerLevel level = server.getLevel(location.dimension());
        if (level == null) return;
        if (run.currentFloor() > 0) floors.ensureFloor(level, area, run.currentFloor());
        for (ServerPlayer player : moved) AvariceTeleporter.teleport(player, level, location);
    }

    private DungeonOperationResult recover(DungeonRun run, String messageKey) {
        DungeonRun recovered = run.recoverInterruptedTransition(tick());
        data.put(recovered);
        return DungeonOperationResult.failure(messageKey, recovered);
    }

    private DungeonOperationResult reject(DungeonRun run, String messageKey) {
        Avarice.LOGGER.warn(
                "Rejected dungeon transition run={} status={} floor={} target={}",
                run.runId(), run.status(), run.currentFloor(), run.transitionTargetFloor()
        );
        return DungeonOperationResult.failure(messageKey, run);
    }

    private boolean validTarget(DungeonRun run, int targetFloor) {
        if (!floors.supports(targetFloor) || !allocator.isValidSlot(run.allocationSlot())) return false;
        if (run.status() == DungeonRunStatus.PREPARING) return run.currentFloor() == 0 && targetFloor == 1;
        return run.status() == DungeonRunStatus.ACTIVE && targetFloor == run.currentFloor() + 1;
    }

    private List<ServerPlayer> onlineParticipants(DungeonRun run) {
        return run.participants().stream().map(server.getPlayerList()::getPlayer).filter(java.util.Objects::nonNull).toList();
    }

    private static DungeonLocation floorLocation(DungeonArea area, int floor) {
        return DungeonLocation.at(AvariceDimensions.DUNGEON, area.floorSpawn(floor), 180.0F);
    }

    private long tick() {
        return server.overworld().getGameTime();
    }
}
