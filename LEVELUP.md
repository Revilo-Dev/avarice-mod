# LevelUP

LevelUP is a NeoForge progression framework that adds a Level Point and level track for players.
It provides a public API and event hooks so other mods can read player levels, grant points, enforce requirements, and react to progression changes.

See the [LevelUP Complete Guide](wiki/LevelUP-Complete-Guide.md) for the complete player, administrator, datapack, configuration, and mod-integration reference.

## Version 26.3-neo-6.0

This release targets Minecraft `26.3` and NeoForge `26.3.0.113`.

- **Java 25 is required to run the game.** The project toolchain is configured for Java 25.
- Gradle `8.14.3` cannot itself run on Java 25. In IntelliJ, use Java 21–24 as the **Gradle JVM** and leave Java 25 available as the project toolchain/run-time JDK.
- The built-in HUD and inventory LevelUP bars can be moved and resized independently. Their saved layout is local client configuration and is not sent to servers.
- The inventory layout editor opens a real inventory screen, so the bar can be positioned against the actual inventory UI.
- Level Point Orbs now simulate movement only on the logical server. Clients receive their position from the server, avoiding client-side entity movement crashes.

## Core Features

- Level Point progression that can remain separate from vanilla experience or consume positive vanilla experience gains.
- Configurable point curve, maximum level, hostile mob point-orb drops, and tagged mob point-orb drops.
- Eligible mob kills drop Level Point Orbs based on `sources.mob_level_drop_base_value` and mob max health.
- Server-side Level Point Orb entity and pickup flow.
- Animated top-center HUD overlay for Level Point gain feedback.
- Optional always-on HUD mode that keeps the level bar in the same on-screen position as the temporary overlay.
- Bottom HUD mode can replace the vanilla experience bar with a persistent LevelUP bar while the HUD is enabled.
- Player nametags can display the player's LevelUP level inline as `Lv# PlayerName`.
- Commands for administrators to grant Level Points and manage progression values, including player selectors.
- Includes a `test_skill_orb` item that grants Level Points on use.
- Integration hooks for external mods through static API calls and NeoForge events.

## Config

Common config:

- `progression.baseXpPerLevel` default `100`
- `progression.linearXpPerLevel` default `20`
- `progression.exponent` default `1.35`
- `progression.levelMultiplier` default `0.75`
- `progression.maxLevel` default `100`
- `sources.accept_experience_as_levels` default `false`
  Converts positive vanilla experience gains into Level Points one-for-one and disables automatic custom mob point-orb drops.
- `sources.mobs_drop_levels` default `true`
  When `true`, hostile enemy mobs can drop Level Point Orbs on player kills.
- `sources.mob_level_drop_base_value` default `8`
  Actual orb value is `base_value * max_health / 20`, rounded down with a minimum of `1`.
  Set to `0` to disable all automatic mob Level Point Orb drops.
- `sources.only_tagged_mobs_drop_levels` default `false`
  When `true`, only entity types in the `levelup:drops_levels` entity type tag drop Level Point Orbs (`levelup:drop_levels` is also accepted for legacy setups).
- `sources.mob_level_drop_whitelist` default `[]`
  Explicitly enables listed entity type IDs, including non-vanilla mobs that do not implement `Enemy`.
- `sources.mob_level_drop_blacklist` default `[]`
  Disables listed entity type IDs and takes priority over every other mob eligibility rule.
  This overrides the generic hostile mob mode.
  If both `sources.mobs_drop_levels` and `sources.only_tagged_mobs_drop_levels` are `false`, mobs never drop levels.

Client config:

- `hud.showTopCenterLevelOverlay` default `true`
  Controls the LevelUP HUD display.
- `hud.showTemporaryLevelOverlay` default `true`
  Controls temporary LevelUP HUD popups from Level Point updates and custom HUD events.
- `hud.levelHudPosition` default `top`
  Accepts `top` or `bottom`.
  `bottom` replaces the vanilla experience bar with a persistent LevelUP bar in normal HUD gameplay and skips the top progress animation.
- `hud.levelHudStayOnScreen` default `false`
  Keeps the LevelUP HUD visible even when no Level Point gain is currently animating.
  Uses the same animated top HUD bar and keeps it visible until this option is turned off, then it slides out normally.
- `hud.levelHudColor` default `#53a4bc`
  Tint color for the LevelUP progress fill and level text.
