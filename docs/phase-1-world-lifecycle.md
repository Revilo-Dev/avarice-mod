# Phase 1 world lifecycle

## Architecture

The Abyss and dungeon are static data-driven dimensions. Both use an empty flat generator and the dedicated `avarice:void` dimension type. The type has no skybox, skylight, End dragon fight, or End dimension behavior. The Abyss also rejects every mob entity server-side, while players, dropped items, and future non-mob infrastructure remain unaffected.

The server initializes the Abyss platform when startup completes and persists The Abyss spawn as the world's default respawn location. New players and players without a personal respawn point therefore spawn safely on the starter platform.

Existing development saves created with the former End dimension type receive a one-time migration. It removes only bedrock, End portal, and End gateway blocks within 16 blocks of the Abyss origin, then persists a completion marker so player-built areas are never broadly scanned or regenerated.

The dungeon uses isolated regions in one dimension rather than creating dimensions per run. Each active run owns one of 64 persisted allocation slots. Slots are arranged on an 8 by 8 grid with 4096 blocks between runs and 256 blocks between placeholder floors. Terminal runs release their slots logically; old blocks are retained and overwritten only when that validated slot is reused. No world deletion or permanent chunk forcing is used.

`DungeonRunSavedData` is server-global saved data. It retains immutable typed run snapshots, participant UUIDs, allocation ownership, transition targets, start and return locations, and the Abyss initialization marker. The manager keeps up to 32 terminal records for diagnostics and reuses their allocation slots.

`DungeonRunManager` is the only gameplay entry point for the gateway and development commands. `DungeonTransitionService` records a transition before provisioning or teleporting, commits the floor only after every online participant moves successfully, and rolls back moved players on failure. `DungeonReturnService` centralizes completed, abandoned, and failed returns.

`PlaceholderFloorService` owns the Phase 1 rooms. Phase 2 can replace its room construction while retaining run allocation, persistence, validation, teleportation, return handling, and commands.

## Recovery

Participant associations remain in saved data when a player disconnects. Login reconciliation resumes a recorded transition, restores an active floor, or returns the player to The Abyss and marks the run failed when its dimension, floor, or allocation is invalid. A player found in the dungeon without an active run is returned to The Abyss.

## Manual verification

1. Run `gradlew runClient`, create a world, grant operator permissions, and run `/avarice dev abyss`.
2. Confirm the 25 by 25 starter platform and central illuminated gateway exist and remain after leaving and re-entering.
3. Use the gateway and confirm floor 1 appears, then use each floor gateway to reach floors 2 and 3 and return to The Abyss.
4. Run `/avarice dev run status` on every floor and confirm the UUID, floor, slot, origin, participant count, transition state, and persistence diagnostics.
5. Start another run and confirm it receives a new UUID and can reuse a released slot.
6. Activate the gateway repeatedly and confirm only one active run exists.
7. Stop the server while on a floor, restart it, reconnect, and confirm the same run UUID and floor are restored.
8. Disconnect on a floor without stopping the server, reconnect, and confirm the run is restored at its safe spawn.
9. Connect two operator players, start separate runs, and confirm their slot numbers and floor origins differ.
10. Run `gradlew runServer`, accept `eula.txt` if required, rerun it, and confirm startup reaches the dedicated server ready state without client classloading errors.

The restart, reconnect, two-player, and interactive gateway scenarios require manual verification unless a suitable multi-client test harness is available.
