AGENTS.md — Avarice development guidance
Local AI coding-agent instructions for the Avarice Minecraft mod. This is a living design reference, not proof that planned systems have been implemented.

1. Project identity and scope
   Avarice (also referred to in planning as Avarice: The Library of God) is an ambitious, replayable roguelite dungeon crawler built as a Minecraft mod.
- Target: Minecraft 26.3, NeoForge, ModDevGradle, Java matching the official 26.3 toolchain (currently planned as Java 25).
- Mod ID: avarice.
- UI dependency: DAQEM UI Lib, with actual compatible artifact/version/API to be verified before coding.
- Other mandatory dependencies: Developed in-house; do not make arbitrary external gameplay mods mandatory.
- Current milestone: Technical foundation and developer tooling only, unless a task explicitly authorizes gameplay work.
- Primary development environment: Windows and IntelliJ IDEA.
- Source of gameplay intent: Existing Avarice Miro planning board, https://miro.com/app/board/uXjVL1pfGss=/ , plus explicit subsequent user requests.
- Design status: Much of the game is a plan, experiment, or open question. Never present design notes as already implemented.
  Working principles for agents
- Respect the difference between confirmed direction, proposed design, and unresolved decisions.
- Do not invent canon, exact drop chances, classes, recipes, entity registrations, or final balancing rules.
- When a feature is not requested, do not implement it opportunistically.
- Keep architecture friendly to a large codebase, but avoid speculative abstractions and placeholder systems.
- Verify NeoForge 26.3 and UI Lib APIs against actual source/docs; do not transplant older 1.20/1.21 code blindly.
- Preserve existing files and user work. Make focused changes and report what actually compiles and runs.
- Prefer data-driven content where it materially accelerates iteration.
- Keep server-authoritative state off the client and client-only imports off dedicated-server classloading paths.
2. Gameplay vision
   The intended core loop is prepare a build → enter a key-gated, procedurally generated dungeon → explore, fight, loot, acquire modifiers and knowledge → risk pressing farther → return, escape or die → convert the results into lasting options for future runs.

The experience should center on risk, greed, resource extraction, character/build expression, and the tension of deciding whether to progress deeper. Dungeon runs grow stronger and more dangerous as the player stays longer. Gear, temporary run modifiers, corruption, knowledge unlocks, sacrifice, and permanent preparation systems interact.

This is not intended as a simple vanilla survival add-on. The player spends substantial time in a purpose-built dungeon and a shared, customizable base. The precise relationship with the normal overworld is not finalized.
Themes
- Greed: Collecting more can increase power, but overcommitting or keeping too much has consequences.
- Knowledge: A vast mysterious library/tower contains lost lore, research and ways to unlock content.
- Sacrifice: Discarding or offering items can convert excess loot into gold, favour or progression.
- Corruption: Prolonged dungeon exposure increases rewards and brings stronger pursuit.
- Resistance vs cult: Competing factions offer help with different prices and loyalties.
- Repeatable builds: Loot, weapons, gear, cards, skills and loadouts encourage changing strategies across runs.
3. World, setting and story
   World layout
- The Abyss / floor 0: An expansive shared void-like home base with a large central dungeon gateway, currently envisioned around coordinate 0,0. Players can prepare, craft, upgrade and potentially build or decorate their own space there.
- The dungeon: A layered structure described as a library/tower above forgotten catacombs, with thematic floor bands, branching procedural rooms, loot, enemies and bosses.
- Forgotten Archives: Planned early band, floors 1–10, themed as an abandoned library where knowledge is weak and the resistance is first encountered.
- Later bands: Not fully named or specified. Do not invent floors 11–100.
- Prologue: A possible introductory forest or catacomb/tutorial area was discussed but remains optional and undecided.
- Room vocabulary: Common/main room, corridor, staircase, treasure room, spawn room, and other specialized pieces.
  Story direction