- `hud.showInventoryLevelBar` default `true`
  Controls the LevelUP progress bar shown in the inventory screen.
- `hud.showPlayerLevelBelowNametag` default `true`
  Prefixes player nametags with their LevelUP level as `Lv# PlayerName`.
- `hud.hudLevelBarOffsetX` default `0`
  Horizontal HUD LevelUP bar offset in pixels.
- `hud.hudLevelBarOffsetY` default `0`
  Vertical HUD LevelUP bar offset in pixels.
- `hud.hudLevelBarScale` default `1.0`
  HUD LevelUP bar scale, from `0.5` to `3.0`.
- `hud.inventoryLevelBarOffsetX` default `0`
  Horizontal inventory LevelUP bar offset in pixels.
- `hud.inventoryLevelBarOffsetY` default `0`
  Vertical inventory LevelUP bar offset in pixels.
- `hud.inventoryLevelBarScale` default `1.0`
  Inventory LevelUP bar scale, from `0.5` to `3.0`.
- `hud.openHudLevelBarRepositionGui` default `false`
  Set to `true` to open the HUD bar reposition GUI. It resets to `false` after opening.
- `hud.openInventoryLevelBarRepositionGui` default `false`
  Set to `true` to open the inventory bar reposition GUI. It resets to `false` after opening.

## Mob Tagging

LevelUP supports entity-type tagging to mark mobs as eligible for Level Point Orb drops when `sources.only_tagged_mobs_drop_levels=true`.

- Entity type tag: add entity IDs to `data/levelup/tags/entity_types/drops_levels.json` in a datapack.

Examples:

```json
{
  "replace": false,
  "values": [
    "minecraft:zombie",
    "minecraft:skeleton"
  ]
}
```

Runtime scoreboard tags such as `Tags:["drops_levels"]` are not used for LevelUP mob drop eligibility.

For dependency setup, source/Javadoc artifacts, API stability guidance, and integration testing, see the [Modder Tooling](wiki/Modder-Tooling.md) guide.

## Commands

- `/levelup multiplier <amount> <targets>`
- `/levelup add level <amount> <targets>`
- `/levelup add points <amount> <targets>`
- `/levelup set level <amount> <targets>`
- `/levelup set points <amount> <targets>`
- `/levelup set max <amount>`
- `/levelup hud <top|bottom>`
- `/levelup gui <inventory|hud>`
- `/levelup query [targets]`
- `/levelup debug spawn_orbs <points>`
- `/levelup debug drop_points <points> <targets>`
- `/levelup pause <true|false>`

`<targets>` accepts player names and standard player selectors such as `@s`, `@a`, and `@p`.

## Bar Layout Editor

Open an editor with `/levelup gui hud` or `/levelup gui inventory`.

- Drag anywhere on the outlined bar to move it.
- Drag the white lower-right corner to resize it from `0.5x` to `3.0x`.
- Hover the bar to see its current screen coordinates in a tooltip.
- Select **Done** to save, **Reset** to restore the default position and `1.0x` scale, or **Cancel** to discard changes.
- Bars use a centred base position plus saved pixel offsets; there are no anchors.

Default positions at `1.0x` scale:

- Top HUD: horizontally centred, `18` pixels from the top before its temporary slide animation.
- Bottom HUD: horizontally centred, `32` pixels above the bottom edge.
- Inventory: horizontally centred, `95` pixels below the screen midpoint.

## Included Content

- `levelup:test_skill_orb` grants `20` Level Points on use.
- `levelup:level_orb` is the custom Level Point Orb entity used by LevelUP rewards.

## Integration API (for other mods)

Use static methods from `com.revilo.levelup.api.LevelUpApi`.

