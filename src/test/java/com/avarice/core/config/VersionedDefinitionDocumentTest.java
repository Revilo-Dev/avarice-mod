package com.avarice.core.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.google.gson.Gson;
import com.revilodev.avarice.core.config.VersionedDefinitionDocument;
import java.util.List;
import org.junit.jupiter.api.Test;

class VersionedDefinitionDocumentTest {
    @Test void acceptsLegacyArrays() {
        assertEquals(1, VersionedDefinitionDocument.definitions("[{\"id\":\"legacy\"}]", "test").size());
    }

    @Test void writesVersionedDocuments() {
        String document = VersionedDefinitionDocument.document(new Gson(), List.of(java.util.Map.of("id", "test")));
        assertEquals(1, VersionedDefinitionDocument.definitions(document, "test").size());
    }

    @Test void rejectsUnsupportedVersions() {
        assertThrows(IllegalArgumentException.class, () -> VersionedDefinitionDocument.definitions("{\"schema_version\":99,\"definitions\":[]}", "test"));
    }
}