- The universe includes seven gods associated with the seven deadly sins; the notes suggest only three are present in this world.
- The god of greed is the strongest central antagonist, with the player's long-term quest to defeat it at the top of the tower.
- The library contains knowledge, secrets, archives and many progression clues.
- The resistance consists of characters who aid the player and may be rescued or brought into the base. Suggested NPC roles include a resistance leader/master, archivist/knowledge keeper and forger/armorer.
- The cult reveres the powers of the tower and monarchs. Contracts can offer early advantages with later consequences, restrict other relationships, and potentially lead to betrayal.
- Monarchs are powerful, knight-like beings who hunt players under high corruption. Some ideas involve petrified statues awakening and rifts opening as corruption grows.
- The precise lore, NPC dialogue and quest sequence are not finalized.
4. The roguelite loop and run boundaries
   Starting and preparing
- Floor 0 is the player's place to respawn, prepare, access NPCs, store extracted resources and enter the dungeon.
- Dungeon access is envisioned through keys. Basic keys must not be prohibitively expensive or softlock progress.
- Stronger keys may increase run/deck capacity and allow additional pre-run modifier cards at a higher resource/corruption cost.
- Players may select or modify loadouts before entering. The exact loadout model remains subject to design iteration.
- Forging, upgrades, enchanting, equipment choices, trinkets and knowledge unlocks should improve future attempts.
  During a run
- Explore generated rooms, enemies, loot containers and floor bosses.
- Accumulate temporary cards, deck effects, booster packs and other modifiers, with choices between floors.
- Choose between continuing upward/deeper and returning to base after defeating a floor.
- Loot and enemies can scale with progression, greed, corruption, gear, knowledge and selected run modifiers.
- Risk-reward should be apparent to players; effects should be readable rather than silently punishing choices.
  Ways to end or interrupt a run
1. Exit after a cleared floor: Return voluntarily with eligible rewards.
2. Bail stone: Emergency exit, typically forfeiting loot collected during the run but preserving specified permanent/protected equipment or progression.
3. Death: Return to base and lose equipment, carried loot, progress or access depending on mode and checkpoint rules.
   The exact rules for carrying items between floors, keeping modifiers, checkpoint resets and co-op death resolution still need confirmation.
   Difficulty concepts from the board
- Easy: Lose collected loot on death but keep equipped gear.
- Normal: Allow some form of expensive equipment buyback.
- Hard: Permanently lose gear.
- Hardcore: Reset deeper progression, no checkpoint or buyback, and a more restrictive starting loadout.
  Treat these as design proposals, not locked balancing specifications.
5. Progression and knowledge
   Levels, skills and abilities
- A level range 0–100 appears in planning.
- Proposed rate: one skill point each level, one ability point every two levels.
- Some notes suggest an additional dungeon point per level for upgrades such as deck capacity, packs, and loadout or run rules.
- Ability costs may grow to encourage build variety and prevent purchasing everything.
- Skill/ability respeccing may require gold at the base.
- The design may build on an in-house system called Aura, but no external mandatory dependency should be assumed.
- The exact progression economy, stat calculations and unlock cadence are not final.
  Knowledge collection and unlocking
- Knowledge is collected as books or discoveries in dungeons, then claimed or researched at a base archivist/knowledge keeper.
- Knowledge areas include lore, weapons, items, secrets, progression and enemy/item information.
- It can unlock craftable gear, possible new loot/spawns, shortcuts, keys, NPCs, and other research.
- Lore scraps may fill in a collective archive/gospel, gradually explaining the world.
- Knowledge should be a meaningful system, not just a second experience bar.
  Shortcuts and checkpoints
- Planned shortcut unlocks could let players skip up to ten floors after defeating a relevant floor boss and contributing required resources.
- Floor checkpoints such as 10, 20, 30 appear in planning.
- Precise unlock costs and behavior across difficulty modes are undecided.
6. Corruption and greed
   These are separate but interacting concepts.
   Corruption
- A run-time escalation pressure, generally increasing the longer a player remains in the dungeon.
- Higher corruption can improve loot strength, rarity and rewards while increasing danger.
- At maximum corruption, monarch encounters or rift events may begin and intensify.
- Leaving the dungeon is the principal way to reset corruption and run-specific modifiers.
- Defeating a monarch might reduce corruption.
- Possible staged presentation: statues awaken, then monarchs arrive through rifts.
- Timing, thresholds, difficulty scaling and the distinction between corruption stages remain open design decisions.
  Greed
