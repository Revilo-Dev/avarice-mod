package com.revilodev.avarice.core.config;

import com.google.gson.JsonObject;

public final class UiConfigMigrations {
    public static final int CURRENT_VERSION = 1;

    private UiConfigMigrations() { }

    public static JsonObject migrate(JsonObject source, String location) throws ConfigException {
        JsonObject candidate = source.deepCopy();
        int version = candidate.has("schema_version") ? candidate.get("schema_version").getAsInt() : 0;
        if (version < 0 || version > CURRENT_VERSION) throw new ConfigException(location + ": unsupported UI schema version " + version);
        while (version < CURRENT_VERSION) {
            if (version == 0) {
                candidate.addProperty("schema_version", 1);
                version = 1;
            }
        }
        return candidate;
    }
}
