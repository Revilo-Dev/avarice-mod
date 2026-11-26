package com.revilodev.avarice.dungeon;

import java.util.Collection;
import java.util.HashSet;
import java.util.OptionalInt;
import java.util.Set;

public final class DungeonAreaAllocator {
    public static final int GRID_WIDTH = 8;
    public static final int RUN_SPACING = 4096;
    public static final int FLOOR_SPACING = 256;
    public static final int CAPACITY = 64;

    public OptionalInt allocate(Collection<DungeonRun> runs) {
        Set<Integer> occupied = new HashSet<>();
        runs.stream().filter(run -> !run.status().isTerminal()).forEach(run -> occupied.add(run.allocationSlot()));
        for (int slot = 0; slot < CAPACITY; slot++) {
            if (!occupied.contains(slot)) return OptionalInt.of(slot);
        }
        return OptionalInt.empty();
    }

    public boolean isValidSlot(int slot) {
        return slot >= 0 && slot < CAPACITY;
    }

    public DungeonArea area(int slot) {
        if (!isValidSlot(slot)) throw new IllegalArgumentException("invalid allocation slot " + slot);
        return new DungeonArea(slot);
    }
}
