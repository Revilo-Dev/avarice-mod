package com.revilodev.avarice.dungeon;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.revilodev.avarice.Avarice;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

public final class DungeonRunSavedData extends SavedData {
    public static final Codec<DungeonRunSavedData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            DungeonRun.CODEC.listOf().optionalFieldOf("runs", List.of()).forGetter(DungeonRunSavedData::allRuns),
            Codec.BOOL.optionalFieldOf("abyss_initialized", false).forGetter(DungeonRunSavedData::abyssInitialized),
            Codec.BOOL.optionalFieldOf("abyss_environment_sanitized", false).forGetter(DungeonRunSavedData::abyssEnvironmentSanitized),
            Codec.BOOL.optionalFieldOf("abyss_platform_removed", false).forGetter(DungeonRunSavedData::abyssPlatformRemoved)
    ).apply(instance, DungeonRunSavedData::new));
    public static final SavedDataType<DungeonRunSavedData> TYPE = new SavedDataType<>(
            Identifier.fromNamespaceAndPath(Avarice.MODID, "dungeon_runs"),
            DungeonRunSavedData::new,
            CODEC
    );

    private final Map<UUID, DungeonRun> runs = new LinkedHashMap<>();
    private boolean abyssInitialized;
    private boolean abyssEnvironmentSanitized;
    private boolean abyssPlatformRemoved;

    public DungeonRunSavedData() { }

    private DungeonRunSavedData(List<DungeonRun> runs, boolean abyssInitialized, boolean abyssEnvironmentSanitized, boolean abyssPlatformRemoved) {
        for (DungeonRun run : runs) {
            if (this.runs.put(run.runId(), run) != null) throw new IllegalArgumentException("duplicate run id " + run.runId());
        }
        this.abyssInitialized = abyssInitialized;
        this.abyssEnvironmentSanitized = abyssEnvironmentSanitized;
        this.abyssPlatformRemoved = abyssPlatformRemoved;
    }

    public static DungeonRunSavedData get(MinecraftServer server) {
        return server.getDataStorage().computeIfAbsent(TYPE);
    }

    public List<DungeonRun> allRuns() {
        return List.copyOf(runs.values());
    }

    public Optional<DungeonRun> get(UUID runId) {
        return Optional.ofNullable(runs.get(runId));
    }

    public Optional<DungeonRun> activeRunFor(UUID playerId) {
        return runs.values().stream().filter(run -> !run.status().isTerminal() && run.participants().contains(playerId)).findFirst();
    }

    public void put(DungeonRun run) {
        runs.put(run.runId(), run);
        setDirty();
    }

    public boolean abyssInitialized() {
        return abyssInitialized;
    }

    public void markAbyssInitialized() {
        if (abyssInitialized) return;
        abyssInitialized = true;
        setDirty();
    }

    public boolean abyssEnvironmentSanitized() {
        return abyssEnvironmentSanitized;
    }

    public void markAbyssEnvironmentSanitized() {
        if (abyssEnvironmentSanitized) return;
        abyssEnvironmentSanitized = true;
        setDirty();
    }

    public boolean abyssPlatformRemoved() {
        return abyssPlatformRemoved;
    }

    public void markAbyssPlatformRemoved() {
        if (abyssPlatformRemoved) return;
        abyssPlatformRemoved = true;
        setDirty();
    }

    public void pruneTerminalRuns(int retainedCount) {
        List<DungeonRun> terminal = runs.values().stream()
                .filter(run -> run.status().isTerminal())
                .sorted(Comparator.comparingLong(DungeonRun::lastUpdatedTick).reversed())
                .toList();
        if (terminal.size() <= retainedCount) return;
        for (DungeonRun run : new ArrayList<>(terminal.subList(retainedCount, terminal.size()))) runs.remove(run.runId());
        setDirty();
    }
}