- An evolving player score/attribute, tentatively 0–100, derived from behaviour across runs.
- Possible positive contributors: staying longer, maximizing corruption, collecting more crates and extracting large amounts of loot.
- Possible negative contributors: bailing early, skipping crates and sacrificing items.
- Greed influences loot and difficulty; one note suggests negative effective greed after bailout penalties, so the range/clamping policy is not settled.
- Make calculation weights data-driven and easy to inspect, rather than hardcoding a final formula at this stage.
7. Equipment, inventory and loot
   Inventory proposal
- Redesign vanilla inventory to better suit dungeon loot and combat.
- The inventory may remove the conventional hotbar and provide dedicated primary and secondary weapon slots.
- Additional equipment concepts include an offhand and trinket slot(s).
- Proposed normal stack size is 99 for appropriate items, subject to technical/interaction constraints.
- Loot pouches can hold four stacks of a specific item type or compatible equipment; exact rules need testing.
- Players may be unable to freely throw away loot; a sacrifice action replaces dropping in the risk/greed loop.
- Ammo such as arrows may be tracked as an abstract resource rather than physical stacks.
- Collected resources brought to base may be moved automatically into a communal/personal stash, except items such as equipment and pouches.
- Proposed hold-click weapon swapping and short switching cooldowns should be revisited for ergonomics.
  Weapons and armour
- Weapon sources: initial loadouts, forging at base, and dungeon loot.
- Weapons may hold runes/cards/modifiers with a finite slot capacity.
- Forged weapons may have a trade-off versus found gear (e.g. fewer rune slots).
- Weapons can be reforged, upgraded, enchanted, scrapped or replaced.
- Scrapping may return materials and sometimes a weaker form of an enchantment.
- Two equipped weapon slots should support distinct combat roles; a proposed secondary-slot speed penalty is tentative.
- Armour comes in themed sets with synergy, but mix-and-match options and weighted stat rolls.
- Possible gear materials by ascending tier include Mana Steel, Elixrite, Astrite, Lunarite/Lunarium, Ignium, Iridium, Mythril, Arcanium and Prismatic. The board contains alternative spellings and level ranges: do not silently canonicalize conflicting details.
- Proposed rarities include common, uncommon, rare, epic and legendary.
- The board lists sample weapon stats and floor bands; do not treat them as final balance.
  Loot and economy
- Loot chests/crates, enemy drops, coins, materials, books and found equipment underpin the loop.
- A paxel may be the preferred tool for breaking crates or coin piles.
- Magnets may simplify picking up loot.
- Gold is the principal proposed currency, earned through enemy drops and sacrifice.
- Gold uses include upgrades, crafting, shops, research/abilities, revives and eligible gear buyback.
- A distinction between ordinary gold and 'mythic coins' appears in older trinket notes; resolve before implementing currencies.
8. Cards, decks and run modifiers
- A roguelite deck-building system changes runs through modifier cards.
- Cards can modify dungeon rules, build traits, rewards and gear, with the precise split between deck types unresolved.
- A preliminary 12-slot base deck appears in planning, with capacity potentially affected by keys, player progress and resources.
- Booster packs can appear or be purchased between floors, offer choices of different rarities and replace or add cards.
- Proposed pack directions include loot-oriented, fighter and defender themes.
- Cards can carry costs, including corruption, to make strong pre-run setups a meaningful trade-off.
- Other future systems mentioned include augments, relics and more elaborate modifiers, but these are not necessarily first-milestone features.
- The UI will need capacity tracking, searchable/inspectable cards, statistics, swap/replace interactions and clear before/after previews.
9. Sacrifice, factions and NPCs
   Sacrifice
- Inventory deletion slot: optionally converts unwanted loot to around 10% of its value.
- Floor 0 altars may allow surrendering items for gold.
- God's altars may request specific items and grant favour or special bonuses.
- Sacrifice has story meaning, especially in relation to the god of greed.
- Do not confuse a normal item sale/sacrifice with god-favour mechanics.
  Resistance
- The resistance helps players progress and establish their base, often through recruitable NPCs.
- Likely roles: leader, armorer/forger, archivist, shopkeepers and other support.
- Some NPC stations and utility functions unlock over time.
- NPCs may be movable in the customizable base; implementation method remains open.
  Cult