- `String API_VERSION` is the published API surface version.
- `String getApiVersion()` returns the same API version at runtime.
- `LevelUpCapabilities getCapabilities()` reports supported integration features.
- `Optional<LevelUpPlayerView> getPlayerData(ServerPlayer player)` returns a stable read-only view without exposing attachments.
- `LevelCurve getActiveLevelCurve()` exposes the active configured progression curve.
- `int getLevel(Player player)` returns the player LevelUP level.
- `int getHighestLevelReached(Player player)` returns the highest LevelUP level this player has reached.
- `long getPoints(Player player)` returns total stored Level Points.
- `int getXpMultiplier(Player player)` returns the stored per-player point multiplier; the method keeps its legacy name.
- `long getPointsIntoCurrentLevel(Player player)` returns the player's current progress inside their active level band.
- `long getPointsNeededForNextLevel(Player player)` returns remaining points to level up.
- `float getProgressToNextLevel(Player player)` returns 0..1 progress for current level.
- `int getMaxLevel()` returns the active max level after config or runtime override resolution.
- `OptionalInt getMaximumLevel()` returns the configured maximum level.
- `boolean isAtMaximumLevel(ServerPlayer player)` checks whether the player is capped by the active max or lock.
- `double getLevelMultiplier()` returns the active point-cost multiplier after config or runtime override resolution.
- `long getPointsForNextLevel(int currentLevel)` returns the point cost to advance from the supplied level.
- `long getTotalPointsForLevel(int level)` returns the cumulative point floor for the supplied level.
- `int levelForTotalPoints(long totalPoints)` resolves a raw point total into a LevelUP level using the active max level.
- `boolean meetsLevelRequirement(Player player, int requiredLevel)` checks level gates.
- `LevelChangeResult addPoints(ServerPlayer player, long amount, ResourceLocation source)` grants points and returns the complete transaction result.
- `LevelChangeResult setPoints(ServerPlayer player, long points, ResourceLocation source)` directly sets total points, recalculates level, synchronizes, and fires events.
- `LevelChangeResult addLevels(ServerPlayer player, int levels, ResourceLocation source)` adds whole levels, capped by the effective maximum.
- `LevelChangeResult setLevel(ServerPlayer player, int level, ResourceLocation source)` sets level by assigning the cumulative point floor for that level.
- `LevelChangeResult reset(ServerPlayer player, ResourceLocation source)` resets points, level, multiplier, and highest-level tracking.
- XP/experience-named methods remain available as compatibility aliases for existing integrations.
- `boolean isLevelLocked(Player player)` returns whether player progression is currently level-locked.
- `int getLockedLevel(Player player)` returns locked cap level, or `-1` when unlocked.
- `void lockLevel(ServerPlayer player, int level)` locks progression to the supplied level cap for that player.
- `void unlockLevel(ServerPlayer player)` clears the player's lock and allows progression to continue.
- `boolean isPaused()` returns whether Level Point gains are currently paused.
- `void setPaused(boolean paused)` pauses or resumes Level Point gains.
- `void setMaxLevelOverride(int maxLevel)` overrides the configured hard cap until cleared.
- `void clearMaxLevelOverride()` restores the configured hard cap.
- `void setLevelMultiplierOverride(double levelMultiplier)` overrides the configured point-cost multiplier until cleared.
- `void clearLevelMultiplierOverride()` restores the configured point-cost multiplier.
- `void sync(ServerPlayer player)` forces network sync of progression data.
- `void registerMilestone(ResourceLocation id, int interval)` registers a milestone interval. Crossing level 4 to 11 with interval `5` fires milestones `5` and `10`.
- `void spawnLevelPointOrb(ServerLevel level, Vec3 position, int amount)` spawns Level Point Orbs.
- `void spawnLevelUpXpOrb(ServerLevel level, double x, double y, double z, int amount)` coordinate overload for orb spawning.
- Legacy `spawnLevelUpXpOrb...` helpers remain available for compatibility.
- `void spawnLevelUpXpOrbForLevel(ServerLevel level, double x, double y, double z, int level)` coordinate overload for level-based orb spawning.

Client GUI rendering API:

