package com.revilodev.avarice.dungeon;

import com.revilodev.avarice.Avarice;
import com.revilodev.avarice.world.AbyssManager;
import com.revilodev.avarice.world.AvariceDimensions;
import com.revilodev.avarice.world.AvariceTeleporter;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public final class DungeonReturn {
    private final MinecraftServer server;
    private final DungeonRunSavedData data;
    private final DungeonAreaAllocator allocator;
    private final FloorBuilder floors;
    private final AbyssManager abyss;

    public DungeonReturn(
            MinecraftServer server,
            DungeonRunSavedData data,
            DungeonAreaAllocator allocator,
            FloorBuilder floors,
            AbyssManager abyss
    ) {
        this.server = server;
        this.data = data;
        this.allocator = allocator;
        this.floors = floors;
        this.abyss = abyss;
    }

    public DungeonOperationResult returnRun(DungeonRun run, DungeonRunStatus outcome) {
        if (run.status().isTerminal() || !outcome.isTerminal()) {
            return DungeonOperationResult.failure("message.avarice.run.invalid_transition", run);
        }
        if (!abyss.ensureInitialized()) return DungeonOperationResult.failure("message.avarice.dimension.missing", run);
        List<ServerPlayer> moved = new ArrayList<>();
        for (ServerPlayer participant : onlineParticipants(run)) {
            if (!AvariceTeleporter.teleport(participant, abyss.level(), run.returnLocation())) {
                rollbackPlayers(moved, run);
                return DungeonOperationResult.failure("message.avarice.run.teleport_failed", run);
            }
            moved.add(participant);
        }
        DungeonRun terminal;
        try {
            terminal = run.terminate(outcome, tick());
        } catch (IllegalStateException exception) {
            rollbackPlayers(moved, run);
            Avarice.LOGGER.warn("Rejected dungeon return run={} status={} outcome={}", run.runId(), run.status(), outcome);
            return DungeonOperationResult.failure("message.avarice.run.invalid_transition", run);
        }
        data.put(terminal);
        String message = outcome == DungeonRunStatus.COMPLETED
                ? "message.avarice.run.completed"
                : outcome == DungeonRunStatus.FAILED ? "message.avarice.run.failed" : "message.avarice.run.returned";
        return DungeonOperationResult.success(message, terminal);
    }

    private void rollbackPlayers(List<ServerPlayer> moved, DungeonRun run) {
        if (run.currentFloor() < 1 || !allocator.isValidSlot(run.allocationSlot())) return;
        ServerLevel level = server.getLevel(AvariceDimensions.DUNGEON);
        if (level == null) return;
        DungeonArea area = allocator.area(run.allocationSlot());
        floors.ensureFloor(level, area, run.currentFloor());
        DungeonLocation location = DungeonLocation.at(AvariceDimensions.DUNGEON, area.floorSpawn(run.currentFloor()), 180.0F);
        for (ServerPlayer player : moved) AvariceTeleporter.teleport(player, level, location);
    }

    private List<ServerPlayer> onlineParticipants(DungeonRun run) {
        return run.participants().stream().map(server.getPlayerList()::getPlayer).filter(java.util.Objects::nonNull).toList();
    }

    private long tick() {
        return server.overworld().getGameTime();
    }
}
