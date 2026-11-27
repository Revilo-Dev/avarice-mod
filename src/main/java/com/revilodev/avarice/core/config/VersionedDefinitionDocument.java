package com.revilodev.avarice.core.config;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Collection;

public final class VersionedDefinitionDocument {
    public static final int CURRENT_VERSION = 1;

    private VersionedDefinitionDocument() { }

    public static JsonArray definitions(String json, String label) {
        JsonElement source = JsonParser.parseString(json);
        if (source.isJsonArray()) return source.getAsJsonArray();
        if (!source.isJsonObject()) throw new IllegalArgumentException(label + " must contain an array or versioned object");
        JsonObject document = source.getAsJsonObject();
        int version = document.has("schema_version") ? document.get("schema_version").getAsInt() : 0;
        if (version < 0 || version > CURRENT_VERSION) throw new IllegalArgumentException(label + " has unsupported schema version " + version);
        JsonElement definitions = document.get("definitions");
        if (definitions == null || !definitions.isJsonArray()) throw new IllegalArgumentException(label + " requires an array at $.definitions");
        return definitions.getAsJsonArray();
    }

    public static String document(Gson gson, Collection<?> definitions) {
        JsonObject document = new JsonObject();
        document.addProperty("schema_version", CURRENT_VERSION);
        document.add("definitions", gson.toJsonTree(definitions));
        return gson.toJson(document);
    }

    public static void writeAtomically(Path file, String contents) throws IOException {
        Files.createDirectories(file.getParent());
        backupLegacyDocument(file);
        Path temporary = Files.createTempFile(file.getParent(), file.getFileName().toString(), ".tmp");
        try {
            Files.writeString(temporary, contents, StandardCharsets.UTF_8);
            try {
                Files.move(temporary, file, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
            } catch (AtomicMoveNotSupportedException exception) {
                Files.move(temporary, file, StandardCopyOption.REPLACE_EXISTING);
            }
        } finally {
            Files.deleteIfExists(temporary);
        }
    }

    private static void backupLegacyDocument(Path file) throws IOException {
        if (Files.notExists(file) || !JsonParser.parseString(Files.readString(file, StandardCharsets.UTF_8)).isJsonArray()) return;
        Path backup = file.resolveSibling(file.getFileName() + ".v0.bak");
        if (Files.notExists(backup)) Files.copy(file, backup, StandardCopyOption.COPY_ATTRIBUTES);
    }
}
