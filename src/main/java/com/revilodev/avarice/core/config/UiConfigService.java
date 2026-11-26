package com.revilodev.avarice.core.config;

import com.revilodev.avarice.Avarice;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;

/** Owns the active UI snapshot. Failed loads never replace the last valid snapshot. */
public final class UiConfigService {
    public static final Path FILE = Path.of("config", "avarice-dev", "ui.json");
    private static final AtomicReference<UiConfig> ACTIVE = new AtomicReference<>();
    private static final List<java.util.function.Consumer<UiConfig>> LISTENERS = new CopyOnWriteArrayList<>();
    private static final UiConfigParser PARSER = new UiConfigParser();
    private UiConfigService() { }
    public static synchronized void initialize() { if (ACTIVE.get() == null) reload(); }
    public static UiConfig current() { UiConfig config = ACTIVE.get(); if (config == null) throw new IllegalStateException("UI config not initialized"); return config; }
    public static AutoCloseable subscribe(java.util.function.Consumer<UiConfig> listener) { LISTENERS.add(listener); return () -> LISTENERS.remove(listener); }
    public static synchronized boolean reload() {
        try {
            if (Files.notExists(FILE)) { Files.createDirectories(FILE.getParent()); Files.writeString(FILE, defaults(), StandardCharsets.UTF_8); }
            UiConfig candidate = PARSER.parse(Files.readString(FILE, StandardCharsets.UTF_8), FILE.toAbsolutePath().toString());
            ACTIVE.set(candidate); LISTENERS.forEach(listener -> listener.accept(candidate));
            return true;
        } catch (IOException | ConfigException exception) { Avarice.LOGGER.error("Could not reload {}: {}", FILE, exception.getMessage()); return false; }
    }
    private static String defaults() { return new GsonBuilder().setPrettyPrinting().create().toJson(com.google.gson.JsonParser.parseString("""
        {"schema_version":1,"theme":{"colors":{"primary":"#A78BFA","background":"#121620","surface":"#232936","text":"#FFFFFF","border":"$theme.colors.primary"},"spacing":{"small":4,"medium":8,"large":16},"typography":{"default_scale":1.0},"ui_scale":1.0,"styles":{"panel":{"background":"$theme.colors.surface","border":"$theme.colors.border","border_width":1,"corner_radius":6,"opacity":1.0,"padding":8,"margin":0}}},"animations":{"screen_open_ms":300,"screen_close_ms":200,"hover_ms":140,"easing":"ease_out_cubic"},"screens":{"development_demo":{"width":340,"height":220,"elements":{"demo_panel":{"anchor":"center","x":0,"y":0,"width":260,"height":140,"style":"panel"}}}}}
        """)); }
}
