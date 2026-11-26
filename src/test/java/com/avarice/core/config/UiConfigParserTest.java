package com.avarice.core.config;

import static org.junit.jupiter.api.Assertions.*;

import com.revilodev.avarice.core.config.ConfigException;
import com.revilodev.avarice.core.config.UiConfig;
import com.revilodev.avarice.core.config.UiConfigParser;
import org.junit.jupiter.api.Test;

class UiConfigParserTest {
    @Test void resolvesThemeReference() throws Exception { UiConfig config = new UiConfigParser().parse("{\"schema_version\":1,\"theme\":{\"colors\":{\"a\":\"#112233\",\"b\":\"$theme.colors.a\"},\"spacing\":{},\"typography\":{\"default_scale\":1}},\"animations\":{},\"screens\":{}}", "test"); assertEquals(config.theme().colors().get("a"), config.theme().colors().get("b")); }
    @Test void rejectsCircularReference() { assertThrows(ConfigException.class, () -> new UiConfigParser().parse("{\"schema_version\":1,\"theme\":{\"colors\":{\"a\":\"$theme.colors.b\",\"b\":\"$theme.colors.a\"},\"spacing\":{},\"typography\":{}},\"animations\":{},\"screens\":{}}", "test")); }
}
