package com.revilodev.avarice.world;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class AvariceDimensionDefinitionTest {
    @Test
    void dimensionsUseTheAvariceVoidType() {
        assertEquals("avarice:void", resource("data/avarice/dimension/abyss.json").get("type").getAsString());
        assertEquals("avarice:void", resource("data/avarice/dimension/dungeon.json").get("type").getAsString());
    }

    @Test
    void voidTypeUsesTheAbyssSkybox() {
        JsonObject type = resource("data/avarice/dimension_type/void.json");
        assertFalse(type.get("has_ender_dragon_fight").getAsBoolean());
        assertFalse(type.get("has_skylight").getAsBoolean());
        assertEquals("end", type.get("skybox").getAsString());
        assertEquals("avarice:abyss", type.getAsJsonObject("attributes").get("neoforge:custom_skybox").getAsString());
    }

    private static JsonObject resource(String path) {
        try (var input = AvariceDimensionDefinitionTest.class.getClassLoader().getResourceAsStream(path)) {
            if (input == null) throw new IllegalStateException("missing test resource " + path);
            return JsonParser.parseReader(new InputStreamReader(input, StandardCharsets.UTF_8)).getAsJsonObject();
        } catch (java.io.IOException exception) {
            throw new IllegalStateException("failed to read test resource " + path, exception);
        }
    }
}