- Cult contracts offer powerful early-game help at the cost of later consequences.
- A cult relationship may disable or weaken resistance relationships.
- Cult shops may sell stronger but more expensive upgrades.
- Contracts may persist until death; story-dependent penalties are still design ideas.
10. Trinkets and example concepts
    Trinkets are equipment with special activated effects and relatively long cooldowns, often configured at base.

Proposed examples:
- Bail Stone: Escape the dungeon early at the cost of gathered loot or other run penalties.
- Orb of Avarice: Spend a limited amount/percentage of currency to highlight nearby crates, coins and knowledge sources for a time.
- Healing Lotus: A regeneration effect balanced by a temporary weakness penalty.
  These are content concepts, not instructions to implement them during the technical framework phase. Exact percentages, durations and effects are not final.
11. Multiplayer and shared play
- Multiple players may share The Abyss base and participate in dungeon runs.
- Possible mechanics include party progression, shared/individual loot rules, revives and soul drops on death.
- The economy may permit gold transfers between party members.
- There are unanswered questions about synchronization, persistence, instance boundaries, group matchmaking, grief prevention and what happens if a teammate leaves a run.
- Future implementations must be server authoritative, synchronize client-visible state and avoid assuming single-player integrated-server behavior.
12. Unresolved design questions — do not decide without direction
- Whether the normal overworld has a substantive gameplay role, and what can cross the dungeon boundary.
- Whether floor 0 is a wholly buildable void, a collection of customizable rooms, or both.
- Whether loadouts are fixed starting archetypes, permanent customizable builds or both.
- Exactly what persists across death, bailouts, floor exits and checkpoint runs.
- Whether unrestricted item dropping is prohibited and how sacrifice replaces it.
- Whether card decks belong to keys, equipment, whole runs or multiple layers.
- Whether greed is clamped to 0–100 or can go temporarily negative.
- How corruption, monarch spawns, time pressure and player skill interact.
- Actual floor themes, bosses, enemy roster, rates, recipes and final world lore.
- Progression and content ownership in multiplayer.
- How later-stage augments/relics interact with the card system.
- Final UI style direction and exact balance formulas.
  Record choices explicitly when the user decides them; do not quietly turn brainstorming notes into binding requirements.
13. Current technical framework priorities
    Do this before gameplay features. Implement reusable developer systems and proof-of-functionality screens/items only where needed to test infrastructure.
    Build and dependency baseline
- NeoForge for Minecraft 26.3 with the official ModDevGradle setup and supported Java toolchain.
- Proper, verified UI Lib dependency and a demonstrably real UI Lib screen.
- Single Gradle project with clean packages initially, rather than premature subprojects.
- Safe dedicated-server loading with client classes isolated correctly.
  Central JSON-driven UI
- One editable master file: config/avarice-dev/ui.json.
- Global colours, typography, spacing, padding, margins, borders, opacity and theme tokens.
- Per-screen positions, dimensions and component style references.
- Screen, parent and element-relative anchors, pivots and percentage/relative sizing.
- UI Lib adapter applies current definitions without hardcoded per-screen constants.
  Development UI controls
- F8: In-game mouse and element inspector, GUI-scaled coordinates, selected/hovered element bounds, parent-relative values, grid/crosshair and copy-to-clipboard.
- F9: Manual UI reload while the screen is open.
- Automatic file-save reload: Java WatchService or equivalent; debounce edits and marshal UI updates to the client thread.
- Validate complete candidate configuration before an immutable atomic swap; keep last valid state on errors and display diagnostics.
- Refresh currently open UI Lib screens, preserving focus, scrolling and other state where feasible.
  UI animation support
- Reloadable durations, delays, easing functions and animation tracks.
- Properties such as x/y, scale, opacity, dimensions, and supported colours/transforms.
- Delta-time-driven client animation; define behavior for already-running animations during reloads.
- Use UI Lib where appropriate; provide small adapters only for missing behavior.
  Reusable values/scaling editor
- Separate configuration domain such as config/avarice-dev/values.json.
- Numbers, percentages, percentage points vs relative percentage changes, multipliers, limits and curves.
- Preview/edit/apply/save controls; ideally a visual scaling graph.
- Values should be typed, validated, versionable and safe to reload.
  Item-definition hot reload — technical proof, not game content