- `com.revilo.levelup.api.LevelUpClientApi.onDataUpdated(listener)` registers a client display-data sync listener.
- `com.revilo.levelup.api.LevelUpClientApi.removeDataUpdatedListener(listener)` unregisters a client sync listener.
- `com.revilo.levelup.api.LevelUpClientApi.getLocalLevelProgressDisplay()` returns local display data for GUI refreshes.
- `com.revilo.levelup.api.LevelUpClientApi.getLevelBarWidth()` returns level bar width.
- `com.revilo.levelup.api.LevelUpClientApi.getLevelBarHeight()` returns level bar height.
- `com.revilo.levelup.api.LevelUpClientApi.renderLevelBar(...)` renders the LevelUP bar with your provided progress/label.
- `com.revilo.levelup.api.LevelUpClientApi.renderTopHudStyleLevelBar(...)` renders with the same background, label placement, alpha, and tint as the LevelUP top HUD.
- `com.revilo.levelup.api.LevelUpClientApi.renderPlayerLevelBar(gui, x, y)` renders the local player's current level/progress and returns `false` if no local player is available.
- `com.revilo.levelup.api.LevelUpClientApi.renderTopHudStylePlayerLevelBar(gui, x, y)` renders the local player's current level/progress using the exact top HUD bar style.
- `com.revilo.levelup.api.LevelUpClientApi.openHudLevelBarRepositionGui()` opens the draggable HUD bar reposition GUI.
- `com.revilo.levelup.api.LevelUpClientApi.openInventoryLevelBarRepositionGui()` opens the draggable inventory bar reposition GUI.
- `com.revilo.levelup.api.LevelUpClientApi.setHudLevelBarOffset(x, y)` updates HUD bar offsets.
- `com.revilo.levelup.api.LevelUpClientApi.setInventoryLevelBarOffset(x, y)` updates inventory bar offsets.
- `com.revilo.levelup.api.LevelUpClientApi.getHudLevelBarOffsetX/Y()` reads HUD bar offsets.
- `com.revilo.levelup.api.LevelUpClientApi.getInventoryLevelBarOffsetX/Y()` reads inventory bar offsets.
- `com.revilo.levelup.api.LevelUpClientApi.getProgressToRequiredLevel(player, requiredLevel)` returns 0..1 progress toward a specific required level.
- `com.revilo.levelup.api.LevelUpClientApi.renderRequiredLevelBar(...)` renders a top-HUD-style bar for locked buttons, boss keys, dungeons, or other level requirements.
- `com.revilo.levelup.api.LevelUpClientApi.renderRewardPreviewBar(...)` renders a top-HUD-style preview after a proposed point reward.
- `com.revilo.levelup.api.LevelUpClientApi.appendLevelRequirementTooltip(...)` appends standard LevelUP requirement lines to an item or GUI tooltip.

## Events You Can Subscribe To

All events are posted on `NeoForge.EVENT_BUS`.

- `LevelUpXpGainedEvent` is cancellable and fires before points are applied; its class name is retained for compatibility.
- `LevelUpXpGainedEvent#setAmount(long)` lets integrations scale or clamp incoming points.
- `LevelUpLevelChangedEvent` fires when stored level changes in either direction.
- `LevelUpLevelChangedEvent.LevelUp` fires once per level gained when multiple levels are gained in one update.
- `com.revilo.levelup.api.event.PlayerExperienceChangeEvent` fires after stored points change; its class name is retained for compatibility.
- `com.revilo.levelup.api.event.PlayerLevelChangeEvent` fires server-side after a player level changes and includes previous level, new level, reason, and source.
- `com.revilo.levelup.api.event.PlayerLevelMilestoneEvent` fires for every registered milestone crossed during a level increase.
- `com.revilo.levelup.api.event.PlayerLevelDataLoadedEvent` fires on the logical server during player login after LevelUP attachment data is available and before sync.
- `LevelUpLevelLockChangedEvent` fires when a player's lock cap changes (`-1` means unlocked).
- `LevelUpOutputEvent` fires only when level increases and includes old/new level, old/new point totals, source, and levels gained.
- `LevelUpHudDisplayEvent` can be posted client-side to force the HUD on-screen with custom text/progress for a duration.

## Progression Transactions

Use the result-returning APIs when integrating rewards, commands, quests, or another progression system.

```java
LevelChangeResult result = LevelUpApi.addPoints(
        player,
        100,
        ResourceLocation.fromNamespaceAndPath("examplemod", "quest_reward")
);

for (int milestone : result.crossedMilestones(5)) {
    // Award external milestone rewards from your own mod data.
}
```

Large point gains are treated as one transaction. A change from level `4` to `16` returns crossed levels `5` through `16`, and a 5-level milestone interval reports `5`, `10`, and `15`.

## Milestone Events

Register milestone intervals during common setup or integration initialization.

```java
LevelUpApi.registerMilestone(
        ResourceLocation.fromNamespaceAndPath("knowledge", "knowledge_point"),
        5
);
```

Then consume milestone events without polling player data.

