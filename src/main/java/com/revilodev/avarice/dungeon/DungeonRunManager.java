package com.revilodev.avarice.dungeon;

import com.revilodev.avarice.Avarice;
import com.revilodev.avarice.world.AbyssManager;
import com.revilodev.avarice.world.AvariceDimensions;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;
import java.util.UUID;
import java.util.Locale;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public final class DungeonRunManager {
    private static final int TERMINAL_HISTORY_LIMIT = 32;

    private final MinecraftServer server;
    private final DungeonRunSavedData data;
    private final DungeonAreaAllocator allocator;
    private final FloorBuilder floors;
    private final AbyssManager abyss;
    private final DungeonTransition transitions;
    private final DungeonReturn returns;

    private DungeonRunManager(MinecraftServer server) {
        this.server = server;
        this.data = DungeonRunSavedData.get(server);
        this.allocator = new DungeonAreaAllocator();
        this.floors = new FloorBuilder();
        this.abyss = new AbyssManager(server, data);
        this.transitions = new DungeonTransition(server, data, allocator, floors);
        this.returns = new DungeonReturn(server, data, allocator, floors, abyss);
        validateRestoredRuns();
    }

    public static DungeonRunManager get(MinecraftServer server) {
        return new DungeonRunManager(server);
    }

    public DungeonOperationResult startRun(ServerPlayer player) {
        if (!player.level().dimension().equals(AvariceDimensions.ABYSS)) {
            return DungeonOperationResult.failure("message.avarice.run.requires_abyss", null);
        }
        Optional<DungeonRun> existing = findActiveRun(player.getUUID());
        if (existing.isPresent()) return reject(existing.get(), "message.avarice.run.already_active");
        if (!abyss.ensureInitialized()) return DungeonOperationResult.failure("message.avarice.dimension.missing", null);
        data.pruneTerminalRuns(TERMINAL_HISTORY_LIMIT);
        OptionalInt allocation = allocator.allocate(data.allRuns());
        if (allocation.isEmpty()) return DungeonOperationResult.failure("message.avarice.run.capacity_reached", null);
        DungeonRun created = DungeonRun.create(player.getUUID(), allocation.getAsInt(), DungeonLocation.fromPlayer(player), abyss.returnLocation(), tick());
        data.put(created);
        DungeonRun preparing = created.changeStatus(DungeonRunStatus.PREPARING, tick());
        data.put(preparing);
        DungeonOperationResult result = transitions.transition(preparing.runId(), FloorBuilder.MIN_FLOOR);
        if (!result.success()) {
            DungeonRun failed = result.run().terminate(DungeonRunStatus.FAILED, tick());
            data.put(failed);
            return DungeonOperationResult.failure(result.messageKey(), failed);
        }
        return DungeonOperationResult.success("message.avarice.run.started", result.run());
    }

    public DungeonOperationResult advanceFloor(ServerPlayer player) {
        DungeonRun run = findActiveRun(player.getUUID()).orElse(null);
        if (run == null) return DungeonOperationResult.failure("message.avarice.run.none", null);
        if (run.status() == DungeonRunStatus.TRANSITIONING) return reject(run, "message.avarice.run.transitioning");
        if (run.status() != DungeonRunStatus.ACTIVE) return reject(run, "message.avarice.run.invalid_transition");
        if (run.currentFloor() >= FloorBuilder.MAX_FLOOR) return reject(run, "message.avarice.run.final_floor");
        return transitions.transition(run.runId(), run.currentFloor() + 1);
    }

    public DungeonOperationResult completeRun(ServerPlayer player) {
        DungeonRun run = findActiveRun(player.getUUID()).orElse(null);
        if (run == null) return DungeonOperationResult.failure("message.avarice.run.none", null);
        if (run.status() != DungeonRunStatus.ACTIVE || run.currentFloor() != FloorBuilder.MAX_FLOOR) {
            return reject(run, "message.avarice.run.not_complete");
        }
        return returns.returnRun(run, DungeonRunStatus.COMPLETED);
    }

    public DungeonOperationResult abandonRun(ServerPlayer player) {
        DungeonRun run = findActiveRun(player.getUUID()).orElse(null);
        if (run == null) return DungeonOperationResult.failure("message.avarice.run.none", null);
        return returns.returnRun(run, DungeonRunStatus.ABANDONED);
    }

    public DungeonOperationResult failRun(ServerPlayer player) {
        DungeonRun run = findActiveRun(player.getUUID()).orElse(null);
        if (run == null) return DungeonOperationResult.failure("message.avarice.run.none", null);
        return returns.returnRun(run, DungeonRunStatus.FAILED);
    }

    public DungeonOperationResult enterAbyss(ServerPlayer player) {
        Optional<DungeonRun> active = findActiveRun(player.getUUID());
        if (active.isPresent()) return returns.returnRun(active.get(), DungeonRunStatus.ABANDONED);
        if (!abyss.teleport(player)) return DungeonOperationResult.failure("message.avarice.dimension.missing", null);
        return DungeonOperationResult.success("message.avarice.abyss.entered", null);
    }

    public boolean initializeAbyss() {
        return abyss.ensureInitialized();
    }

    public DungeonOperationResult handleGateway(ServerPlayer player) {
        if (player.level().dimension().equals(AvariceDimensions.ABYSS)) return startRun(player);
        if (!player.level().dimension().equals(AvariceDimensions.DUNGEON)) {
            return DungeonOperationResult.failure("message.avarice.gateway.wrong_dimension", null);
        }
        DungeonRun run = findActiveRun(player.getUUID()).orElse(null);
        if (run == null) return DungeonOperationResult.failure("message.avarice.run.none", null);
        return run.currentFloor() == FloorBuilder.MAX_FLOOR ? completeRun(player) : advanceFloor(player);
    }

    public Optional<DungeonRun> findActiveRun(UUID playerId) {
        return data.activeRunFor(playerId);
    }

    public void reconcile(ServerPlayer player) {
        DungeonRun run = findActiveRun(player.getUUID()).orElse(null);
        if (run == null) {
            if (player.level().dimension().equals(AvariceDimensions.DUNGEON)) abyss.teleport(player);
            return;
        }
        DungeonOperationResult result;
        if (run.status() == DungeonRunStatus.TRANSITIONING) {
            result = transitions.resume(run);
        } else if (run.status() == DungeonRunStatus.CREATED) {
            DungeonRun preparing = run.changeStatus(DungeonRunStatus.PREPARING, tick());
            data.put(preparing);
            result = transitions.transition(preparing.runId(), FloorBuilder.MIN_FLOOR);
        } else if (run.status() == DungeonRunStatus.PREPARING) {
            result = transitions.transition(run.runId(), FloorBuilder.MIN_FLOOR);
        } else if (run.status() == DungeonRunStatus.ACTIVE && transitions.restorePlayer(player, run)) {
            result = DungeonOperationResult.success("message.avarice.run.recovered", run);
        } else {
            result = DungeonOperationResult.failure("message.avarice.run.recovery_failed", run);
        }
        if (result.success()) {
            player.sendSystemMessage(Component.translatable(result.messageKey(), result.run().currentFloor()));
            return;
        }
        DungeonRun current = data.get(run.runId()).orElse(run);
        if (!current.status().isTerminal()) data.put(current.terminate(DungeonRunStatus.FAILED, tick()));
        abyss.teleport(player);
        player.sendSystemMessage(Component.translatable("message.avarice.run.recovery_fallback"));
    }

    public List<Component> diagnostics(ServerPlayer player) {
        List<Component> lines = new ArrayList<>();
        lines.add(Component.translatable(
                "message.avarice.status.location",
                player.level().dimension().identifier().toString(),
                String.format(Locale.ROOT, "%.2f", player.getX()),
                String.format(Locale.ROOT, "%.2f", player.getY()),
                String.format(Locale.ROOT, "%.2f", player.getZ())
        ));
        DungeonRun run = findActiveRun(player.getUUID()).orElse(null);
        if (run == null) {
            lines.add(Component.translatable("message.avarice.status.no_run"));
        } else {
            DungeonArea area = allocator.area(run.allocationSlot());
            lines.add(Component.translatable("message.avarice.status.run", run.runId().toString(), run.status().name(), run.currentFloor()));
            lines.add(Component.translatable(
                    "message.avarice.status.area",
                    run.allocationSlot(), area.floorOrigin(Math.max(1, run.currentFloor())).toShortString(), run.participants().size()
            ));
            lines.add(Component.translatable(
                    "message.avarice.status.transition",
                    run.status() == DungeonRunStatus.TRANSITIONING, run.transitionTargetFloor()
            ));
        }
        lines.add(Component.translatable("message.avarice.status.persistence", data.isDirty(), data.allRuns().size()));
        return List.copyOf(lines);
    }

    private void validateRestoredRuns() {
        Set<Integer> activeSlots = new HashSet<>();
        Set<UUID> activePlayers = new HashSet<>();
        for (DungeonRun run : data.allRuns()) {
            if (run.status().isTerminal()) continue;
            boolean invalidFloor = run.currentFloor() < 0 || run.currentFloor() > FloorBuilder.MAX_FLOOR;
            boolean invalidTransition = run.status() == DungeonRunStatus.TRANSITIONING && !floors.supports(run.transitionTargetFloor());
            boolean duplicateSlot = activeSlots.contains(run.allocationSlot());
            boolean duplicatePlayer = run.participants().stream().anyMatch(activePlayers::contains);
            if (!allocator.isValidSlot(run.allocationSlot()) || invalidFloor || invalidTransition || duplicateSlot || duplicatePlayer) {
                data.put(run.terminate(DungeonRunStatus.FAILED, tick()));
                Avarice.LOGGER.warn("Invalid persisted dungeon run marked failed run={} slot={} status={}", run.runId(), run.allocationSlot(), run.status());
            } else {
                activeSlots.add(run.allocationSlot());
                activePlayers.addAll(run.participants());
            }
        }
    }

    private DungeonOperationResult reject(DungeonRun run, String messageKey) {
        Avarice.LOGGER.warn("Rejected dungeon operation run={} status={} floor={}", run.runId(), run.status(), run.currentFloor());
        return DungeonOperationResult.failure(messageKey, run);
    }

    private long tick() {
        return server.overworld().getGameTime();
    }
}
