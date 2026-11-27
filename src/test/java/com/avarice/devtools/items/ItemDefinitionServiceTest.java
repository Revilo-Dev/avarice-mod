package com.avarice.devtools.items;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import com.revilodev.avarice.devtools.items.ItemDefinitionService;
import org.junit.jupiter.api.Test;

class ItemDefinitionServiceTest {
    @Test void parsesOptionalDynamicDisplayFields() {
        var definitions = ItemDefinitionService.parse("[{\"id\":\"test\",\"name\":\"Test\",\"description\":\"Description\",\"color\":\"#FFFFFF\",\"tooltip\":[],\"stats\":{},\"percentages\":{},\"multipliers\":{},\"nameTranslation\":\"item.avarice.test\",\"rarity\":\"development\",\"metadata\":{\"source\":\"test\"}}]");
        assertEquals("item.avarice.test", definitions.get("test").nameTranslation());
        assertEquals("development", definitions.get("test").rarity());
    }

    @Test void rejectsDuplicateDefinitions() {
        String value = "[{\"id\":\"test\",\"name\":\"Test\",\"description\":\"Description\",\"color\":\"#FFFFFF\",\"tooltip\":[],\"stats\":{},\"percentages\":{},\"multipliers\":{}},{\"id\":\"test\",\"name\":\"Test\",\"description\":\"Description\",\"color\":\"#FFFFFF\",\"tooltip\":[],\"stats\":{},\"percentages\":{},\"multipliers\":{}}]";
        assertThrows(IllegalArgumentException.class, () -> ItemDefinitionService.parse(value));
    }

    @Test void parsesVersionedDefinitions() {
        String value = "{\"schema_version\":1,\"definitions\":[{\"id\":\"test\",\"name\":\"Test\",\"description\":\"Description\",\"color\":\"#FFFFFF\",\"tooltip\":[],\"stats\":{},\"percentages\":{},\"multipliers\":{}}]}";
        assertEquals("Test", ItemDefinitionService.parse(value).get("test").name());
    }
}
