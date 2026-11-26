package com.revilodev.avarice.devtools.items;

import com.revilodev.avarice.Avarice;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/** Atomic item-definition snapshot. A malformed file cannot replace active definitions. */
public final class ItemDefinitionService {
    public static final Path FILE = Path.of("config", "avarice-dev", "items.json");
    private static final AtomicReference<Map<String, ItemDefinition>> ACTIVE = new AtomicReference<>(Map.of());
    private static final Gson GSON = new Gson();
    private ItemDefinitionService() { }
    public static synchronized void initialize() { if (ACTIVE.get().isEmpty()) reload(); }
    public static ItemDefinition get(String id) { return ACTIVE.get().get(id); }
    public static synchronized boolean reload() { try { if (Files.notExists(FILE)) { Files.createDirectories(FILE.getParent()); Files.writeString(FILE, "[{\"id\":\"dev_relic\",\"name\":\"Developer Relic\",\"description\":\"Reloadable test item\",\"color\":\"#A78BFA\",\"tooltip\":[\"Edit config/avarice-dev/items.json\",\"Existing stacks update live\"],\"stats\":{\"power\":25.0},\"percentages\":{\"crit\":20.0},\"multipliers\":{\"damage\":1.25}}]"); } Type type = new TypeToken<List<ItemDefinition>>() {}.getType(); List<ItemDefinition> definitions = GSON.fromJson(Files.readString(FILE), type); Map<String, ItemDefinition> candidate = definitions.stream().peek(ItemDefinitionService::validate).collect(Collectors.toUnmodifiableMap(ItemDefinition::id, definition -> definition)); ACTIVE.set(candidate); Avarice.LOGGER.info("Reloaded {} item definitions", candidate.size()); return true; } catch (IOException | RuntimeException exception) { Avarice.LOGGER.error("Item definitions reload failed; keeping prior definitions", exception); return false; } }
    private static void validate(ItemDefinition value) { if (value == null || value.id() == null || value.id().isBlank() || value.name() == null || value.tooltip() == null || value.stats() == null || value.percentages() == null || value.multipliers() == null) throw new IllegalArgumentException("Invalid item definition"); }
}