```java
@SubscribeEvent
public static void onMilestone(PlayerLevelMilestoneEvent event) {
    if (event.getMilestoneId().equals(ResourceLocation.fromNamespaceAndPath("knowledge", "knowledge_point"))) {
        // Award Knowledge Points in the Knowledge mod.
    }
}
```

## Point Source IDs

Use `com.revilo.levelup.api.LevelUpSources` constants when possible:

- `UNKNOWN`
- `MOB_KILL`
- `GATEWAY_COMPLETE`
- `QUEST_COMPLETE`
- `OBJECTIVE_COMPLETE`
- `ORB_PICKUP`
- `ITEM_USE`
- `COMMAND`
- `VANILLA_EXPERIENCE`

Create custom source IDs with `LevelUpSources.id("your_path")`.

## Minimal Example

```java
LevelUpApi.addPoints(player, 25, LevelUpSources.QUEST_COMPLETE);
LevelUpApi.setMaxLevelOverride(1000);
LevelUpApi.setLevelMultiplierOverride(0.60D);

if (LevelUpApi.meetsLevelRequirement(player, 20)) {
    // Unlock feature
}
```

## Rendering The LevelUP Bar In Your Own GUI

Use the client API from `Screen#extractRenderState(...)` (or the equivalent GUI extraction event) in NeoForge 26.3.

```java
import com.revilo.levelup.api.LevelUpClientApi;
import net.minecraft.client.gui.GuiGraphicsExtractor;

@Override
public void extractRenderState(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick) {
    super.extractRenderState(guiGraphics, mouseX, mouseY, partialTick);

    int x = (this.width - LevelUpClientApi.getLevelBarWidth()) / 2;
    int y = this.topPos + 16; // place where your GUI needs it
    LevelUpClientApi.renderTopHudStylePlayerLevelBar(guiGraphics, x, y);
}
```

If you want to use custom text/progress instead of the current player value:

```java
LevelUpClientApi.renderTopHudStyleLevelBar(
        guiGraphics,
        x,
        y,
        0.42F,
        Component.translatable("levelup.ui.level", 12)
);
```

## Useful GUI Patterns

Level-gated GUI button:

```java
int requiredLevel = 20;
boolean unlocked = LevelUpApi.meetsLevelRequirement(this.minecraft.player, requiredLevel);
this.miningMasteryButton.active = unlocked;

LevelUpClientApi.renderRequiredLevelBar(
        guiGraphics,
        this.leftPos + 8,
        this.topPos + 42,
        requiredLevel
);
```

Boss key or dungeon entry confirmation:

```java
int requiredLevel = 35;
LevelUpClientApi.renderRequiredLevelBar(
        guiGraphics,
        (this.width - LevelUpClientApi.getLevelBarWidth()) / 2,
        this.topPos + 64,
        requiredLevel
);

this.enterButton.active = LevelUpApi.meetsLevelRequirement(this.minecraft.player, requiredLevel);
```

Item tooltip requirement:

```java
@Override
public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
    Player player = Minecraft.getInstance().player;
    if (player != null) {
        LevelUpClientApi.appendLevelRequirementTooltip(tooltip, player, 15);
    }
}
```

Custom reward preview before claiming:

```java
long pointReward = 250L;
LevelUpClientApi.renderRewardPreviewBar(
        guiGraphics,
        this.leftPos + 8,
        this.topPos + 92,
        pointReward
);
```

## Let Other Mods Reposition LevelUP Bars

Other mods can trigger the built-in draggable GUI from client code:

```java
import com.revilo.levelup.api.LevelUpClientApi;

// Opens the HUD bar reposition screen.
LevelUpClientApi.openHudLevelBarRepositionGui();

// Opens the inventory bar reposition screen.
LevelUpClientApi.openInventoryLevelBarRepositionGui();
```

Other mods can also update config positions directly:

```java
LevelUpClientApi.setHudLevelBarOffset(12, -6);
LevelUpClientApi.setInventoryLevelBarOffset(0, 18);
```

These methods update position only. Users can change the bar scale through the built-in layout editor or the corresponding `hudLevelBarScale` and `inventoryLevelBarScale` client config values.

If a mod wants to trigger repositioning through config mutation (for example from a config GUI button), set:

- `hud.openHudLevelBarRepositionGui=true`, or
- `hud.openInventoryLevelBarRepositionGui=true`

LevelUP consumes these flags and resets them to `false` after opening the GUI.