- Establish a generic item-definition registry and one simple development test item if the implementation task explicitly includes it.
- Reload name/description, dynamically resolved tooltips, stat definitions, percentage modifiers and display formatting.
- Distinguish item-definition data from static registration and physically stored ItemStack data components.
- Existing stacks should consult new definitions or use an explicit safe migration path; do not promise arbitrary vanilla attribute changes happen automatically.
- Server-authoritative definitions must synchronize where client tooltips need them.
- New registered item types and structural Java code changes generally still require restart.
- Differentiate quick JSON reload from Minecraft full resource/model/texture reload.
  Reload engine rules
- Independent named reload domains with validation, apply, rollback/last-good behavior and diagnostics.
- Never parse files every frame.
- Never mutate GUI widgets from file-watcher threads.
- Do not claim tests passed unless run.
- Avoid full client asset/resource reload merely to move a button.
- Keep dev-only file watching, inspectors and commands off in production.
14. Commenting style — mandatory for code agents
    The user's preferred source-code comment style is:
- all lowercase
- no punctuation
- single line in 99 percent of cases
- no paragraphs
- short explanations only
- no bland comments narrating obvious code
- no emojis
  How to apply it
- Comments should explain a subtle reason, constraint, edge case or non-obvious intent.
- Do not narrate what the next line of code already says.
- Prefer no comment over an empty or generic one.
- Use one-line // comments almost always. A multiline block is permitted only when genuinely required for a nontrivial constraint or API contract.
- Do not turn whole files into commentary. Good naming, types and small methods replace many comments.
- For JavaDoc required by tooling, use the minimum syntactically necessary markup while keeping explanatory prose brief and lowercase where practical.
- Do not damage Java syntax, JSON, strings, user-facing text, Gradle identifiers or API names to satisfy comment style. The rule applies to human-written explanatory comments, not code or specification text.
- This AGENTS.md document may use normal capitalization, grammar and Markdown punctuation for readability.
  Good comments
  // keep the last valid layout when parsing fails
  // apply ui changes on the client thread
  // the server owns the final stat value
  // preserve the focused element during rebuild
  Do not write
  // This method reloads the configuration.
  // Initialize the manager.
  // Update the value.
  // Here we are going to perform several important steps in order to...
  // TODO: fix this later!
  Do not add generic file headers or comments explaining class names or trivial getters. Existing comments need not be rewritten unless changing that code.
15. Naming style — mandatory for code agents
- Use short, domain-first names that a developer would naturally use in conversation.
- Prefer names such as `PlayerData`, `AbyssManager`, `Network`, `Coins`, and `FloorBuilder`.
- Do not use filler words such as `Avarice`, `Service`, `Development`, `Temporary`, `Helper`, or `Utility` in Java type names unless they distinguish a real public API.
- Use `Manager` only for a type that owns lifecycle or coordination; use a noun for data and a verb-free noun for focused operations.
- Keep package names and resource identifiers stable unless a migration is explicitly required.
15. Expectations for any implementing AI
1. Inspect the repository and current build files before modifying anything.
2. Identify whether work belongs to UI, reload, shared utility, or authoritative server logic.
3. State assumptions and mark anything not settled by the design.
4. Follow current NeoForge 26.3 and UI Lib versions/APIs verified from sources or local dependencies.
5. Make the smallest working vertical slice and compile/test it.
6. Include test coverage for nontrivial parsing, anchors, easing, percentages, scaling and rollback behavior.
7. Report changed files, actual results, unverified items and any version-specific limitations.
8. Do not add dungeons, NPCs, abilities, equipment content, loot tables or story implementations during the framework-only milestone.
9. Follow the code commenting rules in section 14 consistently.
10. Treat this file as local project context. Do not automatically commit, stage, push or publish it.
16. Miro context and change control
    The planning board contains raw ideas, duplicates and occasionally contradictory proposals. It is an evolving design space, not a fixed specification. The most recent explicit user instruction takes precedence over older brainstorming notes. When meaningful design choices are made, ask whether to update the board rather than altering its existing content unprompted.

Board: https://miro.com/app/board/uXjVL1pfGss=/

Do not edit AGENTS.md solely to reflect tentative proposals unless they are clearly labeled as tentative.
