# Avarice Development Foundation

Minecraft `26.3`, NeoForge `26.3.0.114`, ModDevGradle `2.0.148`, Java `25`, and DAQEM UI Lib `22.1.0`.

## Run

Install/use JDK 25, import `build.gradle` in IntelliJ, then run `gradlew runClient`. The development client creates `config/avarice-dev/ui.json` and `config/avarice-dev/values.json` on first launch.

## Controls

- `/avarice dev ui` opens the UI Lib development screen.
- `-` toggles inspector mode on any open screen.
- `+` requests one validated UI reload and refreshes the open development screen.
- `/avarice dev values` reloads numeric definitions.
- `/avarice dev inventory` opens the server-backed Avarice inventory layout test.
- `/avarice dev abyss` enters the persistent Abyss hub.
- `/avarice dev run start` starts a placeholder dungeon run from The Abyss.
- `/avarice dev run next` advances the active run by one placeholder floor.
- `/avarice dev run return` abandons the active run and returns safely.
- `/avarice dev run status` reports dimension, allocation, run, transition, and persistence state.

## Configuration

`ui.json` is a versioned snapshot. Theme colour references use `$theme.colors.name`; bad or circular references fail validation and leave the active screen on its last valid snapshot. The layout resolver supports all nine anchors, parent-relative bounds, pixel/percentage dimensions, visibility, scale, pivot, and min/max dimensions. It operates in Minecraft GUI-scaled coordinates.

While development tools are enabled, saves beneath `config/avarice-dev` are debounced and routed to UI, values, or item definitions on the client thread. `F8` exposes GUI and raw coordinates, GUI scale, hovered/selected bounds, parent ID, anchor, pivot, and scale. Click an inspected configured element to select it, then use the screen controls to clear or copy its properties.

The animation utility is frame-time based. `AnimationEngine` supports replacement by identifier, delays, loops, reversal, cancellation, callbacks, and the built-in easing modes. It is intentionally independent of individual UI Lib components so adapters can animate any supported UI Lib property without parsing configuration during rendering.

`values.json` contains fixed, percentage, and multiplier definitions with safe formula data. `Percentage.addPoints(5)` and `increaseRelative(5)` deliberately have distinct semantics. The current values service is client-development data only; it does not claim authority over future gameplay/server state.

## Inventory layout test

`/avarice dev inventory` opens the 178×165 Avarice inventory texture. It uses the player inventory server-side, with the main inventory at `7,83`, hotbar at `7,142`, armour slots at `8,9` through `8,63`, offhand at `81,63`, and the magnet placeholder at `81,45`. The two weapon slots and trinket slot use item tags `avarice:weapons` and `avarice:trinkets`; in the dungeon, replacing one of those slots starts a 10-second cooldown. A tagged weapon in the primary slot adds 5% base attack speed while this test menu is open. Shift-click transfer is intentionally disabled until its routing rules are designed.

## Extension Points

- Implement `ReloadableDomain<T>` and register it with `ReloadManager` for future data domains.
- `LayoutResolver` is independent of UI Lib and can be adapted to any UI Lib component tree.
- `UiConfigService.subscribe` lets screens rebuild only after a successful snapshot replacement.
- UI Lib provides `AbstractScreen`, component trees, buttons, and rendering/input; Avarice provides JSON schema, validation, layout resolution, snapshots, and development tooling.
- `DungeonRunManager` owns validated server-side lifecycle operations while `PlaceholderFloorService` is the replaceable Phase 2 floor provisioner.

## Verification

Core tests cover theme references/cycles, anchor resolution, and percentage semantics. Run `gradlew test` with JDK 25. Development launch must download Minecraft artifacts; ensure the JDK trusts the network certificate used by your environment.
