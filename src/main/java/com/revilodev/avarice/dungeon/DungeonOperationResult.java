package com.revilodev.avarice.dungeon;

public record DungeonOperationResult(boolean success, String messageKey, DungeonRun run) {
    public static DungeonOperationResult success(String messageKey, DungeonRun run) {
        return new DungeonOperationResult(true, messageKey, run);
    }

    public static DungeonOperationResult failure(String messageKey, DungeonRun run) {
        return new DungeonOperationResult(false, messageKey, run);
    }
}
