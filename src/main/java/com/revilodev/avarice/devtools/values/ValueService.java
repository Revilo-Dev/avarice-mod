package com.revilodev.avarice.devtools.values;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.revilodev.avarice.devtools.scaling.ScalingFormula;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/** Stores independently reloadable, non-authoritative development numeric definitions. */
public final class ValueService {
    public static final Path FILE = Path.of("config", "avarice-dev", "values.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final AtomicReference<Map<String, ValueDefinition>> ACTIVE = new AtomicReference<>(Map.of());
    private ValueService() { }
    public static synchronized void initialize() { if (ACTIVE.get().isEmpty()) reload(); }
    public static Map<String, ValueDefinition> current() { return ACTIVE.get(); }
    public static synchronized boolean reload() { try { if (Files.notExists(FILE)) { Files.createDirectories(FILE.getParent()); saveDefaults(); } Type type = new TypeToken<List<ValueDefinition>>() { }.getType(); List<ValueDefinition> values = GSON.fromJson(Files.readString(FILE), type); if (values == null) throw new IllegalArgumentException("Value file must contain an array"); Map<String, ValueDefinition> parsed = values.stream().peek(ValueService::validate).collect(Collectors.toUnmodifiableMap(ValueDefinition::id, value -> value)); ACTIVE.set(parsed); return true; } catch (IOException | RuntimeException exception) { return false; } }
    public static synchronized void save(Map<String, ValueDefinition> values) throws IOException { Files.createDirectories(FILE.getParent()); Files.writeString(FILE, GSON.toJson(values.values()), StandardCharsets.UTF_8); ACTIVE.set(Map.copyOf(values)); }
    private static void saveDefaults() throws IOException { save(Map.of("demo_multiplier", new ValueDefinition("demo_multiplier", 1, 1, 0.1, 3, ValueDefinition.Kind.MULTIPLIER, new ScalingFormula(10, 2, 1, 0, 9999)))); }
    public static synchronized void apply(Map<String, ValueDefinition> values) { validate(values); ACTIVE.set(Map.copyOf(values)); }
    public static void validate(Map<String, ValueDefinition> values) { if (values == null || values.isEmpty()) throw new IllegalArgumentException("Value definitions cannot be empty"); values.values().forEach(ValueService::validate); }
    private static void validate(ValueDefinition value) { if (value == null || value.kind() == null || value.formula() == null || !Double.isFinite(value.currentValue())) throw new IllegalArgumentException("Invalid value definition"); }
}
