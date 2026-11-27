package com.revilodev.avarice.core.reload;

import com.revilodev.avarice.core.config.UiConfigStore;
import com.revilodev.avarice.devtools.items.ItemDefs;
import com.revilodev.avarice.devtools.values.Values;
import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.Set;

public final class AvariceReloads {
    public static final String UI = "ui";
    public static final String VALUES = "values";
    public static final String ITEMS = "items";
    private static final ReloadManager MANAGER = new ReloadManager();
    private static boolean initialized;

    private AvariceReloads() { }

    public static synchronized void initialize() {
        if (initialized) return;
        MANAGER.register(domain(UI, UiConfigStore::reload));
        MANAGER.register(domain(VALUES, Values::reload));
        MANAGER.register(domain(ITEMS, ItemDefs::reload));
        initialized = true;
    }

    public static ReloadManager.ReloadDiagnostic reload(String domain) {
        initialize();
        return MANAGER.reload(domain);
    }

    public static Set<ReloadManager.ReloadDiagnostic> reloadChanged(Set<Path> paths) {
        initialize();
        Set<String> domains = new LinkedHashSet<>();
        for (Path path : paths) {
            String name = path.getFileName().toString();
            if (name.equals("ui.json") || name.equals("animations.json")) domains.add(UI);
            else if (name.equals("values.json")) domains.add(VALUES);
            else if (name.equals("items.json") || path.toString().contains("item-definitions")) domains.add(ITEMS);
        }
        Set<ReloadManager.ReloadDiagnostic> results = new LinkedHashSet<>();
        domains.forEach(domain -> results.add(reload(domain)));
        return Set.copyOf(results);
    }

    private static ReloadableDomain<Boolean> domain(String id, java.util.function.BooleanSupplier reload) {
        return new ReloadableDomain<>() {
            @Override public String id() { return id; }
            @Override public Boolean load() { return reload.getAsBoolean(); }
            @Override public void validate(Boolean success) { if (!success) throw new IllegalStateException(id + " reload failed"); }
            @Override public void apply(Boolean success) { }
        };
    }
}
